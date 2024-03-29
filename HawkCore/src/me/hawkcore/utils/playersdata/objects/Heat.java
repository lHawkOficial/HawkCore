package me.hawkcore.utils.playersdata.objects;

import java.util.ArrayList;




import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import lombok.Getter;
import lombok.Setter;
import me.HMasterTesouros.Objects.TesouroPlayer;
import me.hawkcore.Core;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.API;
import me.hawkcore.utils.ConfigGeral;
import me.hawkcore.utils.itemcreator.ItemCreator;
import me.hawkcore.utils.items.Item;
import me.hawkcore.utils.playersdata.objects.utils.ThirstHeatUtils;
import me.hawkcore.verifies.PluginVerifier;

@Getter
public class Heat extends ThirstHeatUtils {

	private List<World> worlds = new ArrayList<>();
	private double blindnessValue, damage;
	private HashMap<ItemStack, Double> equipments = new HashMap<>();
	private boolean active = ConfigGeral.get().getHeat();
	private String bar;
	private long timeBlind, timeFire;
	@Setter
	private boolean cancelled = false;
	
	public Heat(PlayerData pd) {
		super(pd);
		ConfigurationSection section = Core.getInstance().getConfig().getConfigurationSection("Config.heatConfig");
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
		damage = section.getDouble("damage");
		blindnessValue = section.getDouble("blindnessValue");
		setMinValue(getValue());
		bar = section.getString("bar");
		for(String line : section.getStringList("items")) {
			try {
				String[] args = line.split(":");
				ItemCreator ic = ItemCreator.get().getItem(args[0]);
				if (ic == null) continue;
				double value = Double.valueOf(args[1]);
				equipments.put(ic.getItem().clone(), value);
			} catch (Exception e) {
				continue;
			}
		}
	}
	
	public void tickUpdate() {
		PlayerData pd = getPlayerData();
		Player p = pd.getPlayer();
		if (!active) return;
		if (p == null || !p.isOnline() || p.isDead() || !p.isValid() || !worlds.contains(p.getWorld())) return;
		if (p.getGameMode() != GameMode.SURVIVAL) return;
		if (cancelled) return;
		if (new PluginVerifier("H_MasterTesouros", "").queue()) {
			TesouroPlayer tp = TesouroPlayer.check(p);
			if (API.get().containsRegion(p.getLocation()) && tp.getSpawnSelecionado() == null) return;
		}
		double value = getValue();
		if (value >= getBlindnessValue() && System.currentTimeMillis() - timeBlind >= 1500) {
			timeBlind = System.currentTimeMillis();
			Task.run(()-> p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 1)));
		}
		if (value >= getMaxValue() && System.currentTimeMillis() - timeFire >= 300) {
			timeFire = System.currentTimeMillis();
			Task.run(()-> {
				p.setFireTicks(20);
				p.damage(damage);
			});
		}
		
		if (!isDecrease(p)) add(getIncrease());
		else remove(getDecreaseValue(p));
		
		updateActionBar();
	}
	
	private double getDecreaseValue(Player p) {
		Block block = p.getLocation().getBlock();
		double value = 0;
		if (block.getType().toString().toLowerCase().contains("water")) value += getDecrease();
		for(ItemStack item : equipments.keySet()) {
			if (Item.isPlayerEquiped(p, item)) value += equipments.get(item);
		}
		return value;
	}
	
	private boolean isDecrease(Player p) {
		return getDecreaseValue(p) > 0;
	}
	
}
