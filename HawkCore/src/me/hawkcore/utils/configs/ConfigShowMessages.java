package me.hawkcore.utils.configs;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import lombok.Getter;
import me.hawkcore.Core;
import me.hawkcore.utils.ConfigGeral;

@Getter
public class ConfigShowMessages {

	private Boolean join_active,quit_active;
	private String join_message,quit_message;
	
	public ConfigShowMessages() {
		FileConfiguration config = Core.getInstance().getConfig();
		ConfigurationSection section = config.getConfigurationSection("Config");
		join_active = section.getBoolean("join.active");
		quit_active = section.getBoolean("quit.active");
		join_message = section.getString("join.message").replace("&", "§");
		quit_message = section.getString("quit.message").replace("&", "§");
	}
	
	public static ConfigShowMessages get() {
		return ConfigGeral.get().getConfigshowmessages();
	}
	
}
