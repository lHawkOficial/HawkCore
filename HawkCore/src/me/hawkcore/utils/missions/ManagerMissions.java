package me.hawkcore.utils.missions;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import lombok.Getter;
import me.hawkcore.Core;
import me.hawkcore.utils.missions.objects.MissionCategory;
import me.hawkcore.utils.missions.objects.MissionPlayer;

@Getter
public class ManagerMissions {

	private List<MissionPlayer> players = new ArrayList<>();
	private List<MissionCategory> categorys = new ArrayList<>();
	
	public MissionPlayer getMissionPlayer(String name) {
		Iterator<MissionPlayer> it = players.iterator();
		while(it.hasNext()) {
			MissionPlayer player = it.next();
			if (player.getName().equalsIgnoreCase(name)) return player;
		}
		return null;
	}
	
	public static ManagerMissions get() {
		return Core.getInstance().getManagermissions();
	}
	
	public boolean containsMissionPlayer(String name) {
		return getMissionPlayer(name) != null;
	}
	
	public static void checkPlayers() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			MissionPlayer.check(p);
		}
	}
	
}
