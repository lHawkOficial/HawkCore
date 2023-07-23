package me.hawkcore.commands.creator;

import org.bukkit.command.CommandSender;

import lombok.Getter;
import lombok.Setter;
import me.hawkcore.Core;
import me.hawkcore.commands.creator.interfaces.CommandInterface;

@Getter
@Setter
public class Command implements CommandInterface {
	
	private String name;
	
	public Command(String name) {
		this.name = name;
		Core.getInstance().getCommand(name).setExecutor(Core.getInstance().getCommandMain());
		Core.getInstance().getCommandmanager().getCommands().add(this);
	}

	@Override
	public void onCommand(CommandSender s, String[] args) {}
	
}
