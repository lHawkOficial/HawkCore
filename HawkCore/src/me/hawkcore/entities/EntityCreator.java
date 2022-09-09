package me.hawkcore.entities;

import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import lombok.Getter;
import lombok.Setter;
import me.hawkcore.Core;
import me.hawkcore.utils.Save;

@Getter
@Setter
public class EntityCreator {

	private File file;
	private Entity entity;
	
	public EntityCreator(Location loc, EntityType type) {
		try {
			this.entity = loc.getWorld().spawnEntity(loc, type);
		} catch (Exception e) {
			return;
		}
		save();
	}
	
	public void save() {
		file = new File(Core.getInstance().getDataFolder() + "/entities.yml");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		List<Object> lista = Save.load(file);
		if (lista==null) lista = new ArrayList<>();
		lista.add(this.getEntity().getUniqueId().toString());
		new Save(file, lista);
	}
	
	public static void deleteAll() {
		File file = new File(Core.getInstance().getDataFolder() + "/entities.yml");
		if (!file.exists()) return;
		List<Object> lista = Save.load(file);
		if (lista == null) return;
		if (lista.isEmpty()) return;
		for(Object o : lista) {
			UUID uuid = UUID.fromString((String) o);
			for(World world : Bukkit.getWorlds()) {
				for(Entity entity : world.getEntities()) {
					if (entity.getUniqueId().equals(uuid)) entity.remove();
				}
			}
		}
		new Save(file, new ArrayList<>());
	}
	
}
