package me.hawkcore.utils.menus.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import lombok.Getter;
import me.hawkcore.utils.listenercreator.Listener;
import me.hawkcore.utils.menus.Menu;

@Getter
public class MenuClickEvent extends Listener {
	
	private Player player;
	private Inventory inventory;
	private Menu menu;
	private InventoryClickEvent eventclick;
	
	public MenuClickEvent(Player player, Inventory inventory, Menu menu, InventoryClickEvent eventclick) {
		this.player = player;
		this.inventory = inventory;
		this.menu = menu;
		this.eventclick = eventclick;
	}
	
}
