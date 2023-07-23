package me.hawkcore.commands;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.hawkcore.Core;
import me.hawkcore.commands.creator.Command;
import me.hawkcore.utils.missions.ManagerMissions;
import me.hawkcore.utils.missions.menus.menuCategorys;
import me.hawkcore.utils.missions.menus.menuMissions;
import me.hawkcore.utils.missions.objects.MissionCategory;
import me.hawkcore.utils.missions.objects.MissionPlayer;

public class MissionCommand extends Command {

	public MissionCommand(String name) {
		super(name);
	}
	
	@Override
	public void onCommand(CommandSender s, String[] args) {
		Player p = null;
		if (s instanceof Player) p = (Player) s;
		String tag = Core.getInstance().getTag();
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("help") && s.hasPermission("HawkCore.Missions.Adm")) {
				s.sendMessage(" ");
				s.sendMessage(tag + " §aComandos disponíveis:");
				s.sendMessage("§e/Missao [Clear] [Player] §7- Limpar todas as missões de um jogador.");
				s.sendMessage("§e/Missao [Clear] [Category] [Player] §7- Limpar uma categoria específica de um jogador.");
				s.sendMessage("§e/Missao [CompleteAll] [Player] §7- Completar todas as missões de todas as categorias do jogador.");
				s.sendMessage("§e/Missao [Complete] [Category] [Player] §7- Completar uma categoria de missões de um jogador.");
				s.sendMessage(" ");
				return;
			}
		}
		if (args.length == 2 && s.hasPermission("HawkCore.Missions.Adm")) {
			if (args[0].equalsIgnoreCase("clear")) {
				MissionPlayer mp = ManagerMissions.get().getMissionPlayer(args[1]);
				if (mp != null) {
					for(MissionCategory category : mp.getCategorys()) {
						category.getMissions().forEach(m -> {
							m.getObjective().setValue(0);
							m.setCompleted(false);
						});
					}
					mp.save();
					s.sendMessage(tag + " §aVocê resetou todas as missões do jogador §f" + mp.getName() + "§a!");
					return;
				}else {
					s.sendMessage(tag + " §cEste jogador não foi encontrado!");
					return;
				}
			}
			if (args[0].equalsIgnoreCase("completeAll")) {
				MissionPlayer mp = ManagerMissions.get().getMissionPlayer(args[1]);
				if (mp != null) {
					for(MissionCategory category : mp.getCategorys()) {
						category.getMissions().forEach(m -> {
							m.getObjective().setValue(m.getObjective().getMaxValue());
							m.setCompleted(true);
						});
					}
					mp.save();
					s.sendMessage(tag + " §aVocê completou todas as missões do jogador §f" + mp.getName() + "§a!");
					return;
				}else {
					s.sendMessage(tag + " §cEste jogador não foi encontrado!");
					return;
				}
			}
		}
		if (args.length == 3 && s.hasPermission("HawkCore.Missions.Adm")) {
			if (args[0].equalsIgnoreCase("clear")) {
				MissionPlayer mp = ManagerMissions.get().getMissionPlayer(args[2]);
				if (mp != null) {
					MissionCategory category = mp.getMissionCategory(args[1]);
					if (category != null) {
						category.getMissions().forEach(m -> {
							m.getObjective().setValue(0);
							m.setCompleted(false);
						});
						mp.save();
						s.sendMessage(tag + " §aVocê resetou todas as missões do jogador §f" + mp.getName() + "§a da categoria "+category.getName()+"!");
						return;
					}else {
						s.sendMessage(tag + " §cEsta categoria não existe!");
						return;
					}
				}else {
					s.sendMessage(tag + " §cEste jogador não foi encontrado!");
					return;
				}
			}
			if (args[0].equalsIgnoreCase("complete")) {
				MissionPlayer mp = ManagerMissions.get().getMissionPlayer(args[2]);
				if (mp != null) {
					MissionCategory category = mp.getMissionCategory(args[1]);
					if (category != null) {
						category.getMissions().forEach(m -> {
							m.getObjective().setValue(m.getObjective().getMaxValue());
							m.setCompleted(true);
						});
						mp.save();
						s.sendMessage(tag + " §aVocê completou todas as missões do jogador §f" + mp.getName() + "§a na categoria "+category.getName()+"!");
						return;
					}else {
						s.sendMessage(tag + " §cEsta categoria não existe!");
						return;
					}
				}else {
					s.sendMessage(tag + " §cEste jogador não foi encontrado!");
					return;
				}
			}
		}
		if (p != null) {
			p.playSound(p.getLocation(), Sound.NOTE_BASS_GUITAR, 0.5f, 10);
			if (p.hasMetadata("pMissions")) p.removeMetadata("pMissions", Core.getInstance());
			MissionPlayer mp = MissionPlayer.check(p);
			if (args.length == 1) {
				MissionCategory category = mp.getMissionCategory(args[0]);
				if (category != null) {
					mp.setCategorySelected(category);
					menuMissions.get().open(p);
					return;
				}
			}
			menuCategorys.get().open(p);
		}
	}
	
}
