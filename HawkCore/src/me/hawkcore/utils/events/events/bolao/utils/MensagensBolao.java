package me.hawkcore.utils.events.events.bolao.utils;

import java.util.List;

import lombok.Getter;
import me.hawkcore.utils.events.events.bolao.Bolao;
import me.hawkcore.utils.events.utils.Event;
import me.hawkcore.utils.events.utils.MessagesEvent;

@Getter
public class MensagensBolao extends MessagesEvent {
	
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
	eventJoin;
	
	public MensagensBolao(Event event) {
		super(event);
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
	
	public static MensagensBolao get() {
		return (MensagensBolao) Bolao.get().getMessages();
	}
	
}
