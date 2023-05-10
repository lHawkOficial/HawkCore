package me.hawkcore.utils.events.utils;


import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.API;

@Getter
@Setter
public class TeleportQueue implements Runnable {
	
	private int slot = 0;
	private Event event;
	private Task task;
	private HashMap<Player, Location> players;
	private boolean teleporting = false;

	public TeleportQueue(Task task, Event event, HashMap<Player, Location> players) {
		this.task = task;
		this.event = event;
		this.players = players;
		this.task.setTickRate(this.event.getConfigEvent().getTickQueue());
	}
	
	@Override
	public void run() {
		this.task.setTickRate(this.event.getConfigEvent().getTickQueue());
		if (players.keySet().isEmpty()) {
			teleporting = false;
			return;
		}
		if (slot < players.keySet().size()) {
			Player p = (Player) players.keySet().toArray()[slot];
			Location loc = players.get(p).clone();
			Task.run(()->{
				p.teleport(loc);
				p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 0.5f, 10);
			});
			for(Player all : players.keySet()) {
				API.get().sendActionBarMessage(p, event.getMessages().getQueueMessage().replace("{numero}", String.valueOf(slot+1)));
				all.playSound(all.getLocation(), Sound.NOTE_STICKS, 0.5f, 10);
			}
			players.remove(p);
			slot++;
			teleporting = true;
		}else slot = 0;
	}
	
}
