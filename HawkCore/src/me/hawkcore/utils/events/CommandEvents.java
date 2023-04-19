package me.hawkcore.utils.events;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.hawkcore.Core;
import me.hawkcore.utils.events.events.bolao.commands.BolaoCommand;

public class CommandEvents implements CommandExecutor {

	public CommandEvents() {
		Core.getInstance().getCommand("events").setExecutor(this);
		new BolaoCommand();
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String lb, String[] args) {
		if (args.length == 1) {
			
		}
		return false;
	}

}
