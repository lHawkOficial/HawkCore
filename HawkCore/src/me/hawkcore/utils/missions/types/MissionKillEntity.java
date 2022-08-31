package me.hawkcore.utils.missions.types;

import org.bukkit.Bukkit;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;

import lombok.Getter;
import me.hawkcore.Core;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.missions.objects.Mission;
import me.hawkcore.utils.missions.objects.MissionPlayer;
import me.hawkcore.utils.missions.types.utils.MissionObjective;

@Getter
public class MissionKillEntity extends MissionObjective {

	private final EntityType type;
	
	public MissionKillEntity(Mission mission, int max, EntityType type) {
		super(mission, max);
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
		Task.run(()-> mission.getObjective().setMaxValue(max));
		this.type = type;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void event(EntityDeathEvent e) {
		Entity entity = e.getEntity();
		Player p = e.getEntity().getKiller() instanceof Player ? (Player)e.getEntity().getKiller() : null;
		if (p == null && e.getEntity().getKiller() instanceof Projectile) {
			Projectile tile = (Projectile) e.getEntity().getKiller();
			if (tile.getShooter() instanceof Player) p = (Player) tile.getShooter();
		}
		if (p==null) return;
		Mission m = getMission();
		if (m == null) return;
		Mission mission = m.getCategory().getMissionToComplete();
		if (mission == null) return;
		if (!mission.getObjective().equals(this)) return;
		MissionPlayer mp = MissionPlayer.check(p);
		if (!mission.getPlayer().equals(mp)) return;
		MissionKillEntity objective = (MissionKillEntity) mission.getObjective();
		EntityType type = entity.getType();
		if (type == objective.getType()) {
			objective.setValue(objective.getValue()+1);
			if (objective.isCompleted()) objective.complete();
		}
	}

}
