package me.hawkcore.utils.missions.objects;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.Setter;
import me.hawkcore.Core;
import me.hawkcore.utils.itemcreator.ManagerItemCreator;
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
import me.hawkcore.utils.missions.types.utils.MissionObjective;

@Getter
public class Mission {
	
	private String name, description, msgFinalize;
	private Item icon;
	private MissionObjective objective;
	@Setter
	private MissionPlayer player;
	private MissionCategory category;
	private File file;
	private List<String> rewards = new ArrayList<>();
	private double valueRequired;
	@Setter
	private boolean completed = false;
	private int idMission;

	public Mission(MissionCategory category, String name, int idMission) {
		this.name = name;
		this.file = new File(category.getPasta() + "/missions/" + name + ".mission");
		this.player = category.getPlayer();
		this.category = category;
		this.category.getMissions().add(this);
		this.load();
	}
	
	public Mission(MissionCategory category, String name, int idMission, MissionPlayer mp) {
		this.name = name;
		this.file = new File(category.getPasta() + "/missions/" + name + ".mission");
		this.player = category.getPlayer();
		this.category = category;
		this.category.getMissions().add(this);
		this.load();
		this.player = mp;
	}
	
	public static ManagerMissions get() {
		return ManagerMissions.get();
	}
	
	public void load() {
		if (icon!=null) return;
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		ConfigurationSection section = config.getConfigurationSection("Icon");
		Item item = new Item(section.getString("ID"));
		item.setDisplayName(section.getString("name").replace("&", "�"));
		List<String> lore = new ArrayList<>(section.getStringList("lore"));
		lore.replaceAll(l -> l.replace("&", "�"));
		item.setLore(lore);
		if (section.getBoolean("glow")) item.setGlow();
		icon = item;
		section = config.getConfigurationSection("Config");
		description = section.getString("objective").replace("&", "�");
		valueRequired = section.getDouble("valueRequired");
		msgFinalize = section.getString("msgFinalize").replace("&", "�").replace("{tag}", Core.getInstance().getTag());
		rewards = new ArrayList<>(section.getStringList("rewards"));
		rewards.replaceAll(l -> l.toLowerCase().replace("/", new String()));
		ItemStack it = ManagerItemCreator.get().getItem(section.getString("item")).getItem().clone();
		idMission = section.getInt("idMission");
		EntityType entityType = null;
		try {
			entityType = EntityType.valueOf(section.getString("entityType").toUpperCase());
		} catch (Exception e) {
		}
		switch (idMission) {
		case 1:
			objective = new MissionBreakBlock(this, (int) valueRequired, it);
			break;
		case 2:
			objective = new MissionDropItem(this, (int) valueRequired, it);
			break;
		case 3:
			objective = new MissionMoveDistance(this, valueRequired);
			break;
		case 4:
			objective = new MissionPickItem(this, (int) valueRequired, it);
			break;
		case 5:
			objective = new MissionPlaceBlock(this, (int) valueRequired, it);
			break;
		case 6:
			objective = new MissionKillEntity(this, (int) valueRequired, entityType);
			break;
		case 7:
			objective = new MissionEnchantItem(this, (int) valueRequired);
			break;
		case 8:
			objective = new MissionCraftItem(this, (int) valueRequired);
			break;
		case 9:
			objective = new MissionEatEvent(this, (int) valueRequired);
			break;
		case 10:
			objective = new MissionChatEvent(this, (int) valueRequired);
			break;
		case 11:
			objective = new MissionBreakItem(this, (int) valueRequired);
			break;
		default:
			System.out.println("[!] Miss�o " + name + " removida por n�o achar o id " + idMission);
			category.getMissions().remove(this);
			break;
		}
	}
}
