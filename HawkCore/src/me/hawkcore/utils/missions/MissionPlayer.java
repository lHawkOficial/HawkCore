package me.hawkcore.utils.missions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import lombok.Getter;
import me.hawkcore.utils.missions.objects.MissionCategory;

@Getter
public class MissionPlayer {

	private String name;
	private List<MissionCategory> categorys = new ArrayList<>();
	
	public MissionPlayer(String namePlayer) {
		this.name = namePlayer;
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
		MissionPlayer player = ManagerMissions.get().getMissionPlayer(p.getName());
		if (player == null) player = new MissionPlayer(p.getName());
		return player;
	}
	
}
