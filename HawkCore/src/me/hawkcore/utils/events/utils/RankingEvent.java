package me.hawkcore.utils.events.utils;

import java.util.Collections;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Bukkit;

import lombok.Getter;
import lombok.Setter;
import me.hawkcore.Core;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.events.utils.listeners.ChangeTopEvent;

@Getter
@Setter
public class RankingEvent {

	private Event event;
	private String top;
	private LinkedHashMap<String, Integer> tops = new LinkedHashMap<>();
	
	public RankingEvent(Event event) {
		this.event = event;
	}
	
	private boolean updating = false;
	public void update() {
		if (updating) return;
		updating = true;
		Task.runAsync(()->{
			LinkedHashMap<String, Integer> tops = new LinkedHashMap<>(this.tops);
			LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
			long time = System.currentTimeMillis();
			while(!tops.isEmpty()) {
				String name = Collections.max(tops.entrySet(), Map.Entry.comparingByValue()).getKey();
				int total = tops.get(name);
				map.put(name, total);
				tops.remove(name);
			}
			if(top!=null) {
				String antigoTop = this.top;
				Task.run(()->{
					if (!antigoTop.equals(top)) {
						Bukkit.getPluginManager().callEvent(new ChangeTopEvent(top));
					}
				});
			}
			this.tops = map;
			this.top = map.keySet().isEmpty() ? null : (String) map.keySet().toArray()[0];
			Core.getInstance().sendConsole(me.hawkcore.Core.getInstance().getTag() + " &7HawkCore Atualizou o ranking do evento &e"+event.getName() + " &7com " + map.keySet().size() + " &7jogadores em um total de &e" + (System.currentTimeMillis()-time) + "ms&7!");
			updating = false;
		});
	}
	
	public int getWinsPlayer(String name) {
		return tops.containsKey(name) ? tops.get(name) : 0;
	}
	
	public int getRankingPlayer(String name) {
		if (tops.keySet().isEmpty()) return 1;
		for (int i = 0; i < tops.keySet().size(); i++) {
			int rank = i+1;
			String nome = (String) tops.keySet().toArray()[i];
			if (nome.equalsIgnoreCase(name)) return rank;
		}
		return tops.keySet().size();
	}
	
}
