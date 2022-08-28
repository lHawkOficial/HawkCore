package me.hawkcore.utils.missions;

import lombok.Getter;
import me.hawkcore.utils.items.Item;
import me.hawkcore.utils.missions.objects.MissionCategory;
import me.hawkcore.utils.missions.types.MissionObjective;

@Getter
public class Mission {
	
	private String name, description;
	private Item icon;
	private MissionObjective ojective;
	private MissionPlayer player;
	private MissionCategory category;

	public Mission(MissionCategory category, String name, String description, MissionObjective objective) {
		this.name = name;
		this.description = description;
		this.ojective = objective;
		this.player = category.getPlayer();
		this.category = category;
		this.category.getMissions().add(this);
	}
	
	public static ManagerMissions get() {
		return ManagerMissions.get();
	}
	
}
