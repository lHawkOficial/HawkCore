package me.hawkcore.utils.events.events.parkour.utils;

import java.io.File;


import org.bukkit.configuration.file.YamlConfiguration;

import me.hawkcore.Core;
import me.hawkcore.utils.events.events.parkour.Parkour;
import me.hawkcore.utils.events.utils.enums.EventType;
import me.hawkcore.utils.files.FileAPI;

public class ParkourAPI {

	public static void checkFiles() {
		File folder = new File(Core.getInstance().getDataFolder() + "/events/parkour");
		if (!folder.exists()) folder.mkdirs();
		File fileConfig = new File(folder + "/config.yml");
		if (!fileConfig.exists()) {
			FileAPI.copyFileToPath(Core.getInstance().getClass(), "/events/parkour.yml", folder + "/config.yml");
		}
		fileConfig = new File(folder + "/config.yml");
		new Parkour("Parkour", folder, YamlConfiguration.loadConfiguration(fileConfig), EventType.CHAT, true);
	}
	
}
