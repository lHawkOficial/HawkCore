package me.hawkcore.utils.missions.types;



import lombok.Getter;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.missions.objects.Mission;
import me.hawkcore.utils.missions.types.utils.MissionObjective;

@Getter
public class MissionEnchantItem extends MissionObjective {
	
	public MissionEnchantItem(Mission mission, int max) {
		super(mission, max);
		Task.run(()-> mission.getObjective().setMaxValue(max));
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
