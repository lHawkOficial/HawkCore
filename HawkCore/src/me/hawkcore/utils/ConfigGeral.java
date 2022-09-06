package me.hawkcore.utils;

import org.bukkit.configuration.ConfigurationSection;

import lombok.Getter;
import me.hawkcore.Core;

@Getter
public class ConfigGeral {
	
	private Boolean thirst,
	heat,
	actionBarActive;
	
	private String actionBar;
	
	public ConfigGeral() {
		ConfigurationSection section = Core.getInstance().getConfig().getConfigurationSection("Config");
		thirst = section.getBoolean("thirst");
		heat = section.getBoolean("heat");
		actionBarActive = section.getBoolean("actionBarActive");
		actionBar = section.getString("actionBar").replace("&", "§");
	}
	
	public static ConfigGeral get() {
		return Core.getInstance().getConfiggeral();
	}
	
}
