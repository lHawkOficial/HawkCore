package me.hawkcore.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import lombok.Getter;
import me.hawkcore.Core;
import me.hawkcore.utils.configs.ConfigCommands;
import me.hawkcore.utils.configs.ConfigShowMessages;

@Getter
public class ConfigGeral {
	
	private ConfigCommands configcommands;
	private ConfigShowMessages configshowmessages;
	private Boolean thirst,
	heat,
	actionBarActive;
	private List<String> regionsDisables;
	private String actionBar;
	
	public ConfigGeral() {
		configcommands = new ConfigCommands();
		configshowmessages = new ConfigShowMessages();
		ConfigurationSection section = Core.getInstance().getConfig().getConfigurationSection("Config");
		thirst = section.getBoolean("thirst");
		heat = section.getBoolean("heat");
		actionBarActive = section.getBoolean("actionBarActive");
		actionBar = section.getString("actionBar").replace("&", "§");
		regionsDisables = new ArrayList<>(section.getStringList("regionsDisables"));
	}
	
	public static ConfigGeral get() {
		return Core.getInstance().getConfiggeral();
	}
	
}
