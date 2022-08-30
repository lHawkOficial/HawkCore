package me.hawkcore.utils.missions.listeners;

import org.bukkit.Bukkit;



import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import me.hawkcore.Core;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.locations.Distance;
import me.hawkcore.utils.missions.objects.Mission;
import me.hawkcore.utils.missions.objects.MissionCategory;
import me.hawkcore.utils.missions.objects.MissionPlayer;
import me.hawkcore.utils.missions.types.MissionBreakBlock;
import me.hawkcore.utils.missions.types.MissionBreakItem;
import me.hawkcore.utils.missions.types.MissionChatEvent;
import me.hawkcore.utils.missions.types.MissionCraftItem;
import me.hawkcore.utils.missions.types.MissionDropItem;
import me.hawkcore.utils.missions.types.MissionEatEvent;
import me.hawkcore.utils.missions.types.MissionEnchantItem;
import me.hawkcore.utils.missions.types.MissionKillEntity;
import me.hawkcore.utils.missions.types.MissionMoveDistance;
import me.hawkcore.utils.missions.types.MissionPickItem;
import me.hawkcore.utils.missions.types.MissionPlaceBlock;

public class MissionsListeners implements Listener {

	public MissionsListeners() {
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void breakB(BlockBreakEvent e) {
		if (e.isCancelled()) return;
		Player p = e.getPlayer();
		MissionPlayer mp = MissionPlayer.check(p);
		for(MissionCategory category : mp.getCategorys()) {
			Mission mission = category.getMissionToComplete();
			if (mission==null) continue;
			if (!(mission.getObjective() instanceof MissionBreakBlock)) continue;
			MissionBreakBlock objective = (MissionBreakBlock) mission.getObjective();
			if (e.getBlock().getTypeId() == objective.getItem().getTypeId() && e.getBlock().getData() == objective.getItem().getData().getData()) {
				objective.setValue(objective.getValue()+1);
				if (objective.isCompleted()) objective.complete();
				continue;
			}
		}
	}
	
	@EventHandler
	public void breakI(PlayerItemBreakEvent e) {
		Player p = e.getPlayer();
		MissionPlayer mp = MissionPlayer.check(p);
		for(MissionCategory category : mp.getCategorys()) {
			Mission mission = category.getMissionToComplete();
			if (mission==null) continue;
			if (!(mission.getObjective() instanceof MissionBreakItem)) continue;
			MissionBreakItem objective = (MissionBreakItem) mission.getObjective();
			objective.setValue(objective.getValue()+1);
			if (objective.isCompleted()) objective.complete();
			continue;
		}
	}
	
	@EventHandler
	public void placeB(BlockPlaceEvent e) {
		if (e.isCancelled()) return;
		Player p = e.getPlayer();
		MissionPlayer mp = MissionPlayer.check(p);
		for(MissionCategory category : mp.getCategorys()) {
			Mission mission = category.getMissionToComplete();
			if (mission==null) continue;
			if (!(mission.getObjective() instanceof MissionPlaceBlock)) continue;
			MissionPlaceBlock objective = (MissionPlaceBlock) mission.getObjective();
			ItemStack item = p.getItemInHand().clone();
			if (item.isSimilar(objective.getItem())) {
				objective.setValue(objective.getValue()+1);
				if (objective.isCompleted()) objective.complete();
				continue;
			}
		}
	}
	
	@EventHandler
	public void moveD(PlayerMoveEvent e) {
		if (e.isCancelled()) return;
		Player p = e.getPlayer();
		Location loc = e.getTo().clone();
		Location loc1 = e.getFrom().clone();
		Task.runAsync(()-> {
			MissionPlayer mp = MissionPlayer.check(p);
			for(MissionCategory category : mp.getCategorys()) {
				Mission mission = category.getMissionToComplete();
				if (mission == null) continue;
				if (!(mission.getObjective() instanceof MissionMoveDistance)) continue;
				MissionMoveDistance objective = (MissionMoveDistance) mission.getObjective();
				Distance distance = new Distance(loc, loc1);
				double value = distance.value();
				if (value == 0) continue;
				objective.setValue(objective.getValue()+value);
				if (objective.isCompleted()) objective.complete();
				continue;
			}
		});
	}
	
	@EventHandler
	public void pickI(PlayerPickupItemEvent e) {
		if (e.isCancelled()) return;
		Player p = e.getPlayer();
		MissionPlayer mp = MissionPlayer.check(p);
		for(MissionCategory category : mp.getCategorys()) {
			Mission mission = category.getMissionToComplete();
			if (mission==null) continue;
			if (!(mission.getObjective() instanceof MissionPickItem)) continue;
			MissionPickItem objective = (MissionPickItem) mission.getObjective();
			ItemStack item = e.getItem().getItemStack().clone();
			if (item.isSimilar(objective.getItem())) {
				objective.setValue(objective.getValue()+item.getAmount());
				if (objective.isCompleted()) objective.complete();
				continue;
			}
		}
	}
	
	@EventHandler
	public void dropI(PlayerDropItemEvent e) {
		if (e.isCancelled()) return;
		Player p = e.getPlayer();
		MissionPlayer mp = MissionPlayer.check(p);
		for(MissionCategory category : mp.getCategorys()) {
			Mission mission = category.getMissionToComplete();
			if (mission==null) continue;
			if (!(mission.getObjective() instanceof MissionDropItem)) continue;
			MissionDropItem objective = (MissionDropItem) mission.getObjective();
			ItemStack item = e.getItemDrop().getItemStack().clone();
			if (item.isSimilar(objective.getItem())) {
				objective.setValue(objective.getValue()+item.getAmount());
				if (objective.isCompleted()) objective.complete();
				continue;
			}
		}
	}
	
	@EventHandler
	public void killE(EntityDeathEvent e) {
		Entity entity = e.getEntity();
		Player p = e.getEntity().getKiller() instanceof Player ? (Player)e.getEntity().getKiller() : null;
		if (p == null && e.getEntity().getKiller() instanceof Projectile) {
			Projectile tile = (Projectile) e.getEntity().getKiller();
			if (tile.getShooter() instanceof Player) p = (Player) tile.getShooter();
		}
		if (p==null) return;
		MissionPlayer mp = MissionPlayer.check(p);
		for(MissionCategory category : mp.getCategorys()) {
			Mission mission = category.getMissionToComplete();
			if (mission==null) continue;
			if (!(mission.getObjective() instanceof MissionKillEntity)) continue;
			MissionKillEntity objective = (MissionKillEntity) mission.getObjective();
			EntityType type = entity.getType();
			if (type == objective.getType()) {
				objective.setValue(objective.getValue()+1);
				if (objective.isCompleted()) objective.complete();
				continue;
			}
		}
	}
	
	@EventHandler
	public void craftI(CraftItemEvent e) {
		if (e.isCancelled()) return;
		Player p = (Player) e.getView().getPlayer();
		MissionPlayer mp = MissionPlayer.check(p);
		for(MissionCategory category : mp.getCategorys()) {
			Mission mission = category.getMissionToComplete();
			if (mission==null) continue;
			if (!(mission.getObjective() instanceof MissionCraftItem)) continue;
			MissionCraftItem objective = (MissionCraftItem) mission.getObjective();
			objective.setValue(objective.getValue()+1);
			if (objective.isCompleted()) objective.complete();
			continue;
		}
	}
	
	@EventHandler
	public void enchantI(EnchantItemEvent e) {
		if (e.isCancelled()) return;
		Player p = e.getEnchanter();
		MissionPlayer mp = MissionPlayer.check(p);
		for(MissionCategory category : mp.getCategorys()) {
			Mission mission = category.getMissionToComplete();
			if (mission==null) continue;
			if (!(mission.getObjective() instanceof MissionEnchantItem)) continue;
			MissionEnchantItem objective = (MissionEnchantItem) mission.getObjective();
			objective.setValue(objective.getValue()+1);
			if (objective.isCompleted()) objective.complete();
			continue;
		}
	}
	
	@EventHandler
	public void chat(ChatMessageEvent e) {
		if (e.isCancelled()) return;
		Player p = e.getSender();
		MissionPlayer mp = MissionPlayer.check(p);
		for(MissionCategory category : mp.getCategorys()) {
			Mission mission = category.getMissionToComplete();
			if (mission==null) continue;
			if (!(mission.getObjective() instanceof MissionChatEvent)) continue;
			MissionChatEvent objective = (MissionChatEvent) mission.getObjective();
			objective.setValue(objective.getValue()+1);
			if (objective.isCompleted()) objective.complete();
			continue;
		}
	}
	
	@EventHandler
	public void eatL(FoodLevelChangeEvent e) {
		if (e.isCancelled()) return;
		Player p = (Player) e.getEntity();
		if (e.getFoodLevel() - p.getFoodLevel() <= 0) return;
		MissionPlayer mp = MissionPlayer.check(p);
		for(MissionCategory category : mp.getCategorys()) {
			Mission mission = category.getMissionToComplete();
			if (mission==null) continue;
			if (!(mission.getObjective() instanceof MissionEatEvent)) continue;
			MissionEatEvent objective = (MissionEatEvent) mission.getObjective();
			objective.setValue(objective.getValue()+1);
			if (objective.isCompleted()) objective.complete();
			continue;
		}
	}
	
}
