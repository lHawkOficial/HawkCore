package me.hawkcore.utils.events;

import java.awt.Color;
import java.util.ArrayList;


import java.util.List;

import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import me.hawkcore.Core;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.ConfigGeral;
import me.hawkcore.utils.events.utils.Event;
import me.hawkcore.utils.events.utils.enums.EventStatus;
import me.hawkcore.utils.events.utils.interfaces.EventExecutor;
import me.hawkcore.verifies.PluginVerifier;
import me.hbot.objects.bot.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

@Getter
public class EventManager {

	private List<Event> events = new ArrayList<>();
	private Task task;
	
	public EventManager() {
		task = new Task(()->{
			
			if (events.isEmpty()) return;
			if (hasEventPlaying()) return;
			for (int i = 0; i < events.size(); i++) {
				Event event = events.get(i);
				if (!event.getConfigEvent().isAutoStart()) continue;
				if (!event.isEnabled()) continue;
				if (hasEventPlaying()) break;
				if (event.getTimeLast()-ConfigGeral.get().getTime_warn_events()*60 <= 0 && !event.isWarn() && !ConfigGeral.get().getChannel_events().isEmpty()) {
					event.setWarn(true);
					if (new PluginVerifier("H_Bot", "").queue()) {
						Bot bot = Bot.get();
						TextChannel channel = bot.getGuild().getTextChannelById(ConfigGeral.get().getChannel_events());
						if (channel != null && !ConfigGeral.get().getWarn_text().isEmpty()) {
							EmbedBuilder builder = new EmbedBuilder();
							builder.setTitle(ConfigGeral.get().getWarn_title().replace("{evento}", event.getName()));
							builder.setColor(Color.YELLOW.darker());
							ConfigGeral.get().getWarn_text().forEach(msg -> builder.appendDescription(msg.replace("{evento}", event.getName()).replace("{tempo}", event.getTimeLastFormatted())));
							builder.setFooter("Atenciosamente\nHyzard entertainment");
							channel.sendMessageEmbeds(builder.build()).queue();
						}
					}
					continue;
				}
				if (event.getTimeLast() <= 0) {
					event.setLastStart(System.currentTimeMillis());
					event.setWarn(false);
					((EventExecutor)event).start();
					if (!ConfigGeral.get().getWarn_start().isEmpty()) {
						if (new PluginVerifier("H_Bot", "").queue()) {
							Bot bot = Bot.get();
							TextChannel channel = bot.getGuild().getTextChannelById(ConfigGeral.get().getChannel_events());
							if (channel != null && !ConfigGeral.get().getWarn_text().isEmpty()) {
								EmbedBuilder builder = new EmbedBuilder();
								builder.setTitle(ConfigGeral.get().getWarn_title().replace("{evento}", event.getName()));
								builder.setColor(Color.YELLOW.darker());
								ConfigGeral.get().getWarn_start().forEach(msg -> builder.appendDescription(msg.replace("{evento}", event.getName()).replace("{tempo}", event.getTimeLastFormatted())));
								builder.setFooter("Atenciosamente\nHyzard entertainment");
								channel.sendMessageEmbeds(builder.build()).queue();
							}
						}
					}
				}
			}
			
		});
	}
	
	public Event getEvent(String name) {
		for(Event event : events) {
			if (event.getName().equalsIgnoreCase(name)) return event;
		}
		return null;
	}
	
	public boolean hasEventPlaying() {
		for(Event event : events) {
			if (event.getEventStatus() != EventStatus.STOPPED) return true;
		}
		return false;
	}
	
	public Event getEvent(ItemStack icon) {
		for(Event event : events) {
			if (event.getIcon().build().isSimilar(icon)) return event;
		}
		return null;
	}
	
	public static EventManager get() {
		return Core.getInstance().getEventmanager();
	}
	
}
