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
	finish;
	private String alreadyStart,
	alreadyStopped,
	started,
	stopped;
	
	public MensagensBolao(Event event) {
		super(event);
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
