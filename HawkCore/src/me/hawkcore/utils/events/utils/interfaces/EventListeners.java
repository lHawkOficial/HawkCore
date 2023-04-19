package me.hawkcore.utils.events.utils.interfaces;

import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.sk89q.worldguard.bukkit.event.block.BreakBlockEvent;

public interface EventListeners {

	void onChat(AsyncPlayerChatEvent e);
	void onMove(PlayerMoveEvent e);
	void onClickInventory(InventoryClickEvent e);
	void onQuit(PlayerQuitEvent e);
	void onCommands(PlayerCommandPreprocessEvent e);
	void onDamageEntity(EntityDamageByEntityEvent e);
	void onInteract(PlayerInteractEvent e);
	void onBreakBlock(BreakBlockEvent e);
	void onPlaceBlock(BlockPlaceEvent e);
	void onDeath(PlayerDeathEvent e);
	
}
