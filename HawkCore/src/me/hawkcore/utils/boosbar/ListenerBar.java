package me.hawkcore.utils.boosbar;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import me.hawkcore.Core;

public class ListenerBar implements Listener {

	public ListenerBar() {
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
	}
	
	@EventHandler
	public void teleport(PlayerTeleportEvent e) {
		Player p = e.getPlayer();
		if (p.hasMetadata("barTitle")) {
			p.removeMetadata("barTitle", Core.getInstance());
		}
	}
	
}
