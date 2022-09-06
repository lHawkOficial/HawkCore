package me.hawkcore.utils.missions.types;




import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import me.hawkcore.Core;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.items.Item;
import me.hawkcore.utils.missions.listeners.plugin.MissionCollectItemEvent;
import me.hawkcore.utils.missions.menus.menuMissions;
import me.hawkcore.utils.missions.objects.Mission;
import me.hawkcore.utils.missions.types.utils.MissionObjective;
import me.hawkcore.utils.missions.types.utils.MissionVerify;

@Getter
public class MissionCollectItem extends MissionObjective {
	
	private final ItemStack item;
	
	public MissionCollectItem(Mission mission, int max, ItemStack item) {
		super(mission, max);
		this.item = item;
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
		Task.run(()-> mission.getObjective().setMaxValue(max));
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void event(MissionCollectItemEvent e) {
		Player p = e.getMissionPlayer().getPlayer();
		Mission mission = getMission();
		if (!new MissionVerify(p, getMission()).queue()) return;
		if (!Item.containsAmount(p, item, 1)) {
			p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
			return;
		}
		MissionCollectItem objective = (MissionCollectItem) mission.getObjective();
		int amount = Item.getAmount(p, item);
		Item.removeAmount(p, item, amount);
		int rest = (int) (objective.getMaxValue() - (amount+objective.getValue()) < 0 ? (amount+objective.getValue()) - objective.getMaxValue() : 0);
		if (rest>0) {
			ItemStack it = item.clone();
			it.setAmount(1);
			for (int i = 0; i < rest; i++) {
				if (p.getInventory().firstEmpty() != -1) {
					p.getInventory().addItem(it.clone());
				}else {
					p.getWorld().dropItem(p.getLocation(), it.clone());
				}
			}
		}
		p.playSound(p.getLocation(), Sound.LEVEL_UP, 0.5f, 10);
		objective.setValue(objective.getValue()+(rest == 0 ? amount : amount-rest));
		if (objective.isCompleted()) {
			p.closeInventory();
			objective.complete();
		}else {
			menuMissions.get().open(p);
		}
	}

}
