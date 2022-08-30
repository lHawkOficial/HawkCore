package me.hawkcore.utils.missions.types;

import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.Setter;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.missions.objects.Mission;
import me.hawkcore.utils.missions.types.utils.MissionObjective;

@Getter
@Setter
public class MissionBreakBlock extends MissionObjective {
	
	private final ItemStack item;
	
	public MissionBreakBlock(Mission mission, int max, ItemStack item) {
		super(mission, max);
		Task.run(()-> mission.getObjective().setMaxValue(max));
		this.item = item;
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
