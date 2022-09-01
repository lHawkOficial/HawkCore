package me.hawkcore.utils.missions.types;



import org.bukkit.Bukkit;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import lombok.Getter;
import me.hawkcore.Core;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.missions.listeners.plugin.PlayerPlantEvent;
import me.hawkcore.utils.missions.objects.Mission;
import me.hawkcore.utils.missions.types.utils.MissionObjective;
import me.hawkcore.utils.missions.types.utils.MissionVerify;

@Getter
public class MissionPlant extends MissionObjective {
	
	public MissionPlant(Mission mission, int max) {
		super(mission, max);
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
		Task.run(()-> mission.getObjective().setMaxValue(max));
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void event(PlayerPlantEvent e) {
		if (e.isCancelled()) return;
		Player p = e.getPlayer();
		Mission mission = getMission();
		if (!new MissionVerify(p, getMission()).queue()) return;
		MissionPlant objective = (MissionPlant) mission.getObjective();
		objective.setValue(objective.getValue()+1);
		if (objective.isCompleted()) objective.complete();
	}
	
}
