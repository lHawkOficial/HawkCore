package me.hawkcore.utils.events.utils;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import lombok.Getter;
import lombok.Setter;
import me.hawkcore.Core;
import me.hawkcore.tasks.Task;
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
	private ConfigEvent configEvent;
	private Location locationLobby, locationExit, locationStart;
	private HashMap<Player, PlayerType> players = new HashMap<>();
	private HashMap<Player, Location> queueList = new HashMap<>();
	private Listener listeners;
	private Task taskQueue;
	protected Event event;
	
	public Event(String name, EventType type, boolean enabled) {
		this.event = this;
		this.name = name;
		this.eventType = type;
		this.enabled = enabled;
		this.configEvent = new ConfigEvent(this);
		this.messages = new MessagesEvent(this);
		this.taskQueue = new Task(new TeleportQueue(this, queueList));
		this.taskQueue.setTickRate(configEvent.getTickQueue());
	}
	
	public void setupListeners() {
		listeners = new Listener() {
			@EventHandler
			public void chat(AsyncPlayerChatEvent e) {
				Player p = e.getPlayer();
				Event event = Event.getEvent(p);
				((EventListeners)event).onChat(e);
			}
			@EventHandler
			public void move(PlayerMoveEvent e) {
				Player p = e.getPlayer();
				Event event = Event.getEvent(p);
				((EventListeners)event).onMove(e);
			}
			@EventHandler
			public void click(InventoryClickEvent e) {
				Player p = (Player) e.getWhoClicked();
				Event event = Event.getEvent(p);
				((EventListeners)event).onClickInventory(e);
			}
		};
		Bukkit.getPluginManager().registerEvents(listeners, Core.getInstance());
	}
	
	public static Event getEvent(Player p) {
		return p.hasMetadata("event") ? (Event) p.getMetadata("event").get(0).value() : null;
	}
	
	public void removeListeners() {
		if (listeners==null) return;
		HandlerList.unregisterAll(listeners);
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
