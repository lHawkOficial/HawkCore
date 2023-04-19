package me.hawkcore.utils.events.utils.interfaces;

import org.bukkit.entity.Player;

import me.HClan.Objects.Clan;

public interface EventExecutor {

	void addPlayerToEvent(Player p);
	void removePlayerFromEvent(Player p);
	boolean containsPlayerOnEvent(Player p);
	boolean containsClanOnEvent(Clan clan);
	void addPlayerToEspectator(Player p);
	void removePlayerFromEspectator(Player p);
	void start();
	void stop();
	void forceStart();
	void warning();
	void closed();
	
}
