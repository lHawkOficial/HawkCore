package me.hawkcore.utils.missions.listeners.plugin;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;


import org.bukkit.event.Cancellable;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;

@Getter
public class PlayerPlantEvent extends Event implements Cancellable  {

	private static final HandlerList HANDLERS_LIST = new HandlerList();
	
	private Block block;
	private ItemStack itemPlanted;
	private Player player;
	private boolean cancel = false;
	
	public PlayerPlantEvent(Player player, ItemStack itemPlanted, Block block) {
		this.player = player;
		this.itemPlanted = itemPlanted;
		this.block = block;
	}
	
	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean b) {
		cancel = b;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}

	
	
}
