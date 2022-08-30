package me.hawkcore.utils.missions.menus;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;

import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import me.hawkcore.Core;
import me.hawkcore.utils.API;
import me.hawkcore.utils.Glass;
import me.hawkcore.utils.PaginaCreator;
import me.hawkcore.utils.items.Item;
import me.hawkcore.utils.missions.objects.ConfigMission;
import me.hawkcore.utils.missions.objects.MissionCategory;
import me.hawkcore.utils.missions.objects.MissionPlayer;

@Getter
public class menuCategorys {

	private String name, concluido;
	private List<Integer> slots = new ArrayList<>();
	private Glass glass;
	private int size;
	private Item iconNext,
	iconBack,
	iconCategory;
	
	public menuCategorys() {
		ConfigurationSection section = Core.getInstance().getConfig().getConfigurationSection("Missions.Menus.menuCategorys");
		for(String linha : section.getStringList("slots")) {
			try {
				slots.add(Integer.valueOf(linha)-1);
			} catch (Exception e) {
				continue;
			}
		}
		concluido = section.getString("concluido").replace("&", "§");
		size = section.getInt("size");
		name = section.getString("name").replace("&", "§");
		glass = new Glass(new ArrayList<>(section.getStringList("glass")), size);
		
		section = Core.getInstance().getConfig().getConfigurationSection("Missions.Menus.menuCategorys.Icons.iconNext");
		Item item = new Item(API.get().getItemStack(section.getString("ID")));
		item.setDisplayName(section.getString("name").replace("&", "§"));
		List<String> lore = new ArrayList<>(section.getStringList("lore"));
		lore.replaceAll(l -> l.replace("&", "§"));
		item.setLore(lore);
		item.setSlot(section.getInt("slot")-1);
		if (section.getBoolean("glow")) item.setGlow();
		iconNext = item;
		
		section = Core.getInstance().getConfig().getConfigurationSection("Missions.Menus.menuCategorys.Icons.iconBack");
		item = new Item(API.get().getItemStack(section.getString("ID")));
		item.setDisplayName(section.getString("name").replace("&", "§"));
		lore = new ArrayList<>(section.getStringList("lore"));
		lore.replaceAll(l -> l.replace("&", "§"));
		item.setLore(lore);
		item.setSlot(section.getInt("slot")-1);
		if (section.getBoolean("glow")) item.setGlow();
		iconBack = item;
		
		section = Core.getInstance().getConfig().getConfigurationSection("Missions.Menus.menuCategorys.Icons.iconCategory");
		item = new Item(API.get().getItemStack(section.getString("ID")));
		item.setDisplayName(section.getString("name").replace("&", "§"));
		lore = new ArrayList<>(section.getStringList("lore"));
		lore.replaceAll(l -> l.replace("&", "§"));
		item.setLore(lore);
		if (section.getBoolean("glow")) item.setGlow();
		iconCategory = item;
	}
	
	public static menuCategorys get() {
		return ConfigMission.get().getMenucategorys();
	}
	
	public void open(Player p) {
		try {
			
			MissionPlayer mp = MissionPlayer.check(p);
			if (!p.hasMetadata("pMissions")) p.setMetadata("pMissions", new FixedMetadataValue(Core.getInstance(), new PaginaCreator(new ArrayList<>(mp.getCategorys()), slots.size())));
			PaginaCreator pagina = (PaginaCreator) p.getMetadata("pMissions").get(0).value();
			Inventory inv = Bukkit.createInventory(null, 9*size, name.replace("{pagina}", String.valueOf(pagina.getPaginaAtual())).replace("{total}", String.valueOf(pagina.getTotalPaginas())));
			
			for(Item item : glass.getVidros()) {
				inv.setItem(item.getSlot(), item.build().clone());
			}
			
			MissionCategory selected = mp.getCategorySelected();
			List<Object> lista = pagina.getPagina(pagina.getPaginaAtual());
			for (int i = 0; i < slots.size(); i++) {
				int slot = slots.get(i);
				try {
					MissionCategory category = (MissionCategory) lista.get(i);
					mp.setCategorySelected(category);
					Item item = new Item(category.getIcon().build().clone());
					List<String> lore = new ArrayList<>(item.getLore());
					lore.replaceAll(l -> PlaceholderAPI.setPlaceholders(p, l).replace("&", "§"));
					if (category.isCompleted()) lore.add(menuCategorys.get().getConcluido());
					item.setLore(lore);
					inv.setItem(slot, item.build());
				} catch (Exception e) {
					inv.setItem(slot, iconCategory.build().clone());
				}
			}
			mp.setCategorySelected(selected);
			
			inv.setItem(iconNext.getSlot(), iconNext.getItem().clone());
			inv.setItem(iconBack.getSlot(), iconBack.getItem().clone());
			
			p.openInventory(inv);
			p.updateInventory();
			p.playSound(p.getLocation(), Sound.NOTE_PIANO, 0.5f, 10);
			
		} catch (Exception e) {
			e.printStackTrace();
			p.closeInventory();
			System.out.println("Erro ao abrir o menuCategorys, favor contate o desenvolvedor!.");
		}
	}
	
}
