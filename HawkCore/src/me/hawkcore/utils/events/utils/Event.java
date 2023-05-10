package me.hawkcore.utils.events.utils;

import java.io.File;
import java.util.ArrayList;


import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
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
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import lombok.Getter;
import lombok.Setter;
import me.HTags.ListenersPlugin.PlayerUpdateTagEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import me.hawkcore.Core;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.API;
import me.hawkcore.utils.Eco;
import me.hawkcore.utils.Scoreboard;
import me.hawkcore.utils.events.EventManager;
import me.hawkcore.utils.events.utils.enums.EventStatus;
import me.hawkcore.utils.events.utils.enums.EventType;
import me.hawkcore.utils.events.utils.enums.PlayerType;
import me.hawkcore.utils.events.utils.interfaces.EventListeners;
import me.hawkcore.utils.items.Item;

@Getter
@Setter
public class Event {
	
	private String name;
	private boolean enabled,warn=false;
	private EventType eventType = EventType.PVP;
	private EventStatus eventStatus = EventStatus.STOPPED;
	private MessagesEvent messages;
	private FileConfiguration config;
	private String tag = "§7[Evento]";
	private Item icon;
	private ConfigEvent configEvent;
	private RankingEvent ranking;
	private Location locationLobby, locationExit, locationStart;
	private HashMap<Player, PlayerType> players = new HashMap<>();
	private HashMap<Player, Location> queueList = new HashMap<>();
	private List<Listener> listeners = new ArrayList<>();
	private Task taskQueue;
	public Event event;
	private TeleportQueue teleportQueue;
	private File folder, fileSaves;
	private long lastStart = System.currentTimeMillis();
	
	public Event(String name, File folder, FileConfiguration config, EventType type, boolean enabled) {
		EventManager.get().getEvents().add(this);
		this.event = this;
		this.name = name;
		this.config = config;
		this.eventType = type;
		this.folder = folder;
		this.enabled = enabled;
		if (!enabled) return;
		this.ranking = new RankingEvent(this);
		this.configEvent = new ConfigEvent(this);
		this.messages = new MessagesEvent(this);
		this.taskQueue = new Task();
		this.teleportQueue = new TeleportQueue(taskQueue, this, queueList);
		this.taskQueue.setRunnable(teleportQueue);
	}
	
	public void setupListeners() {
		listeners.add(new Listener() {
			@EventHandler(priority = EventPriority.LOWEST)
			public void pick(PlayerPickupItemEvent e) {
				Player p = e.getPlayer();
				Event event = Event.getEvent(p);
				if (event != null && event.equals(getEvent()))
				((EventListeners)event).pickItemEvent(e);
			}
			@EventHandler(priority = EventPriority.LOWEST)
			public void damage(EntityDamageEvent e) {
				if (!(e.getEntity() instanceof Player)) return;
				Player p = (Player) e.getEntity();
				Event event = Event.getEvent(p);
				if (event != null && event.equals(getEvent()))
				((EventListeners)event).damage(e);
			}
			@EventHandler(priority = EventPriority.LOWEST)
			public void updateTag(PlayerUpdateTagEvent e) {
				Player p = e.getJogador().getPlayer();
				Event event = Event.getEvent(p);
				if (event != null && event.equals(getEvent()))
				((EventListeners)event).tagUpdate(e);
			}
			@EventHandler(priority = EventPriority.LOWEST)
			public void dropItem(PlayerDropItemEvent e) {
				Player p = e.getPlayer();
				Event event = Event.getEvent(p);
				if (event != null && event.equals(getEvent()))
				((EventListeners)event).playerDropItem(e);
			}
			@EventHandler(priority = EventPriority.LOWEST)
			public void teleport(PlayerTeleportEvent e) {
				Player p = e.getPlayer();
				Event event = Event.getEvent(p);
				if (event != null && event.equals(getEvent()))
				((EventListeners)event).onTeleport(e);
			}
			@EventHandler(priority = EventPriority.LOWEST)
			public void chat(ChatMessageEvent e) {
				((EventListeners)event).onChat(e);
			}
			@EventHandler(priority = EventPriority.LOWEST)
			public void chatVanilla(AsyncPlayerChatEvent e) {
				Player p = e.getPlayer();
				Event event = Event.getEvent(p);
				if (event != null && event.equals(getEvent()))
				((EventListeners)event).onChatVanilla(e);
			}
			@EventHandler(priority = EventPriority.LOWEST)
			public void move(PlayerMoveEvent e) {
				Player p = e.getPlayer();
				Event event = Event.getEvent(p);
				if (event != null && event.equals(getEvent()))
				((EventListeners)event).onMove(e);
			}
			@EventHandler(priority = EventPriority.LOWEST)
			public void click(InventoryClickEvent e) {
				Player p = (Player) e.getWhoClicked();
				Event event = Event.getEvent(p);
				if (event != null && event.equals(getEvent()))
				((EventListeners)event).onClickInventory(e);
			}
			@EventHandler(priority = EventPriority.LOWEST)
			public void quit(PlayerQuitEvent e) {
				Player p = e.getPlayer();
				Event event = Event.getEvent(p);
				if (event != null && event.equals(getEvent()))
				((EventListeners)event).onQuit(e);
			}
			@EventHandler(priority = EventPriority.LOWEST)
			public void onCommands(PlayerCommandPreprocessEvent e) {
				Player p = e.getPlayer();
				Event event = Event.getEvent(p);
				if (event != null && event.equals(getEvent()))
				((EventListeners)event).onCommands(e);
			}
			@EventHandler(priority = EventPriority.LOWEST)
			public void damageEntity(EntityDamageByEntityEvent e) {
				Player p = null;
				Entity entity = e.getDamager();
				if (entity instanceof Player) p = (Player) entity;
				else {
					if (entity instanceof Projectile && ((Projectile) entity).getShooter() instanceof Player) p = (Player)((Projectile)entity).getShooter();
				}
				if (p == null) return;
				Event event = Event.getEvent(p);
				if (event != null && event.equals(getEvent()))
				((EventListeners)event).onDamageEntity(e);
			}
			@EventHandler(priority = EventPriority.LOWEST)
			public void interact(PlayerInteractEvent e) {
				Player p = e.getPlayer();
				Event event = Event.getEvent(p);
				if (event != null && event.equals(getEvent()))
				((EventListeners)event).onInteract(e);
			}
			@EventHandler(priority = EventPriority.LOWEST)
			public void breakBlock(BlockBreakEvent e) {
				Player p = e.getPlayer();
				Event event = Event.getEvent(p);
				if (event != null && event.equals(getEvent()))
				((EventListeners)event).onBreakBlock(e);
			}
			@EventHandler(priority = EventPriority.LOWEST)
			public void placeBlock(BlockPlaceEvent e) {
				Player p = e.getPlayer();
				Event event = Event.getEvent(p);
				if (event != null && event.equals(getEvent()))
				((EventListeners)event).onPlaceBlock(e);
			}
			@EventHandler(priority = EventPriority.LOWEST)
			public void death(PlayerDeathEvent e) {
				Player p = e.getEntity();
				Event event = Event.getEvent(p);
				if (event != null && event.equals(getEvent()))
				((EventListeners)event).onDeath(e);
			}
			@EventHandler(priority = EventPriority.LOWEST)
			public void hunger(FoodLevelChangeEvent e) {
				Player p = (Player) e.getEntity();
				Event event = Event.getEvent(p);
				if (event != null && event.equals(getEvent()))
				((EventListeners)event).onHunger(e);
			}
			@EventHandler(priority = EventPriority.LOWEST)
			public void join(PlayerJoinEvent e) {
				Player p = (Player) e.getPlayer();
				Event event = Event.getEvent(p);
				if (event != null && event.equals(getEvent()))
				((EventListeners)event).onJoin(e);
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
	
	public String getTimeLastFormatted() {
		return !configEvent.isAutoStart() ? "Aguardando" : eventStatus != EventStatus.STOPPED ? "Em Jogo" : getTimeLast() <= 0 ? "§3Iniciando..." : API.get().formatTime(getTimeLast());
	}
	
	public int getTimeLast() {
		return (int) (configEvent.getAutoStartTime()*60-TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()-lastStart));
	}
	
	public void removeListeners() {
		if (listeners.isEmpty()) return;
		listeners.forEach(listener -> HandlerList.unregisterAll(listener));
	}
	
	public static void clearDatas() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.removeMetadata("event", Core.getInstance());
			p.removeMetadata("scoreevent", Core.getInstance());
		}
	}
	
	public void runRewardToPlayer(Player p, List<String> rewards) {
		for(String line : rewards) {
			if (line.startsWith("money:")) {
				Double valor = Double.valueOf(line.replaceFirst("money:", new String()));
				Eco.get().depositPlayer(p, valor);
				continue;
			}
			if (line.startsWith("command:")) {
				String command = line.replaceFirst("command:", new String()).replaceFirst("/", new String());
				Task.run(()->Bukkit.dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(p, command)));
				continue;
			}
		}
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
	
	public List<Scoreboard> getScoreBoardPlayers() {
		List<Scoreboard> list = new ArrayList<>();
		for(Player p : Bukkit.getOnlinePlayers()) {
			if (!p.hasMetadata("scoreevent")) continue;
			try {
				list.add((Scoreboard) p.getMetadata("scoreevent").get(0).value());
			} catch (Exception e) {
				continue;
			}
		}
		return list;
	}
	
	public Scoreboard getScoreBoardPlayer(String name) {
		for(Player p : Bukkit.getOnlinePlayers()) {
			if (!p.hasMetadata("scoreevent")) continue;
			if (!p.getName().equalsIgnoreCase(name)) continue;
			return (Scoreboard) p.getMetadata("scoreevent").get(0).value();
		}
		return null;
	}
	
	public void removeScoreBoard(Player p) {
		Scoreboard board = getScoreBoardPlayer(name);
		if (board==null) return;
		board.destroy();
		p.removeMetadata("scoreevent", Core.getInstance());
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
