package me.hawkcore.utils.missions.types;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.Setter;
import me.hawkcore.Core;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.items.Item;
import me.hawkcore.utils.missions.objects.Mission;
import me.hawkcore.utils.missions.types.utils.MissionObjective;
import me.hawkcore.utils.missions.types.utils.MissionVerify;

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
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void event(BlockBreakEvent e) {
		if (e.isCancelled()) return;
		Player p = e.getPlayer();
		Mission mission = getMission();
		if (!new MissionVerify(p, getMission()).queue()) return;
		MissionBreakBlock objective = (MissionBreakBlock) mission.getObjective();
		if (!Item.isSimilarToBlock(objective.getItem(), e.getBlock())) return;
		objective.setValue(objective.getValue()+1);
		if (objective.isCompleted()) objective.complete();
	}
	
}
