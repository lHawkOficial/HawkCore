package me.hawkcore.utils.missions.objects;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import lombok.Getter;
import lombok.Setter;
import me.hawkcore.Core;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.Save;
import me.hawkcore.utils.missions.ManagerMissions;

@Getter
public class MissionPlayer {

	private String name;
	private List<MissionCategory> categorys = new ArrayList<>();
	@Setter
	private Mission missionSelected;
	@Setter
	private MissionCategory categoryToComplete;
	@Setter
	private MissionCategory categorySelected;
	private File file;
	
	public MissionPlayer(String namePlayer) {
		this.name = namePlayer;
		File pasta = new File(Core.getInstance().getDataFolder() + "/missions/players");
		if (!pasta.exists()) pasta.mkdir();
		file = new File(pasta + "/"+name+".json");
		if (!file.exists()) {
			try {
				file.createNewFile();
				save();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		load();
		ManagerMissions.get().getPlayers().add(this);
	}
	
	public MissionCategory getMissionCategory(String name) {
		Iterator<MissionCategory> it = categorys.iterator();
		while(it.hasNext()) {
			MissionCategory category = it.next();
			if (category.getName().equalsIgnoreCase(name)) return category;
		}
		return null;
	}
	
	public int getTotalMissions() {
		int total = 0;
		for(MissionCategory category : categorys) {
			total += category.getMissions().size();
		}
		return total;
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayerExact(name);
	}
	
	public static MissionPlayer check(Player p) {
		if (p.hasMetadata("missionplayer")) return (MissionPlayer) p.getMetadata("missionplayer").get(0).value();
		MissionPlayer player = ManagerMissions.get().getMissionPlayer(p.getName());
		if (player == null) player = new MissionPlayer(p.getName());
		p.setMetadata("missionplayer", new FixedMetadataValue(Core.getInstance(), player));
		return player;
	}
	
	public void saveAsync() {
		Task.runAsync(()-> save());
	}
	
	public void save() {
		List<Object> lista = new ArrayList<>();
		List<Object> missoes = new ArrayList<>();
		for(MissionCategory category : categorys) {
			for(Mission mission : category.getMissions()) {
				missoes.add(category.getName()+":"+mission.getName()+":"+String.valueOf(mission.isCompleted())+":"+mission.getObjective().getValue());
			}
		}
		lista.add(missoes);
		lista.add(categoryToComplete == null ? "N.A" : categoryToComplete.getName());
		new Save(file, lista);
	}
	
	@SuppressWarnings({ "unchecked" })
	public void load() {
		for(MissionCategory category : ManagerMissions.get().getCategorys()) {
			category.clone(this);
		}
		List<Object> lista = Save.load(file);
		if (lista == null) return;
		if (lista.isEmpty()) return;
		List<Object> missoes = (List<Object>) lista.get(0);
		for(Object o : missoes) {
			String linha = (String) o;
			String[] args = linha.split(":");
			MissionCategory category = getMissionCategory(args[0]);
			if (category!=null) {
				Mission mission = category.getMission(args[1]);
				if (mission != null) {
					mission.setCompleted(Boolean.valueOf(args[2]));
					mission.getObjective().setValue(Double.valueOf(args[3]));
				}
			}
		}
		categoryToComplete = ((String)lista.get(1)).equalsIgnoreCase("N.A") ? null : getMissionCategory(((String)lista.get(1)));		
		
		List<MissionCategory> categorys = new ArrayList<>();
		List<MissionCategory> rest = new ArrayList<>();
		int i = -1;
		while(!this.categorys.isEmpty() && i < this.categorys.size()) {
			try {
				i++;
				MissionCategory category = this.categorys.get(i);
				for(char c = 'a'; c < 'z'; c++) {
					if (category.getName().toLowerCase().toCharArray()[0] == c) {
						categorys.add(category);
					}
				}
				if (!categorys.contains(category))
				rest.add(category);
			} catch (Exception e) {
				continue;
			}
		}
		this.categorys = new ArrayList<>(categorys);
		this.categorys.addAll(rest);
		
		for(MissionCategory category : this.categorys) {
			List<Mission> missions = new ArrayList<>();
			List<Mission> restM = new ArrayList<>();
			i = -1;
			while(!category.getMissions().isEmpty() && i < category.getMissions().size()) {
				try {
					i++;
					Mission mission = category.getMissions().get(i);
					for(char c = 'a'; c < 'z'; c++) {
						if (mission.getName().toLowerCase().toCharArray()[0] == c) {
							missions.add(mission);
						}
					}
					if (!missions.contains(mission))
					restM.add(mission);
				} catch (Exception e) {
				}
			}
			category.getMissions().clear();
			category.getMissions().addAll(missions);
			category.getMissions().addAll(restM);
		}
		if (categoryToComplete == null && !this.categorys.isEmpty()) categoryToComplete = this.categorys.get(0);
		save();
	}
	
}
