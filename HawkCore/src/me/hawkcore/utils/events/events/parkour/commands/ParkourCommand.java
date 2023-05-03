package me.hawkcore.utils.events.events.parkour.commands;

import org.bukkit.Sound;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.hawkcore.Core;
import me.hawkcore.utils.Eco;
import me.hawkcore.utils.events.events.parkour.Parkour;
import me.hawkcore.utils.events.events.parkour.utils.MensagensParkour;
import me.hawkcore.utils.events.utils.enums.EventStatus;

public class ParkourCommand implements CommandExecutor {

	public ParkourCommand() {
		Core.getInstance().getCommand("parkour").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String lb, String[] args) {
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
					if (parkour.getEventStatus() == EventStatus.WARNING) {
						if (!parkour.containsPlayerOnEvent(p)) {
							if (Eco.get().has(p, parkour.getConfigparkour().getValueJoin())) {
								parkour.addPlayerToEvent(p, parkour);
								s.sendMessage(MensagensParkour.get().getEventJoin());
								return false;
							}else {
								s.sendMessage(MensagensParkour.get().getNoMoney());
								return false;
							}
						}else {
							s.sendMessage(MensagensParkour.get().getIsOnEvent());
							return false;
						}
					} else {
						s.sendMessage(MensagensParkour.get().getEventStopped());
						return false;
					}
				}
			}
			if (args[0].equalsIgnoreCase("iniciar")) {
				if (s.hasPermission("hawkcore.commands.parkour")) {
					if (parkour.getEventStatus() == EventStatus.STOPPED) {
						if (parkour.isConfigured()) {
							parkour.start();
							s.sendMessage(MensagensParkour.get().getStarted());
							return false;
						}else {
							s.sendMessage(MensagensParkour.get().getNoConfigured());
							return false;
						}
					}else {
						s.sendMessage(MensagensParkour.get().getAlreadyStart());
						return false;
					}
				}
			}
			if (args[0].equalsIgnoreCase("setlobby") && p != null) {
				if (s.hasPermission("hawkcore.commands.parkour")) {
					parkour.setLocationLobby(p.getLocation().clone());
					parkour.save();
					p.sendMessage(MensagensParkour.get().getLobbySet());
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("setStart") && p != null) {
				if (s.hasPermission("hawkcore.commands.parkour")) {
					parkour.setLocationStart(p.getLocation().clone());
					parkour.save();
					p.sendMessage(MensagensParkour.get().getStartSet());
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("setFinish") && p != null) {
				if (s.hasPermission("hawkcore.commands.parkour")) {
					parkour.setLocationFinish(p.getLocation().clone());
					parkour.save();
					p.sendMessage(MensagensParkour.get().getStartSet());
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("setExit") && p != null) {
				if (s.hasPermission("hawkcore.commands.parkour")) {
					parkour.setLocationExit(p.getLocation().clone());
					parkour.save();
					p.sendMessage(MensagensParkour.get().getExitSet());
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("tpExit") && p != null) {
				if (s.hasPermission("hawkcore.commands.parkour")) {
					if (parkour.getLocationExit()!=null) {
						p.teleport(parkour.getLocationExit());
						p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 0.5f, 10);
					}else {
						p.sendMessage(MensagensParkour.get().getLocationNotFound());
					}
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("tpLobby") && p != null) {
				if (s.hasPermission("hawkcore.commands.parkour")) {
					if (parkour.getLocationLobby()!=null) {
						p.teleport(parkour.getLocationLobby());
						p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 0.5f, 10);
					}else {
						p.sendMessage(MensagensParkour.get().getLocationNotFound());
					}
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("tpStart") && p != null) {
				if (s.hasPermission("hawkcore.commands.parkour")) {
					if (parkour.getLocationStart()!=null) {
						p.teleport(parkour.getLocationStart());
						p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 0.5f, 10);
					}else {
						p.sendMessage(MensagensParkour.get().getLocationNotFound());
					}
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("tpFinish") && p != null) {
				if (s.hasPermission("hawkcore.commands.parkour")) {
					if (parkour.getLocationStart()!=null) {
						p.teleport(parkour.getLocationFinish());
						p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 0.5f, 10);
					}else {
						p.sendMessage(MensagensParkour.get().getLocationNotFound());
					}
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("parar")) {
				if (s.hasPermission("hawkcore.commands.parkour")) {
					if (parkour.getEventStatus() != EventStatus.STOPPED) {
						parkour.finish();
						s.sendMessage(MensagensParkour.get().getStopped());
						return false;
					}else {
						s.sendMessage(MensagensParkour.get().getAlreadyStopped());
						return false;
					}
				}
			}
		}
		if (s.hasPermission("hawkcore.commands.parkour")) {
			MensagensParkour.get().getCommands_adm().forEach(msg -> s.sendMessage(msg));
		}else {
			MensagensParkour.get().getCommands_player().forEach(msg -> s.sendMessage(msg));
		}
		return false;
	}

}
