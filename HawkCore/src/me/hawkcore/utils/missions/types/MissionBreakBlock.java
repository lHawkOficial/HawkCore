package me.hawkcore.utils.missions.types;

import org.bukkit.Bukkit;


import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.Setter;
import me.hawkcore.Core;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.missions.objects.Mission;
import me.hawkcore.utils.missions.objects.MissionPlayer;
import me.hawkcore.utils.missions.types.utils.MissionObjective;

@Getter
@Setter
public class MissionBreakBlock extends MissionObjective {
	
	private final ItemStack item;
	
	public MissionBreakBlock(Mission mission, int max, ItemStack item) {
		super(mission, max);
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
		Task.run(()-> mission.getObjective().setMaxValue(max));
		this.item = item;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOWEST)
	public void event(BlockBreakEvent e) {
		if (e.isCancelled()) return;
		Mission m = getMission();
		if (m == null) return;
		Mission mission = m.getCategory().getMissionToComplete();
		if (mission == null) return;
		if (!mission.getObjective().equals(this)) return;
		MissionPlayer mp = MissionPlayer.check(e.getPlayer());
		if (!mission.getPlayer().equals(mp)) return;
		MissionBreakBlock objective = (MissionBreakBlock) mission.getObjective();
		if (e.getBlock().getTypeId() == objective.getItem().getTypeId() && e.getBlock().getData() == objective.getItem().getData().getData()) {
			objective.setValue(objective.getValue()+1);
			if (objective.isCompleted()) objective.complete();
		}
	}
	
}
