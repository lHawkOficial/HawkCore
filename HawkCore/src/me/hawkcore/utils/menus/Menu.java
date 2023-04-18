package me.hawkcore.utils.menus;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import lombok.Getter;
import lombok.Setter;
import me.hawkcore.Core;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.Glass;
import me.hawkcore.utils.items.Item;
import me.hawkcore.utils.menus.interfaces.MenuInterface;
import me.hawkcore.utils.menus.listeners.MenuClickEvent;
import me.hawkcore.utils.menus.listeners.MenuCloseEvent;

@Getter
@Setter
public class Menu implements MenuInterface {

	private Menu menu;
	private String title;
	private int row;
	private Glass glass;
	private List<Item> items = new ArrayList<>();
	private Listener listener;
	
	public Menu(String title, int row, Glass glass, List<Item> items) {
		this.title = title;
		this.menu = this;
		this.row = row;
		this.glass = glass;
		this.items = items;
	}
	
	public void register() {
		this.listener = new Listener() {
			@EventHandler
			public void clickMenu(InventoryClickEvent e) {
				Player p = (Player) e.getWhoClicked();
				if (e.getView().getTitle().equals(title)) {
					MenuClickEvent event = new MenuClickEvent(p, e.getInventory(), menu, e);
					Bukkit.getPluginManager().callEvent(event);
					if (event.isCancelled()) e.setCancelled(true);
					if (event.isClickSound() && e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) p.playSound(p.getLocation(), Sound.CLICK, 0.5f, 10);
				}
			}
			@EventHandler
			public void closeMenu(InventoryCloseEvent e) {
				Player p = (Player) e.getPlayer();
				if(e.getView().getTitle().equals(title)) {
					MenuCloseEvent event = new MenuCloseEvent(p, e.getInventory(), menu, e);
					Bukkit.getPluginManager().callEvent(event);
					if (event.isCancelled()) Task.run(()->p.openInventory(e.getInventory()));
				}
			}
		};
		Bukkit.getPluginManager().registerEvents(listener, Core.getInstance());
	}
	
	public void unregister() {
		if (listener!=null)
		HandlerList.unregisterAll(listener);
	}
	
	@Override
	public void open(Player p) {
		MenuInterface.super.open(p);
	}
	
}
