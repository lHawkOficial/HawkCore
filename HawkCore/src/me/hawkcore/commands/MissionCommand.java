package me.hawkcore.commands;

import org.bukkit.command.Command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.hawkcore.Core;
import me.hawkcore.utils.missions.menus.menuCategorys;

public class MissionCommand implements CommandExecutor {

	public MissionCommand() {
		Core.getInstance().getCommand("mission").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String lb, String[] args) {
		if (!(s instanceof Player)) return false;
		Player p = (Player) s;
		if (p.hasMetadata("pMissions")) p.removeMetadata("pMissions", Core.getInstance());
		menuCategorys.get().open(p);
		return false;
	}
	
}
