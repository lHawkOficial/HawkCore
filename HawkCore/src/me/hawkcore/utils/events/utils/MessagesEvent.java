package me.hawkcore.utils.events.utils;

import lombok.Getter;
import lombok.Setter;
import me.hawkcore.Core;

@Getter
@Setter
public class MessagesEvent {

	private Event event;
	private String queueMessage;
	
	public MessagesEvent(Event event) {
		queueMessage = Core.getInstance().getTag() + " §eVoce esta na fila para teleportar! §6Nº{numero}§e.";
	}
	
}
