package me.hawkcore.utils.events.events.bolao.commands;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.hawkcore.Core;
import me.hawkcore.utils.Eco;
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
			if (args[0].equalsIgnoreCase("entrar")) {
				if (p != null) {
					if (bolao.getEventStatus() == EventStatus.INGAME) {
						if (!bolao.containsPlayerOnEvent(p)) {
							if (Eco.get().has(p, bolao.getConfigbolao().getValueJoin())) {
								bolao.addPlayerToEvent(p, bolao);
								s.sendMessage(MensagensBolao.get().getEventJoin());
								return false;
							}else {
								s.sendMessage(MensagensBolao.get().getNoMoney());
								return false;
							}
						}else {
							s.sendMessage(MensagensBolao.get().getIsOnEvent());
							return false;
						}
					} else {
						s.sendMessage(MensagensBolao.get().getEventStopped());
						return false;
					}
				}
			}
			if (args[0].equalsIgnoreCase("iniciar")) {
				if (s.hasPermission("hawkcore.commands.bolao")) {
					if (bolao.getEventStatus() == EventStatus.STOPPED) {
						bolao.start();
						s.sendMessage(MensagensBolao.get().getStarted());
						return false;
					}else {
						s.sendMessage(MensagensBolao.get().getAlreadyStart());
						return false;
					}
				}
			}
			if (args[0].equalsIgnoreCase("parar")) {
				if (s.hasPermission("hawkcore.commands.bolao")) {
					if (bolao.getEventStatus() == EventStatus.INGAME) {
						bolao.finish();
						s.sendMessage(MensagensBolao.get().getStopped());
						return false;
					}else {
						s.sendMessage(MensagensBolao.get().getAlreadyStopped());
						return false;
					}
				}
			}
		}
		if (s.hasPermission("hawkcore.commands.bolao")) {
			MensagensBolao.get().getCommands_adm().forEach(msg -> s.sendMessage(msg));
		}else {
			MensagensBolao.get().getCommands_player().forEach(msg -> s.sendMessage(msg));
		}
		return false;
	}

}
