package me.hawkcore.utils.playersdata.objects;

import java.util.ArrayList;



import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import lombok.Getter;
import lombok.Setter;
import me.hawkcore.Core;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.ConfigGeral;
import me.hawkcore.utils.boosbar.BossBar;
import me.hawkcore.utils.itemcreator.ItemCreator;
import me.hawkcore.utils.items.Item;
import me.hawkcore.utils.locations.Distance;
import me.hawkcore.utils.playersdata.listeners.plugin.PlayerDataEvent;
import me.hawkcore.utils.playersdata.objects.utils.ThirstHeatUtils;
import me.hawkcore.utils.playersdata.utils.MensagensThirstHeat;

@Getter
public class Thirst extends ThirstHeatUtils {
	
	private List<World> worlds = new ArrayList<>();
	private boolean active = ConfigGeral.get().getThirst();
	private int itemPercentIncrease;
	private double damage;
	private ItemStack item;
	private String bar;
	private long tickDamage = -1,
	timeUseWater = -1;
	@Setter
	private boolean cancelled = false;
	
	public Thirst(PlayerData pd) {
		super(pd);
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
		ConfigurationSection section = Core.getInstance().getConfig().getConfigurationSection("Config.thirstConfig");
		for(String name : section.getStringList("worlds")) {
			try {
				worlds.add(Bukkit.getWorld(name));
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		setMaxValue(section.getDouble("maxValue"));
		setValue(section.getDouble("valueStart"));
		setIncrease(section.getDouble("increase"));
		setDecrease(section.getDouble("decrease"));
		bar = section.getString("bar");
		damage = section.getDouble("damage");
		itemPercentIncrease = section.getInt("item_percent_increase");
		ItemCreator ic = ItemCreator.get().getItem(section.getString("item"));
		if (ic != null) item = ic.getItem().clone();
		setMinValue(0);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void tickUpdate(PlayerDataEvent e) {
		PlayerData pd = e.getPlayerData();
		Player p = pd.getPlayer();
		if (!active) return;
		if (p == null || !p.isOnline() || p.isDead() || !p.isValid() || !worlds.contains(p.getWorld())) return;
		if (p.getGameMode() != GameMode.SURVIVAL) return;
		if (cancelled) return;
		double value = getValue();
		Block block = p.getLocation().getBlock();
		double total = getValue()+getIncrease();
		if (value == 0 && System.currentTimeMillis()-tickDamage >= 500) {
			tickDamage = System.currentTimeMillis();
			Task.run(()->{
				p.damage(getDamage());
				p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 1, false, false));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30, 1, false, false));
			});
		}else if(!(block.getType().toString().toLowerCase().contains("water") && p.isSneaking()) || total >= getMaxValue()) remove(getDecrease());
		else {
			if (System.currentTimeMillis() - timeUseWater >= 300 && total <= getMaxValue()) {
				timeUseWater = System.currentTimeMillis();
				add(getIncrease());
				for(Player all : Bukkit.getOnlinePlayers()) {
					if (!all.getWorld().equals(p.getWorld())) continue;
					Distance distance = new Distance(p.getLocation(), all.getLocation());
					double dvalue = distance.value();
					if (dvalue > 10) continue;
					all.playSound(p.getLocation(), Sound.DRINK, 1, 5);
				}
			}
		}
		updateActionBar();
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void useWater(PlayerItemConsumeEvent e) {
		Player p = e.getPlayer();
		PlayerData pd = PlayerData.check(p);
		Thirst thirst = pd.getThirst();
		if (thirst.getItem() == null) return;
		if (!Item.isSimilar(thirst.getItem(), e.getItem())) return;
		int percent = (int) (thirst.getValue() * 100 / thirst.getMaxValue());
		if (!(percent < (100-thirst.getItemPercentIncrease()))) {
			e.setCancelled(true);
			BossBar.send(p, MensagensThirstHeat.get().getWaterFull(), 3);
			p.updateInventory();
			p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 0.5f);
			return;
		}
		double value = thirst.getMaxValue() * thirst.getItemPercentIncrease() / 100;
		add(value);
		for(Player all : Bukkit.getOnlinePlayers()) {
			if (!all.getWorld().equals(p.getWorld())) continue;
			Distance distance = new Distance(p.getLocation(), all.getLocation());
			double dvalue = distance.value();
			if (dvalue > 10) continue;
			all.playSound(p.getLocation(), Sound.DRINK, 1, 5);
		}
		p.updateInventory();
	}
	
}
