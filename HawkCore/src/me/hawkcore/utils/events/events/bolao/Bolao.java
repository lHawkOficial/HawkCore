package me.hawkcore.utils.events.events.bolao;


import java.io.File;
import java.util.ArrayList;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import lombok.Getter;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.API;
import me.hawkcore.utils.Eco;
import me.hawkcore.utils.Scoreboard;
import me.hawkcore.utils.events.EventManager;
import me.hawkcore.utils.events.events.bolao.utils.ConfigBolao;
import me.hawkcore.utils.events.events.bolao.utils.MensagensBolao;
import me.hawkcore.utils.events.utils.Event;
import me.hawkcore.utils.events.utils.enums.EventStatus;
import me.hawkcore.utils.events.utils.enums.EventType;
import me.hawkcore.utils.events.utils.interfaces.EventExecutor;
import me.hawkcore.utils.events.utils.interfaces.EventListeners;

@Getter
public class Bolao extends Event implements EventExecutor, EventListeners {

	private Task task;
	private ConfigBolao configbolao;
	private List<String> participantes = new ArrayList<>();
	private String timeRestante = "N.A";
	private File fileSaves;
	
	public Bolao(String name, File folder, FileConfiguration config, EventType type, boolean enabled) {
		super(name, folder, config, type, enabled);
		setupConfig();
		setupListeners();
		EventManager.get().getEvents().add(this);
	}

	public static Bolao get() {
		return (Bolao) EventManager.get().getEvent("bolao");
	}
	
	@Override
	public void setupConfig() {
		configbolao = new ConfigBolao(this);
		setConfigEvent(configbolao);
		setMessages(new MensagensBolao(this));
		fileSaves = new File(getFolder() + "/saves.json");
		if (!fileSaves.exists()) {
			try {
				fileSaves.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void updateScore() {
		List<Scoreboard> scores = getScoreBoardPlayers();
		for(Scoreboard score : scores) {
			score.create();
			score.setObjectiveName(configbolao.getScore_title());
			for (int i = 0; i < configbolao.getScore_lines().size(); i++) {
				score.setLine(i, configbolao.getScore_lines().get(i).replace("{total}", String.valueOf(participantes.size()))
						.replace("{quantia}", Eco.get().format(getTotal()))
						.replace("{tempo}", timeRestante)
						.replace("{valor}", Eco.get().format(configbolao.getValueJoin())));
			}
		}
	}

	public double getTotal() {
		return participantes.size() * configbolao.getValueJoin();
	}
	
	@Override
	public boolean containsPlayerOnEvent(Player p) {
		return participantes.contains(p.getName().toLowerCase());
	}

	@Override
	public void addPlayerToEspectator(Player p) {
	}
	
	@Override
	public void addPlayerToEvent(Player p, Event event) {
		EventExecutor.super.addPlayerToEvent(p, event);
		participantes.add(p.getName().toLowerCase());
		Eco.get().withdrawPlayer(p, configbolao.getValueJoin());
	}

	@Override
	public void removePlayerFromEspectator(Player p) {
	}

	@Override
	public void start() {
		if (getEventStatus() == EventStatus.INGAME) return;
		setEventStatus(EventStatus.INGAME);
		if (task != null) task.cancel();
		task = new Task();
		task.setRunnable(new Runnable() {
			long timeWarn, time = System.currentTimeMillis();
			@Override
			public void run() {
				timeRestante = API.get().formatTime((configbolao.getTime()*60 - (int)TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()-time)));
				if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()-timeWarn) >= configbolao.getTimeWarn()) {
					timeWarn = System.currentTimeMillis();
					Bukkit.getOnlinePlayers().forEach(p -> {
						MensagensBolao.get().getOpen().forEach(msg -> p.sendMessage(msg
								.replace("{total}", String.valueOf(participantes.size()))
								.replace("{quantia}", Eco.get().format(getTotal()))
								.replace("{tempo}", timeRestante)
								.replace("{valor}", Eco.get().format(configbolao.getValueJoin()))));
						p.playSound(p.getLocation(), Sound.NOTE_BASS, 0.5f, 0.5f);
					});
					if (configbolao.isScore_active()) updateScore();
				}
				if (TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis()-time) >= configbolao.getTime()) {
					task.cancel();
					finish();
				}
			}
		});
	}

	@Override
	public void closed() {
	}

	@Override
	public void forceStart() {
	}

	@Override
	public void warning() {
	}

	@Override
	public void stop() {
		setEventStatus(EventStatus.STOPPED);
		if (task != null) task.cancel();
		getScoreBoardPlayers().forEach(score -> score.destroy());
	}

	@Override
	public void finish() {
		stop();
	}

	@Override
	public void onChat(AsyncPlayerChatEvent e) {
	}

	@Override
	public void onMove(PlayerMoveEvent e) {
	}

	@Override
	public void onClickInventory(InventoryClickEvent e) {
	}

	@Override
	public void onQuit(PlayerQuitEvent e) {
	}

	@Override
	public void onCommands(PlayerCommandPreprocessEvent e) {
	}

	@Override
	public void onDamageEntity(EntityDamageByEntityEvent e) {
	}

	@Override
	public void onInteract(PlayerInteractEvent e) {
		
	}

	@Override
	public void onBreakBlock(BlockBreakEvent e) {
	}

	@Override
	public void onPlaceBlock(BlockPlaceEvent e) {
	}

	@Override
	public void onDeath(PlayerDeathEvent e) {
	}

	@Override
	public void onHunger(FoodLevelChangeEvent e) {
	}

}
