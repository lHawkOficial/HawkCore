package me.hawkcore.utils.events;

import java.util.ArrayList;

import java.util.List;


import lombok.Getter;
import me.hawkcore.Core;
import me.hawkcore.utils.events.utils.Event;

@Getter
public class EventManager {

	private List<Event> events = new ArrayList<>();
	
	public Event getEvent(String name) {
		for(Event event : events) {
			if (event.getName().equalsIgnoreCase(name)) return event;
		}
		return null;
	}
	
	public static EventManager get() {
		return Core.getInstance().getEventmanager();
	}
	
}
