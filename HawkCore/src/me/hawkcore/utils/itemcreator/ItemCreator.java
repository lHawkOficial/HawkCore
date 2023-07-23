package me.hawkcore.utils.itemcreator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.Setter;
import me.hawkcore.Core;
import me.hawkcore.utils.API;
import me.hawkcore.utils.Save;
import me.hawkcore.utils.items.Item;

@Getter
@Setter
public class ItemCreator {

	private String nome;
	private ItemStack item;
	private File file;
	private File folder;
	
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
	
	public ItemCreator(String nome, ItemStack item, File folder) {
		this.item = item;
		this.nome = nome;
		this.folder = folder;
		file = new File(folder + "/" + nome + ".json");
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
	
	
	
	public static List<ItemCreator> getItemCreatorFromFolder(File folder){
		List<ItemCreator> items = new ArrayList<>();
		for(ItemCreator ic : ManagerItemCreator.get().getItems()) {
			if (ic.getFolder() == null || !ic.getFolder().equals(folder)) continue;
			items.add(ic);
		}
		return items;
	}
	
	public static void setup() {
		File pasta = new File(Core.getInstance().getDataFolder() + "/itemcreator");
		if (!pasta.exists()) {
			pasta.mkdir();
			new ItemCreator("itemMissionExample", new Item("2>0").build());
		}
		File[] filesMissions = pasta.listFiles();
		Arrays.sort(filesMissions, (f1, f2) -> f1.getName().compareToIgnoreCase(f2.getName()));
		for(File file : filesMissions) {
			String name = file.getName().replace(".json", new String());
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				Arrays.sort(files, (f1, f2) -> f1.getName().compareToIgnoreCase(f2.getName()));
				loop: for(File f : files) {
					if (!f.getName().endsWith(".json")) continue loop;
					name = f.getName().replace(".json", new String());
					new ItemCreator(name, null, file);
					continue loop;
				}
				continue;
			}
			if (!file.getName().endsWith(".json")) continue;
			new ItemCreator(name, null);
		}
	}
	
	public static ManagerItemCreator get() {
		return ManagerItemCreator.get();
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
