package me.hawkcore.utils.missions.types;


import org.bukkit.Bukkit;


import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

import me.hawkcore.Core;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.locations.Distance;
import me.hawkcore.utils.missions.objects.Mission;
import me.hawkcore.utils.missions.types.utils.MissionObjective;
import me.hawkcore.utils.missions.types.utils.MissionVerify;

public class MissionMoveDistance extends MissionObjective {
	
	public MissionMoveDistance(Mission mission, double max) {
		super(mission, max);
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
		Task.run(()-> mission.getObjective().setMaxValue(max));
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void event(PlayerMoveEvent e) {
		if (e.isCancelled()) return;
		Player p = e.getPlayer();
		Location loc = e.getTo().clone();
		Location loc1 = e.getFrom().clone();
		Task.runAsync(()-> {
			Mission mission = getMission();
			if (!new MissionVerify(p, getMission()).queue()) return;
			MissionMoveDistance objective = (MissionMoveDistance) mission.getObjective();
			Distance distance = new Distance(loc, loc1);
			double value = distance.value();
			if (value == 0) return;
			objective.setValue(objective.getValue()+value);
			if (objective.isCompleted()) objective.complete();
		});
	}
	
}
