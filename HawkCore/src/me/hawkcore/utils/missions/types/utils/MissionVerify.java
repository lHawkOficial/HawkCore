package me.hawkcore.utils.missions.types.utils;

import org.bukkit.entity.Player;

import me.hawkcore.utils.missions.objects.Mission;
import me.hawkcore.utils.missions.objects.MissionPlayer;

public class MissionVerify {

	private Player player;
	private Mission mission;
	
	public MissionVerify(Player player,Mission mission) {
		this.player = player;
		this.mission = mission;
	}
	
	public boolean queue() {
		Player p = player;
		Mission m = mission;
		if (m == null) return false;
		MissionQueue queue = m.getCategory().getMissionToComplete();
		Mission mission = queue.getMission();
		MissionPlayer mp = MissionPlayer.check(p);
		if (!mp.equals(mission.getPlayer())) return false;
		if (!queue.isValid()) return false;
		if (!queue.isEquals(mission, mp.getMissionID())) return false;
		return true;
	}
	
}
