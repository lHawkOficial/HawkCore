package me.hawkcore.utils.missions.objects;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import lombok.Getter;
import me.hawkcore.Core;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.missions.ManagerMissions;
import me.hawkcore.utils.missions.menus.menuCategorys;
import me.hawkcore.utils.missions.menus.menuMissions;

@Getter
public class ConfigMission {
	
	private String mission_complete;
	private menuCategorys menucategorys;
	private menuMissions menumissions;
	private int save_period;
	private boolean activeMissions;

	public ConfigMission() {
		ConfigurationSection section = Core.getInstance().getConfig().getConfigurationSection("Missions");
		mission_complete = section.getString("mission_complete").replace("&", "§");
		save_period = section.getInt("save_period");
		activeMissions = section.getBoolean("activeMissions");
		menucategorys = new menuCategorys();
		menumissions = new menuMissions();
		
		if (!activeMissions) return;
		File pasta = new File(Core.getInstance().getDataFolder() + "/missions");
		if (!pasta.exists()) pasta.mkdir();
		pasta = new File(pasta + "/categorys");
		if (!pasta.exists()) pasta.mkdir();
		
		if (pasta.listFiles().length == 0) {
			pasta = new File(pasta + "/CategoriaExemplo");
			if (!pasta.exists()) pasta.mkdir();
			File file = new File(pasta + "/config.yml");
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			config.createSection("Icon");
			section = config.getConfigurationSection("Icon");
			section.set("ID", "386>0");
			section.set("name", "&eCategoria Exemplo");
			List<String> lore = new ArrayList<>();
			lore.add(" ");
			lore.add("&8# &7Missões: &f%hawkcore_missions_categoryTotalMissions%");
			lore.add("&8# &7Concluídas: &f%hawkcore_missions_categoryMissionsCompleted%");
			lore.add("&8# &7Progresso: &2%hawkcore_missions_categoryProgress%");
			lore.add(" ");
			lore.add("&6# &7Ao completar esta categoria você ganhará uma recompensa!.");
			lore.add("&6# &7Clique para ver as missões.");
			section.set("lore", new ArrayList<>(lore));
			section.set("glow", true);
			try {
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			pasta = new File(pasta + "/missions");
			if (!pasta.exists()) pasta.mkdir();
			file = new File(pasta+"/missionExample.mission");
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			config = YamlConfiguration.loadConfiguration(file);
			config.createSection("Icon");
			section = config.getConfigurationSection("Icon");
			section.set("ID", "386>0");
			section.set("name", "&eMissão");
			lore = new ArrayList<>();
			lore.add(" ");
			lore.add("&8# &7Nome: &f%hawkcore_missions_missionName%");
			lore.add(" ");
			lore.add("&8# &7Objetivo: &e%hawkcore_missions_missionDesc%");
			lore.add("&8# &7Progresso: &2%hawkcore_missions_missionProgress%");
			lore.add(" ");
			section.set("lore", new ArrayList<>(lore));
			section.set("glow", true);
			
			section = config.createSection("Config");
			List<String> lista = new ArrayList<>();
			lista.add("ic give %player_name% diamante 1");
			section.set("objective", "Quebrar 3 blocos de grama");
			section.set("valueRequired", 3);
			section.set("entityType", "sheep");
			section.set("item", "itemMissionExample");
			section.set("idMission", 1);
			section.set("msgFinalize", "{tag} &6Voce finalizou todas as missoes e ganhou um diamante, parabens!");
			section.set("rewards", new ArrayList<>(lista));
			
			try {
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		pasta = new File(Core.getInstance().getDataFolder() + "/missions/categorys");
		for(File f : pasta.listFiles()) {
			if (!f.isDirectory()) continue;
			String nameCategory = f.getName();
			MissionCategory category = new MissionCategory(nameCategory);
			loop: for(File file : f.listFiles()) {
				if (!(file.getName().equals("missions") && file.isDirectory())) continue loop;
				loop1: for(File f1 : file.listFiles()) {
					if (!f1.getName().endsWith(".mission")) continue loop1;
					String nameMission = f1.getName().replace(".mission", new String());
					new Mission(category, nameMission, 1);
				}
				break loop;
			}
		}
		
		new Task(new Runnable() {
			long timeSave = System.currentTimeMillis();
			@Override
			public void run() {
				if (System.currentTimeMillis() - timeSave >= 1000*save_period) {
					timeSave = System.currentTimeMillis();
					ManagerMissions.get().getPlayers().forEach(mp -> mp.save());
				}
			}
		}).run(20*60);
		
	}
	
	public static ConfigMission get() {
		return Core.getInstance().getConfigmission();
	}
	
}
