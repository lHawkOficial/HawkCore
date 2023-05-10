package me.hawkcore.utils.events.events.sumo.commands;

import org.bukkit.Sound;





import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.hawkcore.Core;
import me.hawkcore.utils.Eco;
import me.hawkcore.utils.events.events.sumo.Sumo;
import me.hawkcore.utils.events.events.sumo.utils.MensagensSumo;
import me.hawkcore.utils.events.utils.enums.EventStatus;

public class SumoCommand implements CommandExecutor {

	public SumoCommand() {
		Core.getInstance().getCommand("fight").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String lb, String[] args) {
		Sumo sumo = Sumo.get();
		Player p = !(s instanceof Player) ? null : (Player)s;
		if (p != null) p.playSound(p.getLocation(), Sound.NOTE_BASS, 0.5f, 10);
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("menu")) {
				if (p != null) {
					sumo.getMenu().open(p);
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("top")) {
				if (p != null) {
					sumo.getMenu().getMenutop().open(p);
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("camarote") && p != null) {
				if (!sumo.containsPlayerOnEvent(p)) {
					sumo.addPlayerToEspectator(p);
					return false;
				}else {
					s.sendMessage(MensagensSumo.get().getIsOnEvent());
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("entrar")) {
				if (p != null) {
					if (sumo.getEventStatus() == EventStatus.WARNING) {
						if (!sumo.containsPlayerOnEvent(p)) {
							if (Eco.get().has(p, sumo.getConfigsumo().getValueJoin())) {
								sumo.addPlayerToEvent(p, sumo);
								s.sendMessage(MensagensSumo.get().getEventJoin());
								return false;
							}else {
								s.sendMessage(MensagensSumo.get().getNoMoney());
								return false;
							}
						}else {
							s.sendMessage(MensagensSumo.get().getIsOnEvent());
							return false;
						}
					} else {
						s.sendMessage(MensagensSumo.get().getEventStopped());
						return false;
					}
				}
			}
			if (args[0].equalsIgnoreCase("setKit") && p != null) {
				if (s.hasPermission("hawkcore.commands.sumo")) {
					sumo.setArmor_kit(p.getInventory().getArmorContents().clone());
					sumo.setContent_kit(p.getInventory().getContents().clone());
					sumo.save();
					p.sendMessage(MensagensSumo.get().getKitSet());
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("verKit") && p != null) {
				if (s.hasPermission("hawkcore.commands.sumo")) {
					sumo.getMenu().getMenukit().open(p);
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("iniciar")) {
				if (s.hasPermission("hawkcore.commands.sumo")) {
					if (sumo.getEventStatus() == EventStatus.STOPPED) {
						if (sumo.isConfigured()) {
							sumo.start();
							s.sendMessage(MensagensSumo.get().getStarted());
							return false;
						}else {
							s.sendMessage(MensagensSumo.get().getNoConfigured());
							return false;
						}
					}else {
						s.sendMessage(MensagensSumo.get().getAlreadyStart());
						return false;
					}
				}
			}
			if (args[0].equalsIgnoreCase("setlobby") && p != null) {
				if (s.hasPermission("hawkcore.commands.sumo")) {
					sumo.setLocationLobby(p.getLocation().clone());
					sumo.save();
					p.sendMessage(MensagensSumo.get().getLobbySet());
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("setPos1") && p != null) {
				if (s.hasPermission("hawkcore.commands.sumo")) {
					sumo.setLocationPlayer1(p.getLocation().clone());
					sumo.save();
					p.sendMessage(MensagensSumo.get().getPos1Set());
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("setPos2") && p != null) {
				if (s.hasPermission("hawkcore.commands.sumo")) {
					sumo.setLocationPlayer2(p.getLocation().clone());
					sumo.save();
					p.sendMessage(MensagensSumo.get().getPos2Set());
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("setStart") && p != null) {
				if (s.hasPermission("hawkcore.commands.sumo")) {
					sumo.setLocationStart(p.getLocation().clone());
					sumo.save();
					p.sendMessage(MensagensSumo.get().getStartSet());
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("setEspectator") && p != null) {
				if (s.hasPermission("hawkcore.commands.sumo")) {
					sumo.setLocationEspectator(p.getLocation().clone());
					sumo.save();
					p.sendMessage(MensagensSumo.get().getEspectatorSet());
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("setExit") && p != null) {
				if (s.hasPermission("hawkcore.commands.sumo")) {
					sumo.setLocationExit(p.getLocation().clone());
					sumo.save();
					p.sendMessage(MensagensSumo.get().getExitSet());
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("tpExit") && p != null) {
				if (s.hasPermission("hawkcore.commands.sumo")) {
					if (sumo.getLocationExit()!=null) {
						p.teleport(sumo.getLocationExit());
						p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 0.5f, 10);
					}else {
						p.sendMessage(MensagensSumo.get().getLocationNotFound());
					}
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("tpLobby") && p != null) {
				if (s.hasPermission("hawkcore.commands.sumo")) {
					if (sumo.getLocationLobby()!=null) {
						p.teleport(sumo.getLocationLobby());
						p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 0.5f, 10);
					}else {
						p.sendMessage(MensagensSumo.get().getLocationNotFound());
					}
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("tpPos1") && p != null) {
				if (s.hasPermission("hawkcore.commands.sumo")) {
					if (sumo.getLocationPlayer1()!=null) {
						p.teleport(sumo.getLocationPlayer1());
						p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 0.5f, 10);
					}else {
						p.sendMessage(MensagensSumo.get().getLocationNotFound());
					}
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("tpPos2") && p != null) {
				if (s.hasPermission("hawkcore.commands.sumo")) {
					if (sumo.getLocationPlayer2()!=null) {
						p.teleport(sumo.getLocationPlayer2());
						p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 0.5f, 10);
					}else {
						p.sendMessage(MensagensSumo.get().getLocationNotFound());
					}
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("tpStart") && p != null) {
				if (s.hasPermission("hawkcore.commands.sumo")) {
					if (sumo.getLocationStart()!=null) {
						p.teleport(sumo.getLocationStart());
						p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 0.5f, 10);
					}else {
						p.sendMessage(MensagensSumo.get().getLocationNotFound());
					}
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("tpEspectator") && p != null) {
				if (s.hasPermission("hawkcore.commands.sumo")) {
					if (sumo.getLocationEspectator() != null) {
						p.teleport(sumo.getLocationEspectator());
						p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 0.5f, 10);
					}else {
						p.sendMessage(MensagensSumo.get().getLocationNotFound());
					}
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("parar")) {
				if (s.hasPermission("hawkcore.commands.sumo")) {
					if (sumo.getEventStatus() != EventStatus.STOPPED) {
						sumo.finish();
						s.sendMessage(MensagensSumo.get().getStopped());
						return false;
					}else {
						s.sendMessage(MensagensSumo.get().getAlreadyStopped());
						return false;
					}
				}
			}
		}
		if (s.hasPermission("hawkcore.commands.sumo")) {
			MensagensSumo.get().getCommands_adm().forEach(msg -> s.sendMessage(msg));
		}else {
			MensagensSumo.get().getCommands_player().forEach(msg -> s.sendMessage(msg));
		}
		return false;
	}

}
