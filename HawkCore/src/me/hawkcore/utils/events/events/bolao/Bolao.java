package me.hawkcore.utils.events.events.bolao;


import java.io.File;
import java.util.ArrayList;

import java.util.List;
import java.util.Random;
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

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import lombok.Getter;
import me.hawkcore.Core;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.API;
import me.hawkcore.utils.Eco;
import me.hawkcore.utils.Save;
import me.hawkcore.utils.Scoreboard;
import me.hawkcore.utils.events.EventManager;
import me.hawkcore.utils.events.events.bolao.utils.ConfigBolao;
import me.hawkcore.utils.events.events.bolao.utils.MensagensBolao;
import me.hawkcore.utils.events.utils.Event;
import me.hawkcore.utils.events.utils.RankingEvent;
import me.hawkcore.utils.events.utils.enums.EventStatus;
import me.hawkcore.utils.events.utils.enums.EventType;
import me.hawkcore.utils.events.utils.interfaces.EventExecutor;
import me.hawkcore.utils.events.utils.interfaces.EventListeners;
import me.hawkcore.utils.events.utils.listeners.ChangeTopEvent;

@Getter
public class Bolao extends Event implements EventExecutor, EventListeners {

	private Task task;
	private ConfigBolao configbolao;
	private List<String> participantes = new ArrayList<>();
	private String timeRestante = "N.A";
	
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
		File fileSaves = new File(getFolder() + "/saves.json");
		if (!fileSaves.exists()) {
			try {
				fileSaves.createNewFile();
				setFileSaves(fileSaves);
				save();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		setFileSaves(fileSaves);
		load();
	}
	
	@Override
	public void updateScore() {
		List<Scoreboard> scores = getScoreBoardPlayers();
		for(Scoreboard score : scores) {
			score.setObjectiveName(configbolao.getScore_title());
			for (int i = 0; i < configbolao.getScore_lines().size(); i++) {
				score.setLine(i, configbolao.getScore_lines().get(i).replace("{total}", String.valueOf(participantes.size()))
						.replace("{quantia}", Eco.get().format(getTotal()))
						.replace("{tempo}", timeRestante)
						.replace("{valor}", Eco.get().format(configbolao.getValueJoin())));
			}
			score.create();
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
		if (!getRanking().getTops().containsKey(p.getName())) getRanking().getTops().put(p.getName(), 0);
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
			long timeWarn, updateScores, time = System.currentTimeMillis();
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
					
				}
				if (configbolao.isScore_active() && System.currentTimeMillis()-updateScores >= 800) {
					updateScore();
				}
				if (TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis()-time) >= configbolao.getTime()) {
					time = -1;
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

	@SuppressWarnings("deprecation")
	@Override
	public void finish() {
		stop();
		RankingEvent ranking = getRanking();
		participantes.forEach(name -> {
			Player target = Bukkit.getPlayerExact(name);
			if (target!=null) target.removeMetadata("event", Core.getInstance());
		});
		if (participantes.size() > 1) {
			String name = participantes.isEmpty() ? null : participantes.get(new Random().nextInt(participantes.size()));
			Bukkit.getOnlinePlayers().forEach(p -> {
				MensagensBolao.get().getFinish().forEach(msg -> p.sendMessage(msg
						.replace("{total}", String.valueOf(participantes.size()))
						.replace("{quantia}", Eco.get().format(getTotal()))
						.replace("{tempo}", timeRestante)
						.replace("{valor}", Eco.get().format(configbolao.getValueJoin()))
						.replace("{player}", name)));
				p.playSound(p.getLocation(), Sound.NOTE_BASS, 0.5f, 0.5f);
			});
			Eco.get().depositPlayer(name, getTotal());
			ranking.getTops().put(name, ranking.getTops().containsKey(name) ? ranking.getTops().get(name)+1 : 1);
		}else {
			Bukkit.getOnlinePlayers().forEach(p -> {
				MensagensBolao.get().getStop().forEach(msg -> p.sendMessage(msg
						.replace("{total}", String.valueOf(participantes.size()))
						.replace("{quantia}", Eco.get().format(getTotal()))
						.replace("{tempo}", timeRestante)
						.replace("{valor}", Eco.get().format(configbolao.getValueJoin()))));
				p.playSound(p.getLocation(), Sound.NOTE_BASS, 0.5f, 0.5f);
			});
			for(String name : participantes) {
				Eco.get().depositPlayer(name, configbolao.getValueJoin());
			}
		}
		getRanking().updateAsync();
		participantes.clear();
		save();
	}

	@Override
	public void onChat(ChatMessageEvent e) {
		Player p = e.getSender();
		if (!e.getTags().contains("bolao")) return;
		if (getRanking().getTop() == null) return;
		if (!getRanking().getTop().equalsIgnoreCase(p.getName())) return;
		e.setTagValue("bolao", configbolao.getTag_mito());
	}
	
	@Override
	public void onChatVanilla(AsyncPlayerChatEvent e) {
	}

	@Override
	public void onMove(PlayerMoveEvent e) {
	}

	@Override
	public void onClickInventory(InventoryClickEvent e) {
	}

	@Override
	public void onQuit(PlayerQuitEvent e) {
		removeScoreBoard(e.getPlayer());
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

	@Override
	public void save() {
		List<Object> lista = new ArrayList<>();
		for(String name : getRanking().getTops().keySet()) {
			lista.add(name+":"+getRanking().getTops().get(name));
		}
		new Save(getFileSaves(), lista);
	}

	@Override
	public void load() {
		List<Object> lista = Save.load(getFileSaves());
		if (lista == null || lista.isEmpty()) return;
		for(Object object : lista) {
			String[] args = ((String)object).split(":");
			String name = args[0];
			int value = Integer.valueOf(args[1]);
			getRanking().getTops().put(name, value);
		}
		getRanking().update();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onRankingUpdate(ChangeTopEvent e) {
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.sendMessage(" ");
			p.sendMessage(configbolao.getMito().split("<nl>")[0].replace("{player}", e.getTop()));
			p.sendMessage(configbolao.getMito().split("<nl>")[1].replace("{player}", e.getTop()));
			p.sendMessage(" ");
			p.sendTitle(configbolao.getMito().split("<nl>")[0].replace("{player}", e.getTop()), configbolao.getMito().split("<nl>")[1].replace("{player}", e.getTop()));
			p.playSound(p.getLocation(), Sound.NOTE_PLING, 0.5f, 10);
		}
	}

}
