package me.hawkcore.utils.events.events.fight.utils;

import java.io.File;



import org.bukkit.configuration.file.YamlConfiguration;

import me.hawkcore.Core;
import me.hawkcore.utils.events.events.fight.Fight;
import me.hawkcore.utils.events.utils.enums.EventType;
import me.hawkcore.utils.files.FileAPI;

public class FightAPI {

	public static void checkFiles() {
		File folder = new File(Core.getInstance().getDataFolder() + "/events/fight");
		if (!folder.exists()) folder.mkdirs();
		File fileConfig = new File(folder + "/config.yml");
		if (!fileConfig.exists()) {
			FileAPI.copyFileToPath(Core.getInstance().getClass(), "/events/fight.yml", folder + "/config.yml");
		}
		fileConfig = new File(folder + "/config.yml");
		new Fight("Fight", folder, YamlConfiguration.loadConfiguration(fileConfig), EventType.PVP, true);
	}
	
}
