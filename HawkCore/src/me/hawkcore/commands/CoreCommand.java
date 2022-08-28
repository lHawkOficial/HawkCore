package me.hawkcore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.hawkcore.Core;
import me.hawkcore.tasks.TaskManager;

public class CoreCommand implements CommandExecutor {

	public CoreCommand() {
		Core.getInstance().getCommand("hawkcore").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String lb, String[] args) {
		if (!s.hasPermission("*")) return false;
		s.sendMessage(Core.getInstance().getTag() + " §6A Última latência calculada para o tick do servidor foi de §e" + TaskManager.get().getLastTick() + "ms §6com um total de §e" + TaskManager.get().getTasks().size() + " tasks §6em execução!.");
		return false;
	}
	
}
