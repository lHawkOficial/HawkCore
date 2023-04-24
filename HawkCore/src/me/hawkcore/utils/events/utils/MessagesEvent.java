package me.hawkcore.utils.events.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import lombok.Getter;
import lombok.Setter;
import me.hawkcore.Core;

@Getter
@Setter
public class MessagesEvent {

	private Event event;
	private String queueMessage;
	
	public MessagesEvent(Event event) {
		this.event = event;
		queueMessage = Core.getInstance().getTag() + " §eVoce esta na fila para teleportar! §6Nº{numero}§e.";
	}
	
	public String replace(String msg) {
		FileConfiguration config = getEvent().getConfig();
		return config.getString("Mensagens." + msg).replace("&", "§").replace("{tag}", event.getTag());
	}
	
	public List<String> replaceList(String msg) {
		FileConfiguration config = getEvent().getConfig();
		List<String> list = new ArrayList<>(config.getStringList("Mensagens."+msg));
		list.replaceAll(l -> l.replace("&", "§").replace("{tag}", event.getTag()));
		return list;
	}
	
}
