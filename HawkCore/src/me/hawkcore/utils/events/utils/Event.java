package me.hawkcore.utils.events.utils;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
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
import lombok.Setter;
import me.hawkcore.Core;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.Scoreboard;
import me.hawkcore.utils.events.utils.enums.EventStatus;
import me.hawkcore.utils.events.utils.enums.EventType;
import me.hawkcore.utils.events.utils.enums.PlayerType;
import me.hawkcore.utils.events.utils.interfaces.EventListeners;

@Getter
@Setter
public class Event {
	
	private String name;
	private boolean enabled;
	private EventType eventType = EventType.PVP;
	private EventStatus eventStatus = EventStatus.STOPPED;
	private MessagesEvent messages;
	private FileConfiguration config;
	private ConfigEvent configEvent;
	private RankingEvent ranking;
	private Location locationLobby, locationExit, locationStart;
	private HashMap<Player, PlayerType> players = new HashMap<>();
	private HashMap<Player, Location> queueList = new HashMap<>();
	private List<Listener> listeners = new ArrayList<>();
	private Task taskQueue;
	private Scoreboard score;
	protected Event event;
	
	public Event(String name, FileConfiguration config, EventType type, boolean enabled) {
		this.event = this;
		this.name = name;
		this.eventType = type;
		this.enabled = enabled;
		if (!enabled) return;
		this.ranking = new RankingEvent(this);
		this.configEvent = new ConfigEvent(this);
		this.messages = new MessagesEvent(this);
		this.taskQueue = new Task();
		this.taskQueue.setRunnable(new TeleportQueue(taskQueue, this, queueList));
	}
	
	public void setupListeners() {
		listeners.add(new Listener() {
			@EventHandler
			public void chat(AsyncPlayerChatEvent e) {
				Player p = e.getPlayer();
				Event event = Event.getEvent(p);
				if (event != null && event.equals(getEvent()))
				((EventListeners)event).onChat(e);
			}
			@EventHandler
			public void move(PlayerMoveEvent e) {
				Player p = e.getPlayer();
				Event event = Event.getEvent(p);
				if (event != null && event.equals(getEvent()))
				((EventListeners)event).onMove(e);
			}
			@EventHandler
			public void click(InventoryClickEvent e) {
				Player p = (Player) e.getWhoClicked();
				Event event = Event.getEvent(p);
				if (event != null && event.equals(getEvent()))
				((EventListeners)event).onClickInventory(e);
			}
			@EventHandler
			public void quit(PlayerQuitEvent e) {
				Player p = e.getPlayer();
				Event event = Event.getEvent(p);
				if (event != null && event.equals(getEvent()))
				((EventListeners)event).onQuit(e);
			}
			@EventHandler
			public void onCommands(PlayerCommandPreprocessEvent e) {
				Player p = e.getPlayer();
				Event event = Event.getEvent(p);
				if (event != null && event.equals(getEvent()))
				((EventListeners)event).onCommands(e);
			}
			@EventHandler
			public void damageEntity(EntityDamageByEntityEvent e) {
				if (!(e.getDamager() instanceof Player)) return;
				Player p = (Player) e.getDamager();
				Event event = Event.getEvent(p);
				if (event != null && event.equals(getEvent()))
				((EventListeners)event).onDamageEntity(e);
			}
			@EventHandler
			public void interact(PlayerInteractEvent e) {
				Player p = e.getPlayer();
				Event event = Event.getEvent(p);
				if (event != null && event.equals(getEvent()))
				((EventListeners)event).onInteract(e);
			}
			@EventHandler
			public void breakBlock(BlockBreakEvent e) {
				Player p = e.getPlayer();
				Event event = Event.getEvent(p);
				if (event != null && event.equals(getEvent()))
				((EventListeners)event).onBreakBlock(e);
			}
			@EventHandler
			public void placeBlock(BlockPlaceEvent e) {
				Player p = e.getPlayer();
				Event event = Event.getEvent(p);
				if (event != null && event.equals(getEvent()))
				((EventListeners)event).onPlaceBlock(e);
			}
			@EventHandler
			public void death(PlayerDeathEvent e) {
				Player p = e.getEntity();
				Event event = Event.getEvent(p);
				if (event != null && event.equals(getEvent()))
				((EventListeners)event).onDeath(e);
			}
			@EventHandler
			public void hunger(FoodLevelChangeEvent e) {
				Player p = (Player) e.getEntity();
				Event event = Event.getEvent(p);
				if (event != null && event.equals(getEvent()))
				((EventListeners)event).onHunger(e);
			}
		});
		listeners.forEach(listener -> {
			HandlerList.unregisterAll(listener);
			Bukkit.getPluginManager().registerEvents(listener, Core.getInstance());
		});
	}
	
	public void addListener(Listener listener) {
		listeners.add(listener);
		HandlerList.unregisterAll(listener);
		Bukkit.getPluginManager().registerEvents(listener, Core.getInstance());
	}
	
	public void removeListeners() {
		if (listeners.isEmpty()) return;
		listeners.forEach(listener -> HandlerList.unregisterAll(listener));
	}
	
	public static Event getEvent(Player p) {
		return p.hasMetadata("event") ? (Event) p.getMetadata("event").get(0).value() : null;
	}
	
	public List<Player> getPlayers(PlayerType type) {
		List<Player> players = new ArrayList<>();
		for(Player p : this.players.keySet()) {
			if (this.players.get(p) == type) players.add(p);
		}
		return players;
	}
	
	public void teleportPlayers(Location loc) {
		for(Player p : players.keySet()) {
			teleportPlayer(p, loc);
		}
	}
	
	public void teleportPlayer(Player p, Location loc) {
		Task task = new Task();
		task.setRunnable(()->{
			if (!p.isOnline()) {
				task.cancel();
				return;
			}
			if (queueList.containsKey(p)) return;
			queueList.put(p, loc.clone());
			task.cancel();
		});
	}
	
}
