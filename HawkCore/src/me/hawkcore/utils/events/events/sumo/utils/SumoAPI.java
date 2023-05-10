package me.hawkcore.utils.events.events.sumo.utils;

import java.io.File;




import org.bukkit.configuration.file.YamlConfiguration;

import me.hawkcore.Core;
import me.hawkcore.utils.events.events.sumo.Sumo;
import me.hawkcore.utils.events.utils.enums.EventType;
import me.hawkcore.utils.files.FileAPI;

public class SumoAPI {

	public static void checkFiles() {
		File folder = new File(Core.getInstance().getDataFolder() + "/events/sumo");
		if (!folder.exists()) folder.mkdirs();
		File fileConfig = new File(folder + "/config.yml");
		if (!fileConfig.exists()) {
			FileAPI.copyFileToPath(Core.getInstance().getClass(), "/events/sumo.yml", folder + "/config.yml");
		}
		fileConfig = new File(folder + "/config.yml");
		new Sumo("Sumo", folder, YamlConfiguration.loadConfiguration(fileConfig), EventType.PVP, true);
	}
	
}
