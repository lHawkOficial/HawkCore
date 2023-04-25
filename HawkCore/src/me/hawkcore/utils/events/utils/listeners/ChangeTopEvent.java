package me.hawkcore.utils.events.utils.listeners;

import lombok.Getter;
import me.hawkcore.utils.listenercreator.Listener;

@Getter
public class ChangeTopEvent extends Listener {

	private String top;
	
	public ChangeTopEvent(String top) {
		this.top = top;
	}
	
}
