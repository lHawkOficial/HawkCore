package me.hawkcore.utils.configs;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import lombok.Getter;
import me.hawkcore.Core;
import me.hawkcore.utils.ConfigGeral;

@Getter
public class ConfigCommands {

	private Boolean activeCommandDesenchant;
	private double valueCommandDesenchant;
	
	public ConfigCommands() {
		FileConfiguration config = Core.getInstance().getConfig();
		ConfigurationSection section = config.getConfigurationSection("Config");
		activeCommandDesenchant = section.getBoolean("commandDesenchant.active");
		valueCommandDesenchant = section.getDouble("commandDesenchant.value");
	}
	
	public static ConfigCommands get() {
		return ConfigGeral.get().getConfigcommands();
	}
	
}
