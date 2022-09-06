package me.hawkcore.utils.missions.listeners.plugin;

import lombok.Getter;
import me.hawkcore.utils.listenercreator.Listener;
import me.hawkcore.utils.missions.objects.Mission;
import me.hawkcore.utils.missions.objects.MissionPlayer;

@Getter
public class MissionCollectItemEvent extends Listener {

	private MissionPlayer missionPlayer;
	private Mission mission;
	
	public MissionCollectItemEvent(MissionPlayer missionPlayer, Mission mission) {
		this.missionPlayer = missionPlayer;
		this.mission = mission;
	}
	
}
