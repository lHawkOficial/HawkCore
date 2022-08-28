package me.hawkcore.utils.missions.objects;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import me.hawkcore.utils.items.Item;
import me.hawkcore.utils.missions.Mission;
import me.hawkcore.utils.missions.MissionPlayer;

@Getter
public class MissionCategory {

	private MissionPlayer player;
	private Item icon;
	private String name;
	private List<Mission> missions = new ArrayList<>();
	private int missionNumber;
	
	public MissionCategory(MissionPlayer player, String nameCategory, Item icon) {
		this.name = nameCategory;
		this.icon = icon;
		this.player = player;
		this.player.getCategorys().add(this);
	}
	
	public Mission getMission(String name) {
		Iterator<Mission> it = missions.iterator();
		while(it.hasNext()) {
			Mission mission = it.next();
			if (mission.getName().equalsIgnoreCase(name)) return mission; 
		}
		return null;
	}
	
	public Mission getMission(ItemStack item) {
		Iterator<Mission> it = missions.iterator();
		while(it.hasNext()) {
			Mission mission = it.next();
			if (mission.getIcon().build().isSimilar(item) || (mission.getIcon().build().hasItemMeta() && mission.getIcon().build().getItemMeta().hasDisplayName() && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && mission.getIcon().getItem().getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName()))) return mission; 
		}
		return null;
	}
	
	public Mission getMissionToComplete() {
		return missions.size() > 0 && missionNumber < missions.size() ? missions.get(missionNumber) : null;
	}
	
}
