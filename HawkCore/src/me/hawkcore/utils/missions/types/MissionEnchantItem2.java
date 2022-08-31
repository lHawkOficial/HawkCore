package me.hawkcore.utils.missions.types;



import org.bukkit.Bukkit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import me.hawkcore.Core;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.items.Item;
import me.hawkcore.utils.missions.objects.Mission;
import me.hawkcore.utils.missions.objects.MissionPlayer;
import me.hawkcore.utils.missions.types.utils.MissionObjective;

@Getter
public class MissionEnchantItem2 extends MissionObjective {
	
	private final ItemStack item;
	
	public MissionEnchantItem2(Mission mission, int max, ItemStack item) {
		super(mission, max);
		this.item = item;
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
		Task.run(()-> mission.getObjective().setMaxValue(max));
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void event(EnchantItemEvent e) {
		if (e.isCancelled()) return;
		Mission m = getMission();
		if (m == null) return;
		Mission mission = m.getCategory().getMissionToComplete();
		if (mission == null) return;
		if (!mission.getObjective().equals(this)) return;
		MissionPlayer mp = MissionPlayer.check(e.getEnchanter());
		if (!mission.getPlayer().equals(mp)) return;
		MissionEnchantItem2 objective = (MissionEnchantItem2) mission.getObjective();
		if (!Item.isSimilar(item, e.getItem())) return;
		objective.setValue(objective.getValue()+1);
		if (objective.isCompleted()) objective.complete();
	}
	
}
