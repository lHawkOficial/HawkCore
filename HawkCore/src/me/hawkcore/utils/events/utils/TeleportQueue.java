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
	private HashMap<Player, Location> players;

	public TeleportQueue(Event event, HashMap<Player, Location> players) {
		this.event = event;
		this.players = players;
	}
	
	@Override
	public void run() {
		if (players.keySet().isEmpty()) return;
		if (slot < players.keySet().size()) {
			Player p = (Player) players.keySet().toArray()[slot];
			Task.run(()->{
				p.teleport(players.get(p));
				p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 0.5f, 10);
			});
			for(Player all : (Player[])players.keySet().toArray()) {
				API.get().sendActionBarMessage(p, event.getMessages().getQueueMessage().replace("{numero}", String.valueOf(slot+1)));
				all.playSound(all.getLocation(), Sound.NOTE_STICKS, 0.5f, 10);
			}
			players.remove(p);
			slot++;
		}else slot = 0;
	}
	
}
