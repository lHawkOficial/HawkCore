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
import me.hawkcore.utils.missions.objects.MissionPlayer;
import me.hawkcore.utils.missions.types.utils.MissionObjective;

@Getter
public class MissionExperience extends MissionObjective {
	
	public MissionExperience(Mission mission, int max) {
		super(mission, max);
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
		Task.run(()-> mission.getObjective().setMaxValue(max));
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void event(PlayerExpChangeEvent e) {
		if (e.getAmount() == 0) return;
		Player p = e.getPlayer();
		Mission m = getMission();
		if (m == null) return;
		Mission mission = m.getCategory().getMissionToComplete();
		if (mission == null) return;
		if (!mission.getObjective().equals(this)) return;
		MissionPlayer mp = MissionPlayer.check(p);
		if (!mission.getPlayer().equals(mp)) return;
		MissionExperience objective = (MissionExperience) mission.getObjective();
		objective.setValue(objective.getValue()+e.getAmount());
		if (objective.isCompleted()) objective.complete();
	}

}
