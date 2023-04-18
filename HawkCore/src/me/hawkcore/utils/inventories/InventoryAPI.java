package me.hawkcore.utils.inventories;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class InventoryAPI {

	public static boolean hasSpace(Player p, ItemStack it) {
		Inventory inv = p.getInventory();
		int space = 0;
		int stack = 0;
		for (ItemStack item : inv.getContents()) {
			if (item == null || item.getType() == Material.AIR)
				space++;
			else if (it != null && item.isSimilar(it) && item.getAmount() < item.getMaxStackSize())
				stack++;
		}
		return space > 0 || stack > 0 ? true : false;
	}

	public static int getAmount(Player p, ItemStack it) {
		if (it == null)
			return 0;
		int amount = 0;
		Inventory inv = p.getInventory();
		for (ItemStack item : inv.getContents()) {
			if (item == null || item.getType() == Material.AIR || !item.isSimilar(it))
				continue;
			amount += item.getAmount();
		}
		return amount;
	}

	public static void removeItem(Player player, ItemStack itemStack, int quantidade) {
		PlayerInventory inventario = player.getInventory();
		ItemStack[] itens = inventario.getContents();
		for (int i = 0; i < itens.length; i++) {
			ItemStack itemNoInventario = itens[i];
			if (itemNoInventario != null && itemNoInventario.isSimilar(itemStack)) {
				if (itemNoInventario.getAmount() >= quantidade) {
					itemNoInventario.setAmount(itemNoInventario.getAmount() - quantidade);
					inventario.setItem(i, itemNoInventario);
					player.updateInventory();
					return;
				} else {
					quantidade -= itemNoInventario.getAmount();
					inventario.clear(i);
				}
			}
		}
	}

}
