package me.hawkcore.utils.events.events.bolao.menus;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import lombok.Getter;
import lombok.Setter;
import me.hawkcore.Core;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.events.events.bolao.Bolao;
import me.hawkcore.utils.events.utils.MenuEvents;
import me.hawkcore.utils.items.Item;
import me.hawkcore.utils.menus.Menu;
import me.hawkcore.utils.menus.MenuAPI;
import me.hawkcore.utils.menus.interfaces.MenuExecutor;
import me.hawkcore.utils.menus.interfaces.MenuInterface;
import me.hawkcore.utils.menus.listeners.MenuClickEvent;
import me.hawkcore.utils.menus.listeners.MenuCloseEvent;

@Getter
@Setter
public class MenuBolao extends Menu implements MenuExecutor, MenuInterface {
	
	private MenuTop menutop;
	private Item iconBack,
	iconInfoPlayer,
	iconInfoEvent,
	iconTop;
	
	public MenuBolao(String title, int row, List<String> glass) {
		super(title, row, glass);
		this.menutop = new MenuTop(Bolao.get().getConfig().getString("MenuTop.title").replace("&", "§"), Bolao.get().getConfig().getInt("MenuTop.row"), Bolao.get().getConfig().getStringList("MenuTop.Glass"));
		this.iconBack = MenuAPI.getItemFromSection(Bolao.get().getConfig().getConfigurationSection("MenuMain.Icons.iconBack"));
		this.iconInfoPlayer = MenuAPI.getItemFromSection(Bolao.get().getConfig().getConfigurationSection("MenuMain.Icons.iconInfoPlayer"));
		this.iconInfoEvent = MenuAPI.getItemFromSection(Bolao.get().getConfig().getConfigurationSection("MenuMain.Icons.iconInfoEvent"));
		this.iconTop = MenuAPI.getItemFromSection(Bolao.get().getConfig().getConfigurationSection("MenuMain.Icons.iconTop"));
		register();
	}

	@Override
	public void open(Player p) {
		try {
			
			Inventory inv = Bukkit.createInventory(null, 9*getRow(), getTitle());
			for(Item item : getGlass().getVidros()) {
				inv.setItem(item.getSlot(), item.build().clone());
			}
			
			inv.setItem(iconBack.getSlot(), iconBack.build().clone());
			List<String> replaces = new ArrayList<>();
			replaces.add("{rank}:"+Bolao.get().getRanking().getRankingPlayer(p.getName()));
			replaces.add("{total}:"+Bolao.get().getRanking().getWinsPlayer(p.getName()));
			inv.setItem(iconInfoPlayer.getSlot(), MenuAPI.getItemWithReplaces(MenuAPI.getItemInfoPlayer(p, iconInfoPlayer), replaces).build());
			
			Task task = new Task();
			task.setRunnable(()->{
				if (!p.isOnline() || p.getOpenInventory().getTopInventory() == null || !p.getOpenInventory().getTopInventory().equals(inv)) {
					task.cancel();
					return;
				}
				replaces.clear();
				replaces.add("{tempo}:"+Bolao.get().getTimeLastFormatted());
				inv.setItem(iconInfoEvent.getSlot(), MenuAPI.getItemWithReplaces(new Item(iconInfoEvent.build().clone()), replaces).build());
				if (!Bolao.get().getConfigEvent().isAutoStart()) task.cancel();
				p.updateInventory();
			}).setTickRate(1);
			inv.setItem(iconTop.getSlot(), iconTop.build().clone());
			
			p.openInventory(inv);
			p.updateInventory();
			p.playSound(p.getLocation(), Sound.NOTE_STICKS, 0.5f, 10);
			
		} catch (Exception e) {
			e.printStackTrace();
			p.closeInventory();
			p.sendMessage(Core.getInstance().getTag() + " §cNão foi possível abrir este menu, contate um administrador!");
		}
	}
	
	@Override
	public void click(MenuClickEvent e) {
		Player p = e.getPlayer();
		e.setClickSound(true);
		e.setCancelled(true);
		if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
			if (e.getCurrentItem().isSimilar(iconBack.build())) {
				MenuEvents.get().open(p);
				return;
			}
			if (e.getCurrentItem().isSimilar(iconTop.build())) {
				menutop.open(p);
				return;
			}
		}
	}
	
	@Override public void close(MenuCloseEvent e) {}
	
	public static MenuBolao get() {
		return Bolao.get().getMenu();
	}
}
