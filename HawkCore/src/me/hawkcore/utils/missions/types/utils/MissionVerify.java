package me.hawkcore.utils.missions.types.utils;

import org.bukkit.entity.Player;

import me.hawkcore.utils.missions.objects.Mission;
import me.hawkcore.utils.missions.objects.MissionPlayer;

public class MissionVerify {

	private Player player;
	private Mission missionListener;
	private MissionQueue queue;
	
	public MissionVerify(Player player, Mission missionListener) {
		this.player = player;
		this.missionListener = missionListener;
		this.queue = missionListener == null ? null : missionListener.getCategory().getMissionToComplete();
	}
	
	public boolean queue() {
		Player p = player;
		if (queue == null) return false;
		if (missionListener == null) return false;
		Mission mission = queue.getMission();
		if (mission == null) return false;
		MissionPlayer mp = MissionPlayer.check(p);
		if (!queue.getMission().equals(missionListener)) return false;
		if (!mp.equals(missionListener.getPlayer())) return false;
		return true;
	}
	
}
