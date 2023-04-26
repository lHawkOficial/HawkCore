package me.hawkcore.utils.menus.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import me.hawkcore.utils.listenercreator.Listener;
import me.hawkcore.utils.menus.Menu;

@Getter
public class MenuClickEvent extends Listener {
	
	private Player player;
	private Inventory inventory;
	private Menu menu;
	private InventoryClickEvent eventclick;
	private ItemStack currentItem;
	
	public MenuClickEvent(Player player, Inventory inventory, Menu menu, InventoryClickEvent eventclick) {
		this.player = player;
		this.inventory = inventory;
		this.menu = menu;
		this.eventclick = eventclick;
		this.currentItem = eventclick.getCurrentItem();
	}
	
}
