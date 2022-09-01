package me.hawkcore.utils;

import org.bukkit.configuration.ConfigurationSection;

import lombok.Getter;
import me.hawkcore.Core;

@Getter
public class ConfigGeral {
	
	private Boolean thirst,
	heat;
	
	public ConfigGeral() {
		ConfigurationSection section = Core.getInstance().getConfig().getConfigurationSection("Config");
		thirst = section.getBoolean("thirst");
		heat = section.getBoolean("heat");
	}
	
	public static ConfigGeral get() {
		return Core.getInstance().getConfiggeral();
	}
	
}
