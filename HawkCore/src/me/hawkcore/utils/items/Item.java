package me.hawkcore.utils.items;

import java.util.ArrayList;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
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
