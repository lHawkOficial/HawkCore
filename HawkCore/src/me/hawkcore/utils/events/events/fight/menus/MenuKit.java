package me.hawkcore.utils.events.events.fight.menus;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.hawkcore.utils.events.events.fight.Fight;
import me.hawkcore.utils.menus.Menu;
import me.hawkcore.utils.menus.interfaces.MenuExecutor;
import me.hawkcore.utils.menus.interfaces.MenuInterface;
import me.hawkcore.utils.menus.listeners.MenuClickEvent;
import me.hawkcore.utils.menus.listeners.MenuCloseEvent;

public class MenuKit extends Menu implements MenuExecutor, MenuInterface {
	
	private Fight fight = Fight.get();
	
	public MenuKit(String title, int row, List<String> glass) {
		super(title, row, glass);
		register();
	}

	@Override
	public void open(Player p) {
		try {
			
			Inventory inv = Bukkit.createInventory(null, getRow(), getTitle());
			inv.setContents(fight.getContent_kit().clone());
			inv.setItem(9*5-9, fight.getArmor_kit()[3]);
			inv.setItem(9*5-8, fight.getArmor_kit()[2]);
			inv.setItem(9*5-7, fight.getArmor_kit()[1]);
			inv.setItem(9*5-6, fight.getArmor_kit()[0]);
			p.playSound(p.getLocation(), Sound.CHEST_OPEN, 0.5f, 10);
			p.openInventory(inv);
			p.updateInventory();
			
		} catch (Exception e) {
			e.printStackTrace();
			p.closeInventory();
		}
	}

	@Override
	public void click(MenuClickEvent e) {
		e.setClickSound(true);
		e.setCancelled(true);
	}

	@Override
	public void close(MenuCloseEvent e) {
		e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.CHEST_CLOSE, 0.5f, 10);
	}

}
