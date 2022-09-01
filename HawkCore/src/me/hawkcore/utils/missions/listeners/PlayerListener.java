package me.hawkcore.utils.missions.listeners;


import org.bukkit.Bukkit;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import me.hawkcore.Core;
import me.hawkcore.utils.API;
import me.hawkcore.utils.items.Item;
import me.hawkcore.utils.missions.ManagerMissions;
import me.hawkcore.utils.missions.listeners.plugin.PlayerPlantEvent;
import me.hawkcore.utils.missions.objects.MissionPlayer;

public class PlayerListener implements Listener {

	public PlayerListener() {
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
	}
	
	@EventHandler
	public void join(PlayerJoinEvent e) {
		MissionPlayer.check(e.getPlayer());
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent e) {
		MissionPlayer mp = MissionPlayer.check(e.getPlayer());
		mp.saveAsync();
		e.getPlayer().removeMetadata("missionplayer", Core.getInstance());
		ManagerMissions.get().getPlayers().remove(mp);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void plant(BlockPlaceEvent e) {
		if (e.isCancelled()) return;
		Player p = e.getPlayer();
		p.sendMessage(e.getBlock().getType().toString());
		API api = API.get();
		String[] ids = new String[]{
				"362>0", "361>0", 
				"295>0", "391>0", 
				"392>0", "351>3", 
				"81>0", "338>0",
				"6>0", "6>1",
				"6>2", "6>3",
				"6>4", "6>5",
				"372>0"};
		ItemStack item = p.getItemInHand().clone();
		for(String ID : ids) {
			if (!Item.isSimilar(api.getItemStack(ID), item)) continue;
			PlayerPlantEvent event = new PlayerPlantEvent(p, p.getItemInHand().clone(), e.getBlock());
			Bukkit.getPluginManager().callEvent(event);
			if (event.isCancelled()) e.setCancelled(true);
			return;
		}
	}
	
}
