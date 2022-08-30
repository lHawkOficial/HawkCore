package me.hawkcore.utils.missions.types;

import org.bukkit.entity.EntityType;


import lombok.Getter;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.missions.objects.Mission;
import me.hawkcore.utils.missions.types.utils.MissionObjective;

@Getter
public class MissionKillEntity extends MissionObjective {

	private final EntityType type;
	
	public MissionKillEntity(Mission mission, int max, EntityType type) {
		super(mission, max);
		Task.run(()-> mission.getObjective().setMaxValue(max));
		this.type = type;
	}
	
	@Override
	public String progress() {
		int percent = (int) (getValue() * 100 / getMaxValue());
		return (percent > 100 ? 100 : percent) + "%";
	}
	
	@Override
	public boolean isCompleted() {
		return getValue() >= getMaxValue();
	}

}
