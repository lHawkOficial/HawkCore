package me.hawkcore.utils.events.events.parkour.utils;

import java.util.List;


import lombok.Getter;
import me.hawkcore.utils.events.events.parkour.Parkour;
import me.hawkcore.utils.events.utils.Event;
import me.hawkcore.utils.events.utils.MessagesEvent;

@Getter
public class MensagensParkour extends MessagesEvent {
	
	private List<String> open,
	stop,
	finish,
	commands_adm,
	commands_player,
	noPermission;
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
	checkPointSet;
	
	public MensagensParkour(Event event) {
		super(event);
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
	
	public static MensagensParkour get() {
		return (MensagensParkour) Parkour.get().getMessages();
	}
	
}
