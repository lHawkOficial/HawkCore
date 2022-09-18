package me.hawkcore.utils.missions.types;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import me.hawkcore.Core;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.items.Item;
import me.hawkcore.utils.missions.objects.Mission;
import me.hawkcore.utils.missions.types.utils.MissionObjective;
import me.hawkcore.utils.missions.types.utils.MissionVerify;

@Getter
public class MissionDropItem extends MissionObjective {

	private final ItemStack item;
	
	public MissionDropItem(Mission mission, int max, ItemStack item) {
		super(mission, max);
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
		Task.run(()-> mission.getObjective().setMaxValue(max));
		this.item = item;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void event(PlayerDropItemEvent e) {
		if (e.isCancelled()) return;
		Player p = e.getPlayer();
		Mission mission = getMission();
		if (!new MissionVerify(p, getMission()).queue()) return;
		MissionDropItem objective = (MissionDropItem) mission.getObjective();
		ItemStack item = e.getItemDrop().getItemStack().clone();
		if (!Item.isSimilar(item, objective.getItem())) return;
		objective.setValue(objective.getValue()+item.getAmount());
		if (objective.isCompleted()) objective.complete();
	}

}
