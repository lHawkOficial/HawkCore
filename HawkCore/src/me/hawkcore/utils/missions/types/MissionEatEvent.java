package me.hawkcore.utils.missions.types;



import org.bukkit.Bukkit;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import lombok.Getter;
import me.hawkcore.Core;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.missions.objects.Mission;
import me.hawkcore.utils.missions.types.utils.MissionObjective;
import me.hawkcore.utils.missions.types.utils.MissionVerify;

@Getter
public class MissionEatEvent extends MissionObjective {
	
	public MissionEatEvent(Mission mission, int max) {
		super(mission, max);
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
		Task.run(()-> mission.getObjective().setMaxValue(max));
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void event(FoodLevelChangeEvent e) {
		if (e.isCancelled()) return;
		if (!(e.getEntity() instanceof Player)) return;
		Player p = (Player) e.getEntity();
		Mission mission = getMission();
		if (!new MissionVerify(p, getMission()).queue()) return;
		MissionEatEvent objective = (MissionEatEvent) mission.getObjective();
		if (!(e.getFoodLevel() - p.getFoodLevel() > 0)) return;
		objective.setValue(objective.getValue()+1);
		if (objective.isCompleted()) objective.complete();
	}
	
}
