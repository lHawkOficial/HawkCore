package me.hawkcore.utils.missions.types.utils;

import org.bukkit.entity.Player;

import me.hawkcore.utils.missions.ManagerMissions;
import me.hawkcore.utils.missions.objects.Mission;
import me.hawkcore.utils.missions.objects.MissionCategory;
import me.hawkcore.utils.missions.objects.MissionPlayer;

public class MissionAPI {

	public static MissionCategory getMissionCategory(Player p, String name) {
		MissionPlayer mp = MissionPlayer.check(p);
		return mp.getMissionCategory(name);
	}
	
	public static Mission getMission(Player p, MissionCategory category, String name) {
		return category == null ? null : category.getMission(name);
	}
	
	public static boolean isCategoryMissionComplete(Player p, String name) {
		MissionCategory category = getMissionCategory(p, name);
		return category == null ? false : category.isCompleted();
	}
	
	public static boolean isMissionCompleted(Player p, MissionCategory category, String nameMission) {
		Mission mission = getMission(p, category, nameMission);
		return mission == null ? false : mission.isCompleted();
	}
	
	public static ManagerMissions get() {
		return ManagerMissions.get();
	}
	
}
