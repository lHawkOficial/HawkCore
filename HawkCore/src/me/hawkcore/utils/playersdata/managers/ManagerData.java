package me.hawkcore.utils.playersdata.managers;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;


import lombok.Getter;
import me.hawkcore.Core;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.ConfigGeral;
import me.hawkcore.utils.playersdata.objects.PlayerData;

@Getter
public class ManagerData {

	private List<PlayerData> players = new ArrayList<>();
	private Task task;
	
	public ManagerData() {
		if (!ConfigGeral.get().getHeat() && !ConfigGeral.get().getThirst()) return;
		task = new Task(()-> {
			if (players.isEmpty()) return;
			for (int i = 0; i < players.size(); i++) {
				PlayerData pd = players.get(i);
				pd.getThirst().tickUpdate();
				pd.getHeat().tickUpdate();
			}
		}).run(Core.getInstance().getConfig().getInt("Config.tickRate"));
	}
	
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
