package me.hawkcore.utils.items;

import java.util.ArrayList;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.Getter;
import lombok.Setter;
import me.hawkcore.utils.API;

@Getter
@Setter
public class Item {

	private ItemStack item;
	private int slot, sort;
	private Double valor;
	private String permission;
	private Boolean player;
	
	public Item(ItemStack item) {
		this.item = item;
		this.slot = -1;
		this.player = false;
		this.permission = null;
		this.valor = 0.0;
	}
	
	public Item(String linha) {
		this.item = API.get().getItemStack(linha);
		this.slot = -1;
		this.player = false;
		this.permission = null;
		this.valor = 0.0;
	}
	
	@SuppressWarnings("deprecation")
	public Item(int ID, int DATA) {
		this.item = new ItemStack(Material.getMaterial(ID), 1, (short) DATA);
	}
	
	public Item(Material material, int DATA) {
		this.item = new ItemStack(material, 1, (short) DATA);
	}
	
	public int getAmount() {
		return item.getAmount();
	}
	
	@SuppressWarnings("deprecation")
	public static boolean isSimilar(ItemStack item, ItemStack stack) {
		if (stack == null || item == null) {
			return false;
		} else if (stack == item) {
			return true;
		} else {
			return item.getTypeId() == stack.getTypeId() && item.hasItemMeta() == stack.hasItemMeta()
			&& (!item.hasItemMeta() || Bukkit.getItemFactory().equals(item.getItemMeta(), stack.getItemMeta()));
		}
	}
	
	public static boolean isPlayerEquiped(Player p, ItemStack item) {
		for(ItemStack it : p.getInventory().getArmorContents()) {
			if (it == null || !isSimilar(it, item)) continue;
			return true;
		}
		return false;
	}
	
	public static int getAmount(Player p, ItemStack item) {
		int amount = 0;
		for(ItemStack it : p.getInventory().getContents()) {
			if (it == null || !isSimilar(item, it)) continue;
			amount += it.getAmount();
		}
		return amount;
	}
	
	public static boolean containsAmount(Player p, ItemStack item, int amountItem) {
		return getAmount(p, item) >= amountItem;
	}
	
	public static void removeAmount(Player p, ItemStack item, int amountToRemove) {
		int amount = getAmount(p, item);
		ItemStack itemClone = item.clone();
		itemClone.setAmount(1);
		for(ItemStack it : p.getInventory().getContents()) {
			if (it == null || !isSimilar(item, it)) continue;
			p.getInventory().removeItem(it);
		}
		int rest = amount-amountToRemove;
		if (rest>0) {
			for (int i = 0; i < rest; i++) {
				if (p.getInventory().firstEmpty() != -1) {
					p.getInventory().addItem(itemClone.clone());
				}else {
					p.getWorld().dropItem(p.getLocation(), itemClone.clone());
				}
			}
		}
		p.updateInventory();
	}
	
	@SuppressWarnings("deprecation")
	public static boolean isSimilarToBlock(ItemStack item, Block b) {
		if (item == null || b == null) return false;
		return item.getData().getData() == b.getData() && item.getType() == b.getType();
	}
	
	@SuppressWarnings("deprecation")
	public static boolean isSimilarMaterial(ItemStack item, ItemStack item1) {
		ItemStack it = item.clone();
		it.setDurability(item1.getDurability());
		return it.getData().getData() == item1.getData().getData() && it.getType() == item1.getType();
	}
	
	public Item(Material material) {
		this.item = new ItemStack(material);
	}
	
	public List<String> getLore() {
		return item.hasItemMeta() && item.getItemMeta().hasLore() ? item.getItemMeta().getLore() : new ArrayList<>();
	}
	
	public String getDisplayName() {
		return item.hasItemMeta() && item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : item.getType().toString().replace("_", " ");
	}
	
	public void setDisplayName(String name) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
	}
	
	public void setLore(List<String> lore) {
		ItemMeta meta = item.getItemMeta();
		meta.setLore(lore);
		item.setItemMeta(meta);
	}
	
	public void setLore(String... linha) {
		ItemMeta meta = item.getItemMeta();
		List<String> linhas = new ArrayList<>();
		for(String l : linha) {
			linhas.add(l);
		}
		meta.setLore(linhas);
		item.setItemMeta(meta);
	}
	
	public void setGlow() {
		if (item.getEnchantments().size() == 0) {
			item.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 1);
		}
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
	}
	
	public void setAmount(int amount) {
		item.setAmount(amount);
	}
	
	public void addEnchantment(Enchantment enchant, int level) {
		item.addUnsafeEnchantment(enchant, level);
	}
	
	public ItemStack build() {
		return item;
	}
	
}
