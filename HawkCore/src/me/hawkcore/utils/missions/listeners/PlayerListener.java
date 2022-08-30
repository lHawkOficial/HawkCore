package me.hawkcore.utils.missions.listeners;

import org.bukkit.Bukkit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.hawkcore.Core;
import me.hawkcore.utils.missions.objects.MissionPlayer;

public class PlayerListener implements Listener {

	public PlayerListener() {
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
	}
	
	@EventHandler
	public void join(PlayerJoinEvent e) {
		MissionPlayer.check(e.getPlayer());
	}
	
	public void quit(PlayerQuitEvent e) {
		MissionPlayer.check(e.getPlayer());
		e.getPlayer().removeMetadata("missionplayer", Core.getInstance());
	}
	
}
