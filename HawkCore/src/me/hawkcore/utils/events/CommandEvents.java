package me.hawkcore.utils.events;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.hawkcore.Core;
import me.hawkcore.utils.ConfigGeral;
import me.hawkcore.utils.events.events.bolao.commands.BolaoCommand;
import me.hawkcore.utils.events.utils.Event;
import me.hawkcore.utils.events.utils.MenuEvents;
import me.hawkcore.utils.events.utils.interfaces.EventExecutor;

public class CommandEvents implements CommandExecutor {

	public CommandEvents() {
		Core.getInstance().getCommand("events").setExecutor(this);
		new BolaoCommand();
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String lb, String[] args) {
		Player p = s instanceof Player ? (Player)s : null;
		if (p!=null) p.playSound(p.getLocation(), Sound.NOTE_BASS, 0.5f, 10);
		if (s.hasPermission("hawkcore.commands.events")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("reload")) {
					EventManager.get().getEvents().forEach(event -> ((EventExecutor)event).finish());
					Core.getInstance().setupEvents();
					s.sendMessage(Core.getInstance().getTag() + " §aOs eventos foram recarregados com sucesso!");
					return false;
				}
				if (args[0].equalsIgnoreCase("list")) {
					s.sendMessage(Core.getInstance().getTag() + " §aEventos Criados §f" + EventManager.get().getEvents().size());
					for(Event event : EventManager.get().getEvents()) {
						s.sendMessage("§5# §f" + event.getName());
					}
					return false;
				}
				ConfigGeral.get().getEvents_command().forEach(msg -> s.sendMessage(msg));
				return false;
			}
		}
		MenuEvents.get().open(p);
		return false;
	}

}
