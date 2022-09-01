package me.hawkcore.utils.playersdata.managers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.Getter;
import me.hawkcore.Core;
import me.hawkcore.utils.playersdata.objects.PlayerData;

@Getter
public class ManagerData {

	private List<PlayerData> players = new ArrayList<>();
	
	public static ManagerData get() {
		return Core.getInstance().getManagerdata();
	}
	
	public PlayerData getPlayer(String name) {
		Iterator<PlayerData> it = players.iterator();
		while(it.hasNext()) {
			PlayerData pd = it.next();
			if (pd.getName().equalsIgnoreCase(name)) return pd;
		}
		return null;
	}
	
}
