package me.hawkcore.entities.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import me.hawkcore.Core;

public class Listeners implements Listener {

	public Listeners() {
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void damage(EntityDamageEvent e) {
		if (e.getEntity().hasMetadata("entitycreator")) e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void damage(PlayerInteractAtEntityEvent e) {
		if (e.getRightClicked().hasMetadata("entitycreator")) e.setCancelled(true);
	}
	
}
