package me.hawkcore.utils.playersdata.objects.utils;

import lombok.Getter;
import me.hawkcore.utils.playersdata.objects.PlayerData;

@Getter
public class ThirstHeatUtils {
	
	private final long maxValue, value;
	private final PlayerData playerData;
	
	public ThirstHeatUtils(PlayerData pd, long maxValue, long value) {
		this.maxValue = maxValue;
		this.playerData = pd;
		this.value = value;
	}
	
}
