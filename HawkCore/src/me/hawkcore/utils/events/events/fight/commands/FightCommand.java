package me.hawkcore.utils.events.events.fight.commands;

import org.bukkit.Sound;



import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.hawkcore.Core;
import me.hawkcore.utils.Eco;
import me.hawkcore.utils.events.events.fight.Fight;
import me.hawkcore.utils.events.events.fight.utils.MensagensFight;
import me.hawkcore.utils.events.utils.enums.EventStatus;

public class FightCommand implements CommandExecutor {

	public FightCommand() {
		Core.getInstance().getCommand("fight").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String lb, String[] args) {
		Fight fight = Fight.get();
		Player p = !(s instanceof Player) ? null : (Player)s;
		if (p != null) p.playSound(p.getLocation(), Sound.NOTE_BASS, 0.5f, 10);
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("menu")) {
				if (p != null) {
					fight.getMenu().open(p);
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("top")) {
				if (p != null) {
					fight.getMenu().getMenutop().open(p);
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("entrar")) {
				if (p != null) {
					if (fight.getEventStatus() == EventStatus.WARNING) {
						if (!fight.containsPlayerOnEvent(p)) {
							if (Eco.get().has(p, fight.getConfigfight().getValueJoin())) {
								fight.addPlayerToEvent(p, fight);
								s.sendMessage(MensagensFight.get().getEventJoin());
								return false;
							}else {
								s.sendMessage(MensagensFight.get().getNoMoney());
								return false;
							}
						}else {
							s.sendMessage(MensagensFight.get().getIsOnEvent());
							return false;
						}
					} else {
						s.sendMessage(MensagensFight.get().getEventStopped());
						return false;
					}
				}
			}
			if (args[0].equalsIgnoreCase("iniciar")) {
				if (s.hasPermission("hawkcore.commands.fight")) {
					if (fight.getEventStatus() == EventStatus.STOPPED) {
						if (fight.isConfigured()) {
							fight.start();
							s.sendMessage(MensagensFight.get().getStarted());
							return false;
						}else {
							s.sendMessage(MensagensFight.get().getNoConfigured());
							return false;
						}
					}else {
						s.sendMessage(MensagensFight.get().getAlreadyStart());
						return false;
					}
				}
			}
			if (args[0].equalsIgnoreCase("setlobby") && p != null) {
				if (s.hasPermission("hawkcore.commands.fight")) {
					fight.setLocationLobby(p.getLocation().clone());
					fight.save();
					p.sendMessage(MensagensFight.get().getLobbySet());
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("setStart") && p != null) {
				if (s.hasPermission("hawkcore.commands.fight")) {
					fight.setLocationStart(p.getLocation().clone());
					fight.save();
					p.sendMessage(MensagensFight.get().getStartSet());
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("setEspectator") && p != null) {
				if (s.hasPermission("hawkcore.commands.fight")) {
					fight.setLocationEspectator(p.getLocation().clone());
					fight.save();
					p.sendMessage(MensagensFight.get().getStartSet());
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("setExit") && p != null) {
				if (s.hasPermission("hawkcore.commands.fight")) {
					fight.setLocationExit(p.getLocation().clone());
					fight.save();
					p.sendMessage(MensagensFight.get().getExitSet());
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("tpExit") && p != null) {
				if (s.hasPermission("hawkcore.commands.fight")) {
					if (fight.getLocationExit()!=null) {
						p.teleport(fight.getLocationExit());
						p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 0.5f, 10);
					}else {
						p.sendMessage(MensagensFight.get().getLocationNotFound());
					}
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("tpLobby") && p != null) {
				if (s.hasPermission("hawkcore.commands.fight")) {
					if (fight.getLocationLobby()!=null) {
						p.teleport(fight.getLocationLobby());
						p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 0.5f, 10);
					}else {
						p.sendMessage(MensagensFight.get().getLocationNotFound());
					}
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("tpStart") && p != null) {
				if (s.hasPermission("hawkcore.commands.fight")) {
					if (fight.getLocationStart()!=null) {
						p.teleport(fight.getLocationStart());
						p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 0.5f, 10);
					}else {
						p.sendMessage(MensagensFight.get().getLocationNotFound());
					}
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("tpEspectator") && p != null) {
				if (s.hasPermission("hawkcore.commands.fight")) {
					if (fight.getLocationStart()!=null) {
						p.teleport(fight.getLocationEspectator());
						p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 0.5f, 10);
					}else {
						p.sendMessage(MensagensFight.get().getLocationNotFound());
					}
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("parar")) {
				if (s.hasPermission("hawkcore.commands.fight")) {
					if (fight.getEventStatus() != EventStatus.STOPPED) {
						fight.finish();
						s.sendMessage(MensagensFight.get().getStopped());
						return false;
					}else {
						s.sendMessage(MensagensFight.get().getAlreadyStopped());
						return false;
					}
				}
			}
		}
		if (s.hasPermission("hawkcore.commands.fight")) {
			MensagensFight.get().getCommands_adm().forEach(msg -> s.sendMessage(msg));
		}else {
			MensagensFight.get().getCommands_player().forEach(msg -> s.sendMessage(msg));
		}
		return false;
	}

}
