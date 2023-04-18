package me.hawkcore.utils.showonline.events;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.hawkcore.Core;
import me.hawkcore.utils.configs.ConfigShowMessages;

public class EventsShow implements Listener {

	public EventsShow() {
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
	}
	
	@EventHandler
	public void join(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		ConfigShowMessages config = ConfigShowMessages.get();
		if (config.getJoin_active() && !p.hasPermission("hawkcore.adm")) {
			e.setJoinMessage(new String());
			for(Player all : Bukkit.getOnlinePlayers()) {
				all.sendMessage(config.getJoin_message().replace("{player}", p.getName()));
				all.playSound(all.getLocation(), Sound.NOTE_STICKS, 0.5f, 10);
			}
		}
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		ConfigShowMessages config = ConfigShowMessages.get();
		if (config.getQuit_active() && !p.hasPermission("hawkcore.adm")) {
			e.setQuitMessage(new String());
			for(Player all : Bukkit.getOnlinePlayers()) {
				all.sendMessage(config.getQuit_message().replace("{player}", p.getName()));
				all.playSound(all.getLocation(), Sound.NOTE_STICKS, 0.5f, 10);
			}
		}
	}
	
}
