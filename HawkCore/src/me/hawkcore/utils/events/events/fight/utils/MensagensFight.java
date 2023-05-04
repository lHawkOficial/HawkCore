package me.hawkcore.utils.events.events.fight.utils;

import java.util.List;



import lombok.Getter;
import me.hawkcore.utils.events.events.fight.Fight;
import me.hawkcore.utils.events.utils.Event;
import me.hawkcore.utils.events.utils.MessagesEvent;

@Getter
public class MensagensFight extends MessagesEvent {
	
	private List<String> open,
	stop,
	finish,
	commands_adm,
	commands_player,
	noPermission,
	closed,
	versus;
	private String alreadyStart,
	alreadyStopped,
	started,
	stopped,
	eventStopped,
	isOnEvent,
	noMoney,
	eventJoin,
	exit,
	commandBloqued,
	actionbar,
	startSet,
	exitSet,
	lobbySet,
	locationNotFound,
	checkPointSet,
	noConfigured,
	espectatorSet;
	
	public MensagensFight(Event event) {
		super(event);
		versus = replaceList("versus");
		espectatorSet = replace("espectatorSet");
		noConfigured = replace("noConfigured");
		closed = replaceList("closed");
		checkPointSet = replace("checkPointSet");
		locationNotFound = replace("locationNotFound");
		lobbySet = replace("lobbySet");
		exitSet = replace("exitSet");
		startSet = replace("startSet");
		actionbar = replace("actionbar");
		commandBloqued = replace("commandBloqued");
		exit = replace("exit");
		noPermission = replaceList("noPermission");
		commands_player = replaceList("commands_player");
		commands_adm = replaceList("commands_adm");
		eventJoin = replace("eventJoin");
		noMoney = replace("noMoney");
		isOnEvent = replace("isOnEvent");
		eventStopped = replace("eventStopped");
		finish = replaceList("finish");
		stop = replaceList("stop");
		open = replaceList("open");
		stopped = replace("stopped");
		started = replace("started");
		alreadyStopped = replace("alreadyStopped");
		alreadyStart = replace("alreadyStart");
	}
	
	public static MensagensFight get() {
		return (MensagensFight) Fight.get().getMessages();
	}
	
}
