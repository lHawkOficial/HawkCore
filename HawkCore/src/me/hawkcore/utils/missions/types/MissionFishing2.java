package me.hawkcore.utils.missions.types;

import org.bukkit.Bukkit;



import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import me.hawkcore.Core;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.items.Item;
import me.hawkcore.utils.missions.objects.Mission;
import me.hawkcore.utils.missions.objects.MissionPlayer;
import me.hawkcore.utils.missions.types.utils.MissionObjective;

@Getter
public class MissionFishing2 extends MissionObjective {
	
	private final ItemStack item;
	
	public MissionFishing2(Mission mission, int max, ItemStack item) {
		super(mission, max);
		this.item = item;
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
		Task.run(()-> mission.getObjective().setMaxValue(max));
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void event(PlayerFishEvent e) {
		if (e.isCancelled()) return;
		if (e.getState() != State.CAUGHT_FISH) return;
		Player p = e.getPlayer();
		Mission m = getMission();
		if (m == null) return;
		Mission mission = m.getCategory().getMissionToComplete();
		if (mission == null) return;
		if (!mission.getObjective().equals(this)) return;
		MissionPlayer mp = MissionPlayer.check(p);
		if (!mission.getPlayer().equals(mp)) return;
		if (!Item.isSimilar(item, ((org.bukkit.entity.Item)e.getCaught()).getItemStack())) return;
		MissionFishing2 objective = (MissionFishing2) mission.getObjective();
		objective.setValue(objective.getValue()+1);
		if (objective.isCompleted()) objective.complete();
	}

}
