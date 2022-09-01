package me.hawkcore.utils.missions.types.utils;

import lombok.Getter;
import lombok.Setter;
import me.hawkcore.utils.missions.objects.Mission;

@Getter
@Setter
public class MissionQueue {

	private Mission mission;
	
	public MissionQueue(Mission mission) {
		this.mission = mission;
	}
	
}
