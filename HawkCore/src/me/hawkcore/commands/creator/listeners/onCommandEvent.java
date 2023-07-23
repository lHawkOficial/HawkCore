package me.hawkcore.commands.creator.listeners;

import org.bukkit.command.CommandSender;

import lombok.Getter;
import me.hawkcore.commands.creator.Command;
import me.hawkcore.utils.listenercreator.Listener;

@Getter
public class onCommandEvent extends Listener {

	private CommandSender sender;
	private Command command;
	
	public onCommandEvent(CommandSender sender, Command command) {
		this.sender = sender;
		this.command = command;
	}
	
}
