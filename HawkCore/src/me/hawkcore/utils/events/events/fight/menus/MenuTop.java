package me.hawkcore.utils.events.events.fight.menus;

import java.util.ArrayList;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import lombok.Getter;
import lombok.Setter;
import me.hawkcore.Core;
import me.hawkcore.utils.events.events.fight.Fight;
import me.hawkcore.utils.events.utils.RankingEvent;
import me.hawkcore.utils.items.Item;
import me.hawkcore.utils.items.SkullCreator;
import me.hawkcore.utils.menus.Menu;
import me.hawkcore.utils.menus.MenuAPI;
import me.hawkcore.utils.menus.interfaces.MenuExecutor;
import me.hawkcore.utils.menus.interfaces.MenuInterface;
import me.hawkcore.utils.menus.listeners.MenuClickEvent;
import me.hawkcore.utils.menus.listeners.MenuCloseEvent;

@Getter
@Setter
public class MenuTop extends Menu implements MenuExecutor, MenuInterface {

	private Item iconBack,
	iconPlayers,
	iconNonPlayer;
	private List<Integer> slots;
	
	public MenuTop(String title, int row, List<String> glass) {
		super(title, row, glass);
		iconBack = MenuAPI.getItemFromSection(Fight.get().getConfig().getConfigurationSection("MenuTop.Icons.iconBack"));
		iconPlayers = MenuAPI.getItemFromSection(Fight.get().getConfig().getConfigurationSection("MenuTop.Icons.iconPlayers"));
		iconNonPlayer = MenuAPI.getItemFromSection(Fight.get().getConfig().getConfigurationSection("MenuTop.Icons.iconNonPlayer"));
		this.slots = new ArrayList<>();
		for(int slot : Fight.get().getConfig().getIntegerList("MenuTop.slots")) {
			this.slots.add(slot-1);
		}
		register();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void open(Player p) {
		try {
			
			Inventory inv = Bukkit.createInventory(null, 9*getRow(), getTitle());
			for(Item item : getGlass().getVidros()) {
				inv.setItem(item.getSlot(), item.build().clone());
			}
			inv.setItem(iconBack.getSlot(), iconBack.build().clone());
			
			RankingEvent ranking = Fight.get().getRanking();
			for (int i = 0; i < slots.size(); i++) {
				int slot = slots.get(i);
				try {
					String name = (String) ranking.getTops().keySet().toArray()[i];
					int rank = ranking.getRankingPlayer(name);
					int total = ranking.getWinsPlayer(name);
					Item item = new Item(SkullCreator.itemFromName(name));
					item.setDisplayName(iconPlayers.getDisplayName().replace("{player}", name));
					List<String> lore = new ArrayList<>(iconPlayers.getLore());
					lore.replaceAll(l -> l.replace("{rank}", String.valueOf(rank)).replace("{total}", String.valueOf(total)));
					item.setLore(lore);
					inv.setItem(slot, item.build().clone());
				} catch (Exception e) {
					inv.setItem(slot, iconNonPlayer.build().clone());
				}
			}
			
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
				MenuFight.get().open(p);
			}
		}
	}

	@Override
	public void close(MenuCloseEvent e) {
		
	}

}
