package me.hawkcore.utils.playersdata.listeners;


import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.hawkcore.Core;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.boosbar.BossBar;
import me.hawkcore.utils.items.Item;
import me.hawkcore.utils.locations.Distance;
import me.hawkcore.utils.playersdata.managers.ManagerData;
import me.hawkcore.utils.playersdata.objects.PlayerData;
import me.hawkcore.utils.playersdata.objects.Thirst;
import me.hawkcore.utils.playersdata.utils.MensagensThirstHeat;

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
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void respawn(PlayerDeathEvent e) {
		Player p = e.getEntity();
		PlayerData pd = PlayerData.check(p);
		Task.run(()-> {
			p.spigot().respawn();
			pd.getThirst().reset();
			pd.getHeat().reset();
		});
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void useWater(PlayerItemConsumeEvent e) {
		Player p = e.getPlayer();
		PlayerData pd = PlayerData.check(p);
		Thirst thirst = pd.getThirst();
		if (thirst.getItem() == null) return;
		if (!Item.isSimilar(thirst.getItem(), e.getItem())) return;
		int percent = (int) (thirst.getValue() * 100 / thirst.getMaxValue());
		if (!(percent < (100-thirst.getItemPercentIncrease()))) {
			e.setCancelled(true);
			BossBar.send(p, MensagensThirstHeat.get().getWaterFull(), 3);
			p.updateInventory();
			p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 0.5f);
			return;
		}
		double value = thirst.getMaxValue() * thirst.getItemPercentIncrease() / 100;
		thirst.add(value);
		for(Player all : Bukkit.getOnlinePlayers()) {
			if (!all.getWorld().equals(p.getWorld())) continue;
			Distance distance = new Distance(p.getLocation(), all.getLocation());
			double dvalue = distance.value();
			if (dvalue > 10) continue;
			all.playSound(p.getLocation(), Sound.DRINK, 1, 5);
		}
		p.updateInventory();
	}
	
}
