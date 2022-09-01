package me.hawkcore.utils.playersdata.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.hawkcore.Core;
import me.hawkcore.utils.playersdata.managers.ManagerData;
import me.hawkcore.utils.playersdata.objects.PlayerData;

public class PlayerDataListener implements Listener {

	public PlayerDataListener() {
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
	}
	
	@EventHandler
	public void join(PlayerJoinEvent e) {
		PlayerData.check(e.getPlayer());
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		PlayerData pd = PlayerData.check(p);
		p.removeMetadata("playerdata", Core.getInstance());
		ManagerData.get().getPlayers().remove(pd);
	}
	
}
