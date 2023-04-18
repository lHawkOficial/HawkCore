package me.hawkcore.commands;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.hawkcore.Core;
import me.hawkcore.utils.Eco;
import me.hawkcore.utils.Mensagens;
import me.hawkcore.utils.configs.ConfigCommands;

public class DesenchantCommand implements CommandExecutor {

	public DesenchantCommand() {
		Core.getInstance().getCommand("desencantar").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String lb, String[] args) {
		if (!(s instanceof Player)) return false;
		Player p = (Player) s;		
		p.playSound(p.getLocation(), Sound.NOTE_BASS, 0, 0);
		if (!p.hasPermission("hawkcore.command.desenchant")) {
			p.sendMessage(Mensagens.get().getPermission());
			return false;
		}
		if (Eco.get().has(p, ConfigCommands.get().getValueCommandDesenchant())) {
			if (p.getItemInHand() != null && p.getItemInHand().getType() != Material.AIR && !p.getItemInHand().getEnchantments().isEmpty()) {
				Eco.get().withdrawPlayer(p, ConfigCommands.get().getValueCommandDesenchant());
				ItemStack item = p.getItemInHand();
				for(Enchantment enchant : item.getEnchantments().keySet()) {
					item.removeEnchantment(enchant);
				}
				p.sendMessage(Mensagens.get().getCommandDesenchant_sucess());
				p.playSound(p.getLocation(), Sound.LEVEL_UP, 0.5f, 10);
				p.updateInventory();
				return false;
			}else {
				p.sendMessage(Mensagens.get().getCommandDesenchant_itemErro());
			}
		}else {
			p.sendMessage(Mensagens.get().getCommandDesenchant_noMoney().replace("{valor}", Eco.get().format(ConfigCommands.get().getValueCommandDesenchant())));
		}
		return false;
	}
	
}
