package me.hawkcore.utils.menus;

import java.util.ArrayList;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.clip.placeholderapi.PlaceholderAPI;
import me.hawkcore.Core;
import me.hawkcore.utils.API;
import me.hawkcore.utils.items.Item;
import me.hawkcore.utils.items.SkullCreator;

public class MenuAPI {

	public static Item getItemFromSection(ConfigurationSection section) {
		Item item = new Item(section.contains("ID") ? API.get().getItemStack(section.getString("ID")) : new ItemStack(Material.STONE));
		if (section.contains("name")) item.setDisplayName(section.getString("name").replace("&", "§"));
		if (section.contains("lore")) {
			List<String> lore = new ArrayList<>(section.getStringList("lore"));
			lore.replaceAll(l -> l.replace("{tag}", Core.getInstance().getTag()).replace("&", "§"));
			item.setLore(lore);
		}
		if (section.contains("ID") && section.getString("ID").equalsIgnoreCase("player")) item.setPlayer(true);
		if (section.contains("slot")) item.setSlot(section.getInt("slot")-1);
		if (section.contains("glow") && section.getBoolean("glow")) item.setGlow();
		return item;
	}
	
	@SuppressWarnings("deprecation")
	public static Item getItemInfoPlayer(Player p, Item item) {
		Item it = new Item(item.getPlayer() ? SkullCreator.itemFromName(p.getName()) : item.build());
		it.setDisplayName(item.getDisplayName());
		List<String> lore = new ArrayList<>(item.getLore());
		lore.replaceAll(l -> PlaceholderAPI.setPlaceholders(p, l));
		it.setLore(lore);
		it.setSlot(item.getSlot());
		return it;
	}
	
	public static Item getItemWithReplaces(Item item, List<String> replaces) {
		Item it = new Item(item.build());
		List<String> lore = new ArrayList<>(item.getLore());
		if (!lore.isEmpty()) {
			for (int i = 0; i < lore.size(); i++) {
				for(String line : replaces) {
					String[] args = line.split(":");
					lore.set(i, lore.get(i).replace(args[0], args[1]));
				}
			}
		}
		it.setLore(lore);
		it.setSlot(item.getSlot());
		return it;
	}
	
}
