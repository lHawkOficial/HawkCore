package me.hawkcore.utils.missions.types.utils;

import lombok.Getter;
import lombok.Setter;
import me.hawkcore.utils.missions.objects.Mission;

@Getter
@Setter
public class MissionQueue {

	private Mission mission;
	private int id;
	
	public MissionQueue(Mission mission, int id) {
		this.mission = mission;
		this.id = id;
	}
	
	public boolean isValid() {
		return mission != null || id == -1;
	}
	
	public boolean isEquals(Mission mission, int id) {
		return this.id == id;
	}
	
}
