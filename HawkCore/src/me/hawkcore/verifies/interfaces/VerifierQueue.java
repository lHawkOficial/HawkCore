package me.hawkcore.verifies.interfaces;

import org.bukkit.Bukkit;

import me.hawkcore.verifies.query.results.VerifierResult;

public interface VerifierQueue {

	default VerifierResult queue(String pluginName) {
		return Bukkit.getPluginManager().getPlugin(pluginName) == null ? VerifierResult.ERROR : VerifierResult.CONNECTED;
	}

	boolean queue();
	
}
