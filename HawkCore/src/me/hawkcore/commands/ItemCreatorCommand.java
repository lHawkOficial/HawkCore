package me.hawkcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.hawkcore.Core;
import me.hawkcore.utils.API;
import me.hawkcore.utils.inventories.InventoryAPI;
import me.hawkcore.utils.itemcreator.ItemCreator;
import me.hawkcore.utils.itemcreator.ManagerItemCreator;

public class ItemCreatorCommand implements CommandExecutor {

	public ItemCreatorCommand() {
		Core.getInstance().getCommand("itemcreator").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String lb, String[] args) {
		if (!s.hasPermission("hawkcore.itemcreator")) return false;
		
		String tag = Core.getInstance().getTag();
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("list")) {
				String linha = new String();
				s.sendMessage(tag + " §7Total Item(s) §e" + ManagerItemCreator.get().getItems().size());
				for(ItemCreator ic : ManagerItemCreator.get().getItems()) {
					linha += "§7, §f" + ic.getNome();
				}
				s.sendMessage(linha.replaceFirst("§7, ", new String()));
				return false;
			}
		}
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("criar")) {
				if (!(s instanceof Player)) return false;
				Player p = (Player) s;
				ItemCreator ic = ManagerItemCreator.get().getItem(args[1]);
				if (ic == null) {
					if (p.getItemInHand() != null && p.getItemInHand().getType() != Material.AIR) {
						ItemStack item = p.getItemInHand().clone();
						new ItemCreator(args[1], item);
						s.sendMessage(tag + " §aItem §f" + args[1] + " §acriado com sucesso!");
						return false;
					}else {
						s.sendMessage(tag + " §cSegure um item válido para poder criar!.");
						return false;
					}
				}else {
					s.sendMessage(tag + " §cEste item ja existe!.");
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("deletar")) {
				ItemCreator ic = ManagerItemCreator.get().getItem(args[1]);
				if (ic != null) {
					ic.delete();
					s.sendMessage(tag + " §aItem §f" + ic.getNome() + " §adeletado com sucesso!");
					return false;
				}else {
					s.sendMessage(tag + " §cEste item não existe!.");
					return false;
				}
			}
		}
		if (args.length == 4) {
			if (args[0].equalsIgnoreCase("give")) {
				ItemCreator ic = ManagerItemCreator.get().getItem(args[2]);
				if (ic != null) {
					if (API.get().isInteger(args[3])) {
						int amount = Integer.valueOf(args[3]);
						if (args[1].equalsIgnoreCase("all")) {
							int total = 0;
							for(Player p : Bukkit.getOnlinePlayers()) {
								for (int i = 0; i < amount; i++) {
									if (InventoryAPI.hasSpace(p, ic.getItem())) {
										p.getInventory().addItem(ic.getItem().clone());
									} else {
										p.getWorld().dropItem(p.getLocation(), ic.getItem().clone());
									}
								}
								total++;
							}
							s.sendMessage(tag + " §aGivado o item §f" + ic.getNome() + " §apara §f" + total + " jogadores§a!");
							return false;
						}else {
							Player p = Bukkit.getPlayerExact(args[1]);
							if (p != null) {
								for (int i = 0; i < amount; i++) {
									if (InventoryAPI.hasSpace(p, ic.getItem())) {
										p.getInventory().addItem(ic.getItem().clone());
									} else {
										p.getWorld().dropItem(p.getLocation(), ic.getItem().clone());
									}
								}
								s.sendMessage(tag + " §aGivado o item §f" + ic.getNome() + " §apara §f" + p.getName() + "§a!");
								return false;
							}else {
								s.sendMessage(tag + " §cEste jogador não foi encontrado.");
								return false;
							}
						}
					}
				}else {
					s.sendMessage(tag + " §cItem não encontrado.");
					return false;
				}
			}
		}
		
		s.sendMessage(" ");
		s.sendMessage(tag + " §e/Ic [Criar] [Nome] §7- Criar um novo item.");
		s.sendMessage(tag + " §e/Ic [Deletar] [Nome] §7- Deletar um item.");
		s.sendMessage(tag + " §e/Ic [List] §7- Listar todos os item(s) criados.");
		s.sendMessage(tag + " §e/Ic [Give] [Player/all] [NomeItem] [Amount] §7- Givar item aos jogadores");
		s.sendMessage(" ");
		return false;
	}
	
}
