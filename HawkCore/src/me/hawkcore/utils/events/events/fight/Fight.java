package me.hawkcore.utils.events.events.fight;


import java.io.File;






import java.util.ArrayList;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import lombok.Getter;
import lombok.Setter;
import me.HTags.ListenersPlugin.PlayerUpdateTagEvent;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.API;
import me.hawkcore.utils.Eco;
import me.hawkcore.utils.Save;
import me.hawkcore.utils.Scoreboard;
import me.hawkcore.utils.events.EventManager;
import me.hawkcore.utils.events.events.fight.menus.MenuFight;
import me.hawkcore.utils.events.events.fight.utils.ConfigFight;
import me.hawkcore.utils.events.events.fight.utils.MensagensFight;
import me.hawkcore.utils.events.utils.Event;
import me.hawkcore.utils.events.utils.enums.EventStatus;
import me.hawkcore.utils.events.utils.enums.EventType;
import me.hawkcore.utils.events.utils.interfaces.EventExecutor;
import me.hawkcore.utils.events.utils.interfaces.EventListeners;
import me.hawkcore.utils.events.utils.listeners.ChangeTopEvent;
import me.hawkcore.utils.items.Item;
import me.hawkcore.utils.menus.MenuAPI;

@Getter
public class Fight extends Event implements EventExecutor, EventListeners {

	private Task task;
	private ConfigFight configfight;
	private String timeRestante = "N.A";
	private MenuFight menu;
	private Item iconLeave;
	private Player win;
	@Setter
	private Location locationEspectator;
	
	public Fight(String name, File folder, FileConfiguration config, EventType type, boolean enabled) {
		super(name, folder, config, type, enabled);
		setupConfig();
		setupListeners();
	}

	public static Fight get() {
		return (Fight) EventManager.get().getEvent("fight");
	}
	
	@Override
	public void setupConfig() {
		configfight = new ConfigFight(this);
		setConfigEvent(configfight);
		setMessages(new MensagensFight(this));
		setIcon(MenuAPI.getItemFromSection(getConfig().getConfigurationSection("Icon")));
		this.menu = new MenuFight(getConfig().getString("MenuMain.title").replace("&", "§"), getConfig().getInt("MenuMain.row"), getConfig().getStringList("MenuMain.Glass"));
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
		this.iconLeave = MenuAPI.getItemFromSection(getConfig().getConfigurationSection("IconLeave"));
		setFileSaves(fileSaves);
		load();
	}
	
	@Override
	public void updateScore() {
		List<Scoreboard> scores = getScoreBoardPlayers();
		for(Scoreboard score : scores) {
			score.setObjectiveName(configfight.getScore_title());
			for (int i = 0; i < configfight.getScore_lines().size(); i++) {
				score.setLine(i, configfight.getScore_lines().get(i).replace("{total}", String.valueOf(getPlayers().size()))
						.replace("{tempo}", timeRestante)
						.replace("{valor}", Eco.get().format(configfight.getValueJoin())));
			}
			score.create();
		}
	}
	
	@Override
	public boolean containsPlayerOnEvent(Player p) {
		return getPlayers().containsKey(p);
	}
	
	@Override
	public void onTeleport(PlayerTeleportEvent e) {
		Player p = e.getPlayer();
		Task.run(()->{
			Scoreboard board = getScoreBoardPlayer(p.getName());
			if (board!=null)board.destroy();
		});
	}
	
	@Override
	public void addPlayerToEvent(Player p, Event event) {
		EventExecutor.super.addPlayerToEvent(p, event);
		
		
		
	}
	
	@Override
	public void removePlayerFromEvent(Player p) {
		EventExecutor.super.removePlayerFromEvent(p);
		
	}

	@Override
	public void removePlayerFromEspectator(Player p) {
	}

	@Override public void closed() {}

	@Override public void forceStart() {}

	@Override public void warning() {}

	@Override
	public void start() {
		
	}
	
	@Override
	public void stop() {
		if (getEventStatus() == EventStatus.STOPPED) return;
		if (task != null) task.cancel();
		setEventStatus(EventStatus.STOPPED);
		Task.run(()->Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "asb reload"));
	}
	
	@Override
	public void finish() {
		
	}
	
	@Override
	public void onChat(ChatMessageEvent e) {
		
	}

	@Override
	public void onQuit(PlayerQuitEvent e) {
		
	}

	@Override
	public void save() {
		List<Object> lista = new ArrayList<>();
		lista.add(getLocationExit() == null ? "N.A" : API.get().serialize(getLocationExit()));
		lista.add(getLocationLobby() == null ? "N.A" : API.get().serialize(getLocationLobby()));
		lista.add(getLocationStart() == null ? "N.A" : API.get().serialize(getLocationStart()));
		lista.add(getLocationEspectator() == null ? "N.A" : API.get().serialize(getLocationEspectator()));
		List<String> nomes = new ArrayList<>();
		for(String name : getRanking().getTops().keySet()) {
			nomes.add(name+":"+getRanking().getTops().get(name));
		}
		lista.add(nomes);
		new Save(getFileSaves(), lista);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void load() {
		List<Object> lista = Save.load(getFileSaves());
		if (lista == null || lista.isEmpty()) return;
		setLocationExit(((String)lista.get(0)).equalsIgnoreCase("N.A")?null:API.get().unserialize(((String)lista.get(0))));
		setLocationLobby(((String)lista.get(1)).equalsIgnoreCase("N.A")?null:API.get().unserialize(((String)lista.get(1))));
		setLocationStart(((String)lista.get(2)).equalsIgnoreCase("N.A")?null:API.get().unserialize(((String)lista.get(2))));
		setLocationEspectator(((String)lista.get(3)).equalsIgnoreCase("N.A")?null:API.get().unserialize(((String)lista.get(3))));
		List<Object> objects = (List<Object>) lista.get(4);
		for(Object object : objects) {
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
			p.sendMessage(configfight.getMito().split("<nl>")[0].replace("{player}", e.getTop()));
			p.sendMessage(configfight.getMito().split("<nl>")[1].replace("{player}", e.getTop()));
			p.sendMessage(" ");
			p.sendTitle(configfight.getMito().split("<nl>")[0].replace("{player}", e.getTop()), configfight.getMito().split("<nl>")[1].replace("{player}", e.getTop()));
			p.playSound(p.getLocation(), Sound.NOTE_PLING, 0.5f, 10);
		}
	}

	@Override public void addPlayerToEspectator(Player p) {}
	
	@Override public void onCommands(PlayerCommandPreprocessEvent e) {
		e.setCancelled(true);
		Player p = e.getPlayer();
		String[] args = e.getMessage().replaceFirst("/", new String()).split(" ");
		if (args[0].equalsIgnoreCase("sair")) {
			removePlayerFromEvent(p);
			return;
		}
		for(String cmd : configfight.getCommands()) {
			if (cmd.equalsIgnoreCase(args[0])) {
				e.setCancelled(false);
				return;
			}
		}
		p.sendMessage(MensagensFight.get().getCommandBloqued());
		p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
	}

	@Override public void onDamageEntity(EntityDamageByEntityEvent e) {
		e.setCancelled(true);
	}
	
	@Override
	public void damage(EntityDamageEvent e) {
		e.setCancelled(true);
	}

	@Override public void onInteract(PlayerInteractEvent e) {
		
	}

	@Override public void onBreakBlock(BlockBreakEvent e) {}

	@Override public void onPlaceBlock(BlockPlaceEvent e) {}

	@Override public void onDeath(PlayerDeathEvent e) {
		
	}

	@Override public void onHunger(FoodLevelChangeEvent e) {
		
	}
	
	@Override public void onJoin(PlayerJoinEvent e) {}
	
	@Override public void onChatVanilla(AsyncPlayerChatEvent e) {}

	@Override public void onMove(PlayerMoveEvent e) {}

	@Override public void onClickInventory(InventoryClickEvent e) {
		
	}

	public boolean isConfigured() {
		return getLocationExit() != null && getLocationLobby() != null && getLocationStart() != null && getLocationEspectator() != null;
	}
	
	@Override
	public void playerDropItem(PlayerDropItemEvent e) {
		
	}

	@Override
	public void tagUpdate(PlayerUpdateTagEvent e) {
		
	}

}
