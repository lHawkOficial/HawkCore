package me.hawkcore.utils.menus.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import lombok.Getter;
import lombok.Setter;
import me.hawkcore.utils.listenercreator.Listener;
import me.hawkcore.utils.menus.Menu;

@Getter
@Setter
public class MenuCloseEvent extends Listener {
	
	private Player player;
	private Inventory inventory;
	private Menu menu;
	private InventoryCloseEvent event;
	
	public MenuCloseEvent(Player player, Inventory inventory, Menu menu, InventoryCloseEvent event) {
		this.player = player;
		this.inventory = inventory;
		this.menu = menu;
		this.event = event;
	}
}
