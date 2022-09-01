package me.hawkcore.utils.missions.types;



import org.bukkit.Bukkit;



import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;

import lombok.Getter;
import me.hawkcore.Core;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.missions.objects.Mission;
import me.hawkcore.utils.missions.types.utils.MissionObjective;
import me.hawkcore.utils.missions.types.utils.MissionVerify;

@Getter
public class MissionDeath extends MissionObjective {
	
	public MissionDeath(Mission mission, int max) {
		super(mission, max);
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
		Task.run(()-> mission.getObjective().setMaxValue(max));
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void event(PlayerDeathEvent e) {
		if (!(e.getEntity() instanceof Player)) return;
		Player p = e.getEntity();
		Mission mission = getMission();
		if (!new MissionVerify(p, getMission()).queue()) return;
		MissionDeath objective = (MissionDeath) mission.getObjective();
		objective.setValue(objective.getValue()+1);
		if (objective.isCompleted()) objective.complete();
	}
	
}
