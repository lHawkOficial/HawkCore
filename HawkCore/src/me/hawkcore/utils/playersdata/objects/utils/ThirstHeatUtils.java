package me.hawkcore.utils.playersdata.objects.utils;


import org.bukkit.entity.Player;



import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import me.hawkcore.utils.API;
import me.hawkcore.utils.ConfigGeral;
import me.hawkcore.utils.playersdata.objects.PlayerData;
import me.hawkcore.utils.playersdata.objects.Thirst;

@Getter
@Setter
public class ThirstHeatUtils {
	
	private double increase, decrease, maxValue, value, damage, minValue;
	private final PlayerData playerData;
	private API api;
	private boolean actionbar = ConfigGeral.get().getActionBarActive();
	private String line = ConfigGeral.get().getActionBar();
	
	public ThirstHeatUtils(PlayerData pd) {
		this.playerData = pd;
		this.api = API.get();
	}
	
	public void updateActionBar() {
		if (!actionbar) return;
		PlayerData pd = playerData;
		Player p = pd.getPlayer();
		getApi().sendActionBarMessage(pd.getPlayer(), PlaceholderAPI.setPlaceholders(p, line));
	}
	
	public void add(double value) {
		this.value+=value;
		if (this.value>maxValue) this.value = maxValue;
	}
	
	public void remove(double value) {
		this.value-=value;
		if (this.value<minValue) this.value = minValue;
	}
	
	public void reset() {
		if (this instanceof Thirst) setValue(maxValue);
		else setValue(minValue);
	}
	
}
