package me.hawkcore.utils.events.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import me.hawkcore.Core;
import me.hawkcore.tasks.Task;

@Getter
@Setter
public class RankingEvent {

	private Event event;
	private String top;
	private HashMap<String, Integer> tops = new HashMap<>();
	
	public RankingEvent(Event event) {
		this.event = event;
	}
	
	private boolean updating = false;
	public void update() {
		if (updating) return;
		updating = true;
		Task.runAsync(()->{
			HashMap<String, Integer> tops = new HashMap<>(this.tops);
			HashMap<String, Integer> map = new HashMap<>();
			long time = System.currentTimeMillis();
			while(!tops.isEmpty()) {
				String name = Collections.max(tops.entrySet(), Map.Entry.comparingByValue()).getKey();
				map.put(name, tops.get(name));
				tops.remove(name);
			}
			this.tops = tops;
			this.top = (String) tops.keySet().toArray()[0];
			Core.getInstance().sendConsole(me.hawkcore.Core.getInstance().getTag() + " &7HawkCore Atualizou o ranking do evento &e"+event.getName() + " &7com " + tops.keySet().size() + " &7jogadores em um total de &e" + (System.currentTimeMillis()-time) + "ms&7!");
			updating = false;
		});
	}
	
}
