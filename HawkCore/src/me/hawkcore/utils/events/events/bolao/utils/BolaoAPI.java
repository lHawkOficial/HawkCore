package me.hawkcore.utils.events.events.bolao.utils;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import me.hawkcore.Core;
import me.hawkcore.utils.events.events.bolao.Bolao;
import me.hawkcore.utils.events.utils.enums.EventType;
import me.hawkcore.utils.files.FileAPI;

public class BolaoAPI {

	public static void checkFiles() {
		File folder = new File(Core.getInstance().getDataFolder() + "/events/bolao");
		if (!folder.exists()) folder.mkdirs();
		File fileConfig = new File(folder + "/config.yml");
		if (!fileConfig.exists()) {
			FileAPI.copyFileToPath(Core.getInstance().getClass(), "/events/bolao.yml", folder + "/config.yml");
		}
		new Bolao("Bolao", folder, YamlConfiguration.loadConfiguration(fileConfig), EventType.CHAT, true);
	}
	
}
