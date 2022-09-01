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
import me.hawkcore.utils.missions.objects.Mission;
import me.hawkcore.utils.missions.objects.MissionCategory;
import me.hawkcore.utils.missions.objects.MissionPlayer;

@Getter
public class menuMissions {

	private String name, concluido, id;
	private int size;
	private List<Integer> slots = new ArrayList<>();
	private Glass glass;
	private Item iconNext,
	iconBack,
	iconMission,
	iconCategorys;
	
	public menuMissions() {
		ConfigurationSection section = Core.getInstance().getConfig().getConfigurationSection("Missions.Menus.menuMissions");
		name = section.getString("name").replace("&", "§");
		size = section.getInt("size");
		concluido = section.getString("concluido").replace("&", "§");
		glass = new Glass(new ArrayList<>(section.getStringList("glass")), size);
		for(String linha : section.getStringList("slots")) {
			try {
				slots.add(Integer.valueOf(linha)-1);
			} catch (Exception e) {
				continue;
			}
		}
		id = section.getString("id");
		
		section = Core.getInstance().getConfig().getConfigurationSection("Missions.Menus.menuMissions.Icons.iconNext");
		Item item = new Item(section.getString("ID"));
		item.setDisplayName(section.getString("name").replace("&", "§"));
		List<String> lore = new ArrayList<>(section.getStringList("lore"));
		lore.replaceAll(l -> l.replace("&", "§"));
		item.setLore(lore);
		if (section.getBoolean("glow")) item.setGlow();
		item.setSlot(section.getInt("slot")-1);
		iconNext = item;
		
		section = Core.getInstance().getConfig().getConfigurationSection("Missions.Menus.menuMissions.Icons.iconBack");
		item = new Item(section.getString("ID"));
		item.setDisplayName(section.getString("name").replace("&", "§"));
		lore = new ArrayList<>(section.getStringList("lore"));
		lore.replaceAll(l -> l.replace("&", "§"));
		item.setLore(lore);
		if (section.getBoolean("glow")) item.setGlow();
		item.setSlot(section.getInt("slot")-1);
		iconBack = item;
		
		section = Core.getInstance().getConfig().getConfigurationSection("Missions.Menus.menuMissions.Icons.iconCategorys");
		item = new Item(section.getString("ID"));
		item.setDisplayName(section.getString("name").replace("&", "§"));
		lore = new ArrayList<>(section.getStringList("lore"));
		lore.replaceAll(l -> l.replace("&", "§"));
		item.setLore(lore);
		if (section.getBoolean("glow")) item.setGlow();
		item.setSlot(section.getInt("slot")-1);
		iconCategorys = item;
		
		section = Core.getInstance().getConfig().getConfigurationSection("Missions.Menus.menuMissions.Icons.iconMission");
		item = new Item(section.getString("ID"));
		item.setDisplayName(section.getString("name").replace("&", "§"));
		lore = new ArrayList<>(section.getStringList("lore"));
		lore.replaceAll(l -> l.replace("&", "§"));
		item.setLore(lore);
		if (section.getBoolean("glow")) item.setGlow();
		iconMission = item;
	}
	
	public static menuMissions get() {
		return ConfigMission.get().getMenumissions();
	}
	
	public void open(Player p) {
		try {
			
			MissionPlayer mp = MissionPlayer.check(p);
			MissionCategory category = mp.getCategorySelected();
			if (category==null) return;
			if (!p.hasMetadata("pMissions")) p.setMetadata("pMissions", new FixedMetadataValue(Core.getInstance(), new PaginaCreator(new ArrayList<>(category.getMissions()), slots.size())));
			PaginaCreator pagina = (PaginaCreator) p.getMetadata("pMissions").get(0).value();
			Inventory inv = Bukkit.createInventory(null, 9*size, name.replace("{nome}", category.getName()).replace("{pagina}", String.valueOf(pagina.getPaginaAtual())).replace("{total}", String.valueOf(pagina.getTotalPaginas())));
			
			for(Item item : glass.getVidros()) {
				inv.setItem(item.getSlot(), item.build().clone());
			}
			
			List<Object> lista = pagina.getPagina(pagina.getPaginaAtual());
			Mission selected = mp.getMissionSelected();
			for (int i = 0; i < slots.size(); i++) {
				int slot = slots.get(i);
				try {
					Mission mission = (Mission) lista.get(i);
					mp.setMissionSelected(mission);
					if (!mission.isCompleted() && !category.getMissionToComplete().getMission().equals(mission)) {
						inv.setItem(slot, iconMission.build().clone());
						continue;
					}
					Item item = new Item(mission.isCompleted() ? API.get().getItemStack(menuMissions.get().getId()) : mission.getIcon().build().clone());
					if (mission.isCompleted()) item.setGlow();
					item.setDisplayName(mission.getIcon().getItem().getItemMeta().getDisplayName());
					List<String> lore = new ArrayList<>(mission.getIcon().getItem().getItemMeta().getLore());
					lore.replaceAll(l -> PlaceholderAPI.setPlaceholders(p, l).replace("&", "§"));
					if (mission.isCompleted()) lore.add(menuMissions.get().getConcluido());
					item.setLore(lore);
					inv.setItem(slot, item.build());
				} catch (Exception e) {
					inv.setItem(slot, iconMission.build().clone());
				}
			}
			mp.setMissionSelected(selected);
			
			inv.setItem(iconNext.getSlot(), iconNext.getItem().clone());
			inv.setItem(iconBack.getSlot(), iconBack.getItem().clone());
			inv.setItem(iconCategorys.getSlot(), iconCategorys.getItem().clone());
			
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
