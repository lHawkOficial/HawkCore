package me.hawkcore.utils.events.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfigEvent {
	
	private Event event;
	private int tickQueue = 5,
	autoStartTime,
	minPlayers,
	maxPlayers;
	private boolean autoStart;
	
	public ConfigEvent(Event event) {
		this.event = event;
	}
	
}
