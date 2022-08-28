package me.hawkcore.utils.missions.objects;

import org.bukkit.configuration.ConfigurationSection;

import lombok.Getter;
import me.hawkcore.Core;
import me.hawkcore.utils.missions.menus.menuCategorys;
import me.hawkcore.utils.missions.menus.menuMissions;

@Getter
public class ConfigMission {
	
	private String mission_complete;
	private menuCategorys menucategorys;
	private menuMissions menumissions;

	public ConfigMission() {
		ConfigurationSection section = Core.getInstance().getConfig().getConfigurationSection("Missions");
		mission_complete = section.getString("mission_complete");
		menucategorys = new menuCategorys();
		menumissions = new menuMissions();
	}
	
	public static ConfigMission get() {
		return Core.getInstance().getConfigmission();
	}
	
}
