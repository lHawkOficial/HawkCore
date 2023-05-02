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
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import me.hawkcore.utils.events.utils.listeners.ChangeTopEvent;

public interface EventListeners {

	void onChatVanilla(AsyncPlayerChatEvent e);
	void onChat(ChatMessageEvent e);
	void onMove(PlayerMoveEvent e);
	void onClickInventory(InventoryClickEvent e);
	void onQuit(PlayerQuitEvent e);
	void onJoin(PlayerJoinEvent e);
	void onCommands(PlayerCommandPreprocessEvent e);
	void onDamageEntity(EntityDamageByEntityEvent e);
	void onInteract(PlayerInteractEvent e);
	void onBreakBlock(BlockBreakEvent e);
	void onPlaceBlock(BlockPlaceEvent e);
	void onDeath(PlayerDeathEvent e);
	void onHunger(FoodLevelChangeEvent e);
	void onRankingUpdate(ChangeTopEvent e);
	void onTeleport(PlayerTeleportEvent e);
	
}
