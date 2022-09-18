package me.hawkcore.utils.missions.types;

import org.bukkit.Bukkit;



import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerExpChangeEvent;


import lombok.Getter;
import me.hawkcore.Core;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.missions.objects.Mission;
import me.hawkcore.utils.missions.types.utils.MissionObjective;
import me.hawkcore.utils.missions.types.utils.MissionVerify;

@Getter
public class MissionExperience extends MissionObjective {
	
	public MissionExperience(Mission mission, int max) {
		super(mission, max);
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
		Task.run(()-> mission.getObjective().setMaxValue(max));
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void event(PlayerExpChangeEvent e) {
		if (e.getAmount() == 0) return;
		Player p = e.getPlayer();
		Mission mission = getMission();
		if (!new MissionVerify(p, getMission()).queue()) return;
		MissionExperience objective = (MissionExperience) mission.getObjective();
		objective.setValue(objective.getValue()+e.getAmount());
		if (objective.isCompleted()) objective.complete();
	}

}
