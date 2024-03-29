package me.hawkcore.utils.events.utils.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.hawkcore.Core;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.events.utils.Event;

public class PlayerListener implements Listener {

	public PlayerListener() {
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		Event event = Event.getEvent(p);
		if (event!=null) event.removeScoreBoard(p);
		Task.run(()->{
			p.removeMetadata("event", Core.getInstance());
		});
	}
	
}
