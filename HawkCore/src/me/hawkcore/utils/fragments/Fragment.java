package me.hawkcore.utils.fragments;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import me.hawkcore.Core;
import me.hawkcore.utils.ConfigGeral;
import me.hawkcore.utils.inventories.InventoryAPI;
import me.hawkcore.utils.itemcreator.ItemCreator;
import me.hawkcore.utils.items.Item;

public class Fragment implements Listener {
	
	public Fragment() {
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOWEST)
	public void breakB(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if (!ConfigGeral.get().getActive_fragments()) return;
		if (ConfigGeral.get().getOnly_survival_fragments() && p.getGameMode() != GameMode.SURVIVAL) return;
		for(String ID : ConfigGeral.get().getBlocks_fragments()) {
			if (!Item.isSimilarMaterial(new Item(e.getBlock().getTypeId(),e.getBlock().getData()).build(), new Item(ID).build())) continue;
			loop: for(String linha : ConfigGeral.get().getFragments()) {
				String[] args = linha.split(":");
				ItemCreator ic = ItemCreator.get().getItem(args[0]);
				if (ic==null) continue loop;
				ItemStack item = ic.getItem().clone();
				double chance = Double.valueOf(args[1]);
				double random;
				do {
					random = new Random().nextDouble();
				} while (random > 1);
				if (random <= chance) {
					if (InventoryAPI.hasSpace(p, item)) {
						p.getInventory().addItem(item);
					}else {
						p.getWorld().dropItem(p.getLocation(), item);
					}
					p.playSound(p.getLocation(), Sound.LEVEL_UP, 0.5f, 10);
					p.sendTitle(ConfigGeral.get().getMessage_fragments().split("<nl>")[0].replace("{fragmento}", ic.getNome()), ConfigGeral.get().getMessage_fragments().split("<nl>")[1].replace("{fragmento}", ic.getNome()));
					break loop;
				}
			}
			break;
		}
	}
	
}
