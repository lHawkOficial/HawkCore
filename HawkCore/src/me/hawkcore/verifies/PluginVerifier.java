package me.hawkcore.verifies;

import org.bukkit.Bukkit;

import me.hawkcore.verifies.interfaces.VerifierQueue;
import me.hawkcore.verifies.query.results.VerifierResult;

public class PluginVerifier implements VerifierQueue {

	private String pluginName,
	messageError;
	
	public PluginVerifier(String pluginName, String messageError) {
		this.pluginName = pluginName;
		this.messageError = messageError.replace("&", "§");
	}

	@Override
	public boolean queue() {
		VerifierResult queue = queue(pluginName);
		if (queue == VerifierResult.ERROR) {
			Bukkit.getConsoleSender().sendMessage(messageError);
			return false;
		}
		return true;
	}
	
}
