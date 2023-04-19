package me.hawkcore.utils.events.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.events.utils.enums.EventStatus;
import me.hawkcore.utils.events.utils.enums.EventType;
import me.hawkcore.utils.events.utils.enums.PlayerType;

@Getter
@Setter
public class Event {
	
	private String name;
	private EventType eventType = EventType.PVP;
	private EventStatus eventStatus = EventStatus.STOPPED;
	private MessagesEvent messages;
	private ConfigEvent configEvent;
	private Location locationLobby, locationExit, locationStart;
	private HashMap<Player, PlayerType> players = new HashMap<>();
	private HashMap<Player, Location> queueList = new HashMap<>();
	private Task taskQueue;
	
	public Event() {
		this.configEvent = new ConfigEvent(this);
		this.messages = new MessagesEvent(this);
		this.taskQueue = new Task(new TeleportQueue(this, queueList));
		this.taskQueue.setTickRate(configEvent.getTickQueue());
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
