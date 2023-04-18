package me.hawkcore.utils.missions.types;



import org.bukkit.Bukkit;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import me.hawkcore.Core;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.items.Item;
import me.hawkcore.utils.missions.objects.Mission;
import me.hawkcore.utils.missions.types.utils.MissionObjective;
import me.hawkcore.utils.missions.types.utils.MissionVerify;

@Getter
public class MissionEnchantItem2 extends MissionObjective {
	
	private final ItemStack item;
	
	public MissionEnchantItem2(Mission mission, int max, ItemStack item) {
		super(mission, max);
		this.item = item;
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
		Task.run(()-> mission.getObjective().setMaxValue(max));
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void event(EnchantItemEvent e) {
		if (e.isCancelled()) return;
		Player p = e.getEnchanter();
		Mission mission = getMission();
		if (!new MissionVerify(p, getMission()).queue()) return;
		MissionEnchantItem2 objective = (MissionEnchantItem2) mission.getObjective();
		if (!Item.isSimilarMaterial(item, e.getItem().clone())) return;
		objective.setValue(objective.getValue()+1);
		if (objective.isCompleted()) objective.complete();
	}
	
}
