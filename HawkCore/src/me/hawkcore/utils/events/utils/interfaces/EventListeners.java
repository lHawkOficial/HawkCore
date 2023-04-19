package me.hawkcore.utils.events.utils.interfaces;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public interface EventListeners {

	void onChat(AsyncPlayerChatEvent e);
	void onMove(PlayerMoveEvent e);
	void onClickInventory(InventoryClickEvent e);
	void onQuit(PlayerQuitEvent e);
	void onCommands(PlayerCommandPreprocessEvent e);
	void onDamageEntity(EntityDamageByEntityEvent e);
	void onInteract(PlayerInteractEvent e);
	void onBreakBlock(BlockBreakEvent e);
	void onPlaceBlock(BlockPlaceEvent e);
	void onDeath(PlayerDeathEvent e);
	void onHunger(FoodLevelChangeEvent e);
	
}
