package me.hawkcore.utils.missions.objects;

import java.io.File;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.Setter;
import me.hawkcore.Core;
import me.hawkcore.utils.items.Item;
import me.hawkcore.utils.missions.ManagerMissions;
import me.hawkcore.utils.missions.types.MissionBreakBlock;
import me.hawkcore.utils.missions.types.MissionBreakItem;
import me.hawkcore.utils.missions.types.MissionChatEvent;
import me.hawkcore.utils.missions.types.MissionCraftItem;
import me.hawkcore.utils.missions.types.MissionDropItem;
import me.hawkcore.utils.missions.types.MissionEatEvent;
import me.hawkcore.utils.missions.types.MissionEnchantItem;
import me.hawkcore.utils.missions.types.MissionKillEntity;
import me.hawkcore.utils.missions.types.MissionMoveDistance;
import me.hawkcore.utils.missions.types.MissionPickItem;
import me.hawkcore.utils.missions.types.MissionPlaceBlock;

@Getter
public class MissionCategory {

	@Setter
	private MissionPlayer player;
	private Item icon;
	private String name;
	private List<Mission> missions = new ArrayList<>();
	private int missionNumber;
	private File pasta;
	
	public MissionCategory(String nameCategory) {
		this.name = nameCategory;
		this.pasta = new File(Core.getInstance().getDataFolder() + "/missions/categorys/" + name);
		if (!pasta.exists()) pasta.mkdir();
		this.loadIcon();
		ManagerMissions.get().getCategorys().add(this);
	}
	
	public MissionCategory(String nameCategory, MissionPlayer mp) {
		this.name = nameCategory;
		this.pasta = new File(Core.getInstance().getDataFolder() + "/missions/categorys/" + name);
		if (!pasta.exists()) pasta.mkdir();
		this.loadIcon();
		this.player = mp;
		mp.getCategorys().add(this);
	}
	
	public Mission getMission(String name) {
		Iterator<Mission> it = missions.iterator();
		while(it.hasNext()) {
			Mission mission = it.next();
			if (mission.getName().equalsIgnoreCase(name)) return mission; 
		}
		return null;
	}
	
	public void loadIcon() {
		if (icon!=null) return;
		File file = new File(pasta + "/config.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		ConfigurationSection section = config.getConfigurationSection("Icon");
		Item item = new Item(section.getString("ID"));
		item.setDisplayName(section.getString("name").replace("&", "§"));
		List<String> lore = new ArrayList<>(section.getStringList("lore"));
		lore.replaceAll(l -> l.replace("&", "§"));
		item.setLore(lore);
		if (section.getBoolean("glow")) item.setGlow();
		icon = item;
	}
	
	public static MissionCategory get(ItemStack item, Player p) {
		MissionPlayer mp = MissionPlayer.check(p);
		Iterator<MissionCategory> it = mp.getCategorys().iterator();
		while(it.hasNext()) {
			MissionCategory category = it.next();
			if (category.getIcon().build().isSimilar(item) || (category.getIcon().build().hasItemMeta() && category.getIcon().build().getItemMeta().hasDisplayName() && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && category.getIcon().getItem().getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName()))) return category; 
		}
		return null;
	}
	
	public String progress() {
		int total = 0;
		for(Mission mission : missions) {
			int percent = 0;
			if (mission.getObjective() instanceof MissionBreakBlock) percent = Double.valueOf(((MissionBreakBlock)mission.getObjective()).progress().replace("%", new String())).intValue();
			if (mission.getObjective() instanceof MissionDropItem) percent = Double.valueOf(((MissionDropItem)mission.getObjective()).progress().replace("%", new String())).intValue();
			if (mission.getObjective() instanceof MissionMoveDistance) percent = Double.valueOf(((MissionMoveDistance)mission.getObjective()).progress().replace("%", new String())).intValue();
			if (mission.getObjective() instanceof MissionPickItem) percent = Double.valueOf(((MissionPickItem)mission.getObjective()).progress().replace("%", new String())).intValue();
			if (mission.getObjective() instanceof MissionPlaceBlock) percent = Double.valueOf(((MissionPlaceBlock)mission.getObjective()).progress().replace("%", new String())).intValue();
			if (mission.getObjective() instanceof MissionKillEntity) percent = Double.valueOf(((MissionKillEntity)mission.getObjective()).progress().replace("%", new String())).intValue();
			if (mission.getObjective() instanceof MissionEnchantItem) percent = Double.valueOf(((MissionEnchantItem)mission.getObjective()).progress().replace("%", new String())).intValue();
			if (mission.getObjective() instanceof MissionCraftItem) percent = Double.valueOf(((MissionCraftItem)mission.getObjective()).progress().replace("%", new String())).intValue();
			if (mission.getObjective() instanceof MissionEatEvent) percent = Double.valueOf(((MissionEatEvent)mission.getObjective()).progress().replace("%", new String())).intValue();
			if (mission.getObjective() instanceof MissionChatEvent) percent = Double.valueOf(((MissionChatEvent)mission.getObjective()).progress().replace("%", new String())).intValue();
			if (mission.getObjective() instanceof MissionBreakItem) percent = Double.valueOf(((MissionBreakItem)mission.getObjective()).progress().replace("%", new String())).intValue();
			total+=percent;
		}
		return (missions.isEmpty() ? 0 : total/missions.size()) + "%";
	}
	
	public Mission getMission(ItemStack item) {
		Iterator<Mission> it = missions.iterator();
		while(it.hasNext()) {
			Mission mission = it.next();
			if (mission.getIcon().build().isSimilar(item) || (mission.getIcon().build().hasItemMeta() && mission.getIcon().build().getItemMeta().hasDisplayName() && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && mission.getIcon().getItem().getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName()))) return mission; 
		}
		return null;
	}
	
	public boolean isCompleted() {
		boolean b = true;
		for(Mission m : missions) {
			if (!m.isCompleted()) b = false;
		}
		return b;
	}
	
	public int getTotalCompleted() {
		int total = 0;
		for(Mission mission : missions) {
			if (!mission.isCompleted()) continue;
			total++;
		}
		return total;
	}
	
	public Mission getMissionToComplete() {
		for(Mission mission : missions) {
			if (mission.isCompleted()) continue;
			return mission;
		}
		return null;
	}
	
	public MissionCategory clone(MissionPlayer mp) {
		MissionCategory category =  new MissionCategory(name, mp);
		for(Mission mission : missions) {
			new Mission(category, mission.getName(), mission.getIdMission(), mp);
		}
		return category;
	}
	
}
