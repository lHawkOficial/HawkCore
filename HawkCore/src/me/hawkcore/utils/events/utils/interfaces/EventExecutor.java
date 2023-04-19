package me.hawkcore.utils.events.utils.interfaces;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import me.hawkcore.Core;
import me.hawkcore.utils.events.utils.Event;

public interface EventExecutor {

	default void addPlayerToEvent(Player p, Event event) {
		if (p.hasMetadata("event")) p.removeMetadata("event", Core.getInstance());
		p.setMetadata("event", new FixedMetadataValue(Core.getInstance(), event));
	}
	
	default void removePlayerFromEvent(Player p, Event event) {
		if (p.hasMetadata("event")) p.removeMetadata("event", Core.getInstance());
	}
	
	boolean containsPlayerOnEvent(Player p, Event event);
	void addPlayerToEspectator(Player p, Event event);
	void removePlayerFromEspectator(Player p, Event event);
	void start();
	void closed();
	void forceStart();
	void warning();
	void stop();
	void finish();
	
}
