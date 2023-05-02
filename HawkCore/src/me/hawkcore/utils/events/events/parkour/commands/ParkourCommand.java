package me.hawkcore.utils.events.events.parkour.commands;

import org.bukkit.Sound;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.hawkcore.Core;
import me.hawkcore.utils.Eco;
import me.hawkcore.utils.events.EventManager;
import me.hawkcore.utils.events.events.bolao.utils.MensagensBolao;
import me.hawkcore.utils.events.events.parkour.Parkour;
import me.hawkcore.utils.events.utils.enums.EventStatus;

public class ParkourCommand implements CommandExecutor {

	public ParkourCommand() {
		Core.getInstance().getCommand("parkour").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String lb, String[] args) {
		s.sendMessage(""+EventManager.get().getEvent("parkour"));
		Parkour parkour = Parkour.get();
		Player p = !(s instanceof Player) ? null : (Player)s;
		if (p != null) p.playSound(p.getLocation(), Sound.NOTE_BASS, 0.5f, 10);
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("menu")) {
				if (p != null) {
					parkour.getMenu().open(p);
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("top")) {
				if (p != null) {
					parkour.getMenu().getMenutop().open(p);
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("entrar")) {
				if (p != null) {
					if (parkour.getEventStatus() == EventStatus.INGAME) {
						if (!parkour.containsPlayerOnEvent(p)) {
							if (Eco.get().has(p, parkour.getConfigparkour().getValueJoin())) {
								parkour.addPlayerToEvent(p, parkour);
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
				if (s.hasPermission("hawkcore.commands.parkour")) {
					if (parkour.getEventStatus() == EventStatus.STOPPED) {
						parkour.start();
						s.sendMessage(MensagensBolao.get().getStarted());
						return false;
					}else {
						s.sendMessage(MensagensBolao.get().getAlreadyStart());
						return false;
					}
				}
			}
			if (args[0].equalsIgnoreCase("parar")) {
				if (s.hasPermission("hawkcore.commands.parkour")) {
					if (parkour.getEventStatus() == EventStatus.INGAME) {
						parkour.finish();
						s.sendMessage(MensagensBolao.get().getStopped());
						return false;
					}else {
						s.sendMessage(MensagensBolao.get().getAlreadyStopped());
						return false;
					}
				}
			}
		}
		if (s.hasPermission("hawkcore.commands.parkour")) {
			MensagensBolao.get().getCommands_adm().forEach(msg -> s.sendMessage(msg));
		}else {
			MensagensBolao.get().getCommands_player().forEach(msg -> s.sendMessage(msg));
		}
		return false;
	}

}
