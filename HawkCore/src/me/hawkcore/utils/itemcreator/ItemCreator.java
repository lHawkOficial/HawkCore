package me.hawkcore.utils.itemcreator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import me.hawkcore.Core;
import me.hawkcore.utils.API;
import me.hawkcore.utils.Save;
import me.hawkcore.utils.items.Item;

@Getter
public class ItemCreator {

	private String nome;
	private ItemStack item;
	private File file;
	
	public ItemCreator(String nome, ItemStack item) {
		this.item = item;
		this.nome = nome;
		file = new File(Core.getInstance().getDataFolder() + "/itemcreator");
		if (!file.exists()) file.mkdir();
		file = new File(file + "/" + nome + ".json");
		if (!file.exists()) {
			try {
				file.createNewFile();
				save();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		load();
		this.item.setAmount(1);
		ManagerItemCreator.get().getItems().add(this);
	}
	
	public static void setup() {
		File pasta = new File(Core.getInstance().getDataFolder() + "/itemcreator");
		if (!pasta.exists()) {
			pasta.mkdir();
			new ItemCreator("itemMissionExample", new Item("2>0").build());
		}
		for(File file : pasta.listFiles()) {
			if (!file.getName().endsWith(".json")) continue;
			String name = file.getName().replace(".json", new String());
			new ItemCreator(name, null);
		}
	}
	
	public void delete() {
		ManagerItemCreator.get().getItems().remove(this);
		file.delete();
	}
	
	public void save() {
		List<Object> lista = new ArrayList<>();
		lista.add(API.get().serializeItem(item.clone()));
		new Save(file, lista);
	}
	
	public void load() {
		List<Object> lista = Save.load(file);
		if (lista == null) return;
		if (lista.isEmpty()) return;
		this.item = API.get().unserializeItem((String) lista.get(0));
	}
	
}
