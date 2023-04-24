package me.hawkcore.utils.events.events.bolao.commands;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.hawkcore.Core;
import me.hawkcore.utils.events.events.bolao.Bolao;
import me.hawkcore.utils.events.events.bolao.utils.MensagensBolao;
import me.hawkcore.utils.events.utils.enums.EventStatus;

public class BolaoCommand implements CommandExecutor {

	public BolaoCommand() {
		Core.getInstance().getCommand("bolao").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String lb, String[] args) {
		Bolao bolao = Bolao.get();
		Player p = !(s instanceof Player) ? null : (Player)s;
		if (p != null) p.playSound(p.getLocation(), Sound.NOTE_BASS, 0.5f, 10);
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("iniciar")) {
				if (bolao.getEventStatus() == EventStatus.STOPPED) {
					bolao.start();
					s.sendMessage(MensagensBolao.get().getStarted());
					return false;
				}else {
					s.sendMessage(MensagensBolao.get().getAlreadyStart());
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("parar")) {
				if (bolao.getEventStatus() == EventStatus.INGAME) {
					bolao.stop();
					s.sendMessage(MensagensBolao.get().getStopped());
					return false;
				}else {
					s.sendMessage(MensagensBolao.get().getAlreadyStopped());
					return false;
				}
			}
		}
		return false;
	}

}
