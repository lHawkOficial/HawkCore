package me.hawkcore.utils.missions.listeners.plugin;

import org.bukkit.event.Cancellable;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import me.hawkcore.utils.missions.objects.Mission;
import me.hawkcore.utils.missions.objects.MissionPlayer;
import me.hawkcore.utils.missions.types.utils.MissionObjective;

@Getter
public class MissionCompleteEvent extends Event implements Cancellable  {

	private static final HandlerList HANDLERS_LIST = new HandlerList();
	
	private MissionPlayer player;
	private Mission missionCompleted;
	private MissionObjective objective;
	private boolean cancel = false;
	
	public MissionCompleteEvent(MissionPlayer player, Mission missionCompleted, MissionObjective objective) {
		this.player = player;
		this.missionCompleted = missionCompleted;
		this.objective = objective;
	}
	
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
