package me.hawkcore.utils.missions.types;

import lombok.Getter;
import me.hawkcore.utils.missions.Mission;

@Getter
public class MissionObjective {
	
	private Mission mission;
	
	public MissionObjective(Mission mission) {
		this.mission = mission;
	}
	
}
