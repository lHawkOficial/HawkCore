package me.hawkcore.utils.listenercreator;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;

public class Listener extends Event implements Cancellable {

private static final HandlerList HANDLERS_LIST = new HandlerList();
	
	@Setter
	private boolean cancel = false;
	@Setter
	@Getter
	private boolean clickSound = false;
	
	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean b) {
		cancel = b;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}
	
}
