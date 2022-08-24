package me.hawkcore;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.hawkcore.tasks.TaskManager;
import me.hawkcore.utils.API;

@Getter
public class Core extends JavaPlugin {
	
	private String version = "§dv" + getDescription().getVersion();
	private TaskManager taskmanager;
	private API api;
	
	@Getter
	private static Core instance;
	
	@Override
	public void onEnable() {
		instance = this;
		api = new API();
		taskmanager = new TaskManager();
		taskmanager.runTaskTimerAsynchronously(this, 0, 1);
		
		sendConsole(" ");
		sendConsole("&aHawkCore iniciado com sucesso! &6[Author lHawk_] " + version);
		sendConsole(" ");
		
	}
	
	@Override
	public void onDisable() {
		taskmanager.cancel();
		sendConsole(" ");
		sendConsole("&cHawkCore desligado com sucesso! &6[Author lHawk_] " + version);
		sendConsole(" ");
	}
	
	private void sendConsole(String msg) {Bukkit.getConsoleSender().sendMessage(msg.replace("&", ""));}
	
}
