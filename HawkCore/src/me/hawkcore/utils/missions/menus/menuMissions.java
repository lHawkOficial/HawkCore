package me.hawkcore.utils.missions.menus;

import me.hawkcore.utils.missions.objects.ConfigMission;

public class menuMissions {

	public menuMissions() {
		
		
	}
	
	public static menuMissions get() {
		return ConfigMission.get().getMenumissions();
	}
	
}
