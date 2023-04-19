package me.hawkcore.utils.events.events.bolao.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.hawkcore.Core;

public class BolaoCommand implements CommandExecutor {

	public BolaoCommand() {
		Core.getInstance().getCommand("bolao").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String lb, String[] args) {
		
		return false;
	}

}
