package me.hawkcore.utils.missions.types;

import org.bukkit.Bukkit;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;


import lombok.Getter;
import me.hawkcore.Core;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.items.Item;
import me.hawkcore.utils.missions.objects.Mission;
import me.hawkcore.utils.missions.types.utils.MissionObjective;
import me.hawkcore.utils.missions.types.utils.MissionVerify;

@Getter
public class MissionPickItem2 extends MissionObjective {

	private final ItemStack item;
	
	public MissionPickItem2(Mission mission, int max, ItemStack item) {
		super(mission, max);
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
		Task.run(()-> mission.getObjective().setMaxValue(max));
		this.item = item;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void event(PlayerPickupItemEvent e) {
		if (e.isCancelled()) return;
		Player p = e.getPlayer();
		Mission mission = getMission();
		if (!new MissionVerify(p, getMission()).queue()) return;
		MissionPickItem2 objective = (MissionPickItem2) mission.getObjective();
		ItemStack item = e.getItem().getItemStack().clone();
		if (!Item.isSimilar(item, e.getItem().getItemStack())) return;
		objective.setValue(objective.getValue()+item.getAmount());
		if (objective.isCompleted()) objective.complete();
	}

}
