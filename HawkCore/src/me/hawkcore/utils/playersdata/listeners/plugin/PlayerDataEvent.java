package me.hawkcore.utils.playersdata.listeners.plugin;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import me.hawkcore.utils.playersdata.objects.PlayerData;

@Getter
public class PlayerDataEvent extends Event {
	
	private static final HandlerList HANDLERS_LIST = new HandlerList();
	private PlayerData playerData;
	
	public PlayerDataEvent(PlayerData pd) {
		this.playerData = pd;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}
	
}
