package me.hawkcore.commands;

import org.bukkit.command.CommandSender;

import me.hawkcore.commands.creator.Command;

public class PvPCommand extends Command {

	public PvPCommand(String name) {
		super(name);
	}

	@Override
	public void onCommand(CommandSender s, String[] args) {
		s.sendMessage("olá");
	}
	
}
