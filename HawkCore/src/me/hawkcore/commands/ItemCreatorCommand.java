package me.hawkcore.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.hawkcore.Core;
import me.hawkcore.commands.creator.Command;
import me.hawkcore.utils.API;
import me.hawkcore.utils.inventories.InventoryAPI;
import me.hawkcore.utils.itemcreator.ItemCreator;
import me.hawkcore.utils.itemcreator.ManagerItemCreator;

public class ItemCreatorCommand extends Command {
	
	public ItemCreatorCommand(String name) {
		super(name);
		Core.getInstance().getCommand(name).setTabCompleter(new TabCompleter() {
			@Override
			public List<String> onTabComplete(CommandSender s, org.bukkit.command.Command c, String lb, String[] args) {
				if (args.length == 3) {
					if (args[0].equalsIgnoreCase("give")) {
						List<String> nomes = new ArrayList<>();
						for(ItemCreator ic : ManagerItemCreator.get().getItems()) {
							if (!ic.getNome().toLowerCase().contains(args[2].toLowerCase())) continue;
							nomes.add(ic.getNome());
						}
						return nomes;
					}
				}
				return null;
			}
		});
	}

	@Override
	public void onCommand(CommandSender s, String[] args) {
		if (!s.hasPermission("hawkcore.itemcreator")) return;
		
		String tag = Core.getInstance().getTag();
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("lista")) {
				String linha = new String();
				File folder = new File(Core.getInstance().getDataFolder() + "/itemcreator");
				if (!folder.exists()) folder.mkdir();
				s.sendMessage(tag + " §7Total Item(s) §e" + folder.listFiles().length);
				File[] files = folder.listFiles();
				Arrays.sort(files, (f1, f2) -> f1.getName().compareToIgnoreCase(f2.getName()));
				for(File file : files) {
					linha += "§7, " + (!file.isDirectory() ? "§f" + file.getName() : "§e" + file.getName() + " §8(" + ItemCreator.getItemCreatorFromFolder(file).size() + ")");
				}
				s.sendMessage(linha.replaceFirst("§7, ", new String()));
				return;
			}
		}
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("listfolder") || args[0].equalsIgnoreCase("listarpasta")) {
				File folder = new File(Core.getInstance().getDataFolder() + "/itemcreator/"+args[1]);
				if (folder.exists()) {
					String linha = new String();
					s.sendMessage(tag + " §7Total Item(s) §e" + ItemCreator.getItemCreatorFromFolder(folder).size());
					for(ItemCreator ic : ItemCreator.getItemCreatorFromFolder(folder)) {
						linha += "§7, " + "§f" + ic.getNome();
					}
					s.sendMessage(linha.replaceFirst("§7, ", new String()));
					return;
				} else {
					s.sendMessage(tag + " §cEsta pasta não foi encontrada!");
					return;
				}
			}
			if (args[0].equalsIgnoreCase("criar") || args[0].equalsIgnoreCase("create")) {
				if (!(s instanceof Player)) return;
				Player p = (Player) s;
				ItemCreator ic = ManagerItemCreator.get().getItem(args[1]);
				if (ic == null) {
					if (p.getItemInHand() != null && p.getItemInHand().getType() != Material.AIR) {
						ItemStack item = p.getItemInHand().clone();
						new ItemCreator(args[1], item);
						s.sendMessage(tag + " §aItem §f" + args[1] + " §acriado com sucesso!");
						return;
					}else {
						s.sendMessage(tag + " §cSegure um item válido para poder criar!.");
						return;
					}
				}else {
					s.sendMessage(tag + " §cEste item ja existe!.");
					return;
				}
			}
			if (args[0].equalsIgnoreCase("deletar") || args[0].equalsIgnoreCase("delete")) {
				ItemCreator ic = ManagerItemCreator.get().getItem(args[1]);
				if (ic != null) {
					ic.delete();
					s.sendMessage(tag + " §aItem §f" + ic.getNome() + " §adeletado com sucesso!");
					return;
				}else {
					s.sendMessage(tag + " §cEste item não existe!.");
					return;
				}
			}
		}
		if (args.length == 4) {
			if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("dar")) {
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
							return;
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
								return;
							}else {
								s.sendMessage(tag + " §cEste jogador não foi encontrado.");
								return;
							}
						}
					}
				}else {
					s.sendMessage(tag + " §cItem não encontrado.");
					return;
				}
			}
		}
		
		s.sendMessage(" ");
		s.sendMessage(tag + " §e/Ic [Criar] [Nome] §7- Criar um novo item.");
		s.sendMessage(tag + " §e/Ic [Deletar] [Nome] §7- Deletar um item.");
		s.sendMessage(tag + " §e/Ic [List] §7- Listar todos os item(s) criados.");
		s.sendMessage(tag + " §e/Ic [ListFolder] §7- Listar todos os item(s) criados de uma pasta.");
		s.sendMessage(tag + " §e/Ic [Give] [Player/all] [NomeItem] [Amount] §7- Givar item aos jogadores");
		s.sendMessage(" ");
	}
	
	
	
	
}
