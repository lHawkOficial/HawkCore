package me.hawkcore.entities;

import java.io.File;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.metadata.FixedMetadataValue;

import lombok.Getter;
import lombok.Setter;
import me.hawkcore.Core;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.Save;

@Getter
@Setter
public class EntityCreator {

	private File file;
	private Entity entity;
	
	public EntityCreator(Location loc, EntityType type) {
		try {
			this.entity = loc.getWorld().spawnEntity(loc, type);
			this.entity.setMetadata("entitycreator", new FixedMetadataValue(Core.getInstance(), entity.getUniqueId()));
		} catch (Exception e) {
			return;
		}
		Task.runAsync(()-> {
			save();
			loc.getWorld().save();
		});
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
		lista.add(entity.getLocation().getChunk().getX() + ":"+entity.getLocation().getChunk().getZ()+":"+this.getEntity().getUniqueId().toString());
		new Save(file, lista);
	}
	
	public static void deleteAll() {
		File file = new File(Core.getInstance().getDataFolder() + "/entities.yml");
		if (!file.exists()) return;
		List<Object> lista = Save.load(file);
		if (lista == null) return;
		if (lista.isEmpty()) return;
		for(Object o : lista) {
			String line = (String)o;
			String[] args = line.split(":");
			UUID uuid = UUID.fromString(args[2]);
			int x = Integer.valueOf(args[0]);
			int z = Integer.valueOf(args[1]);
			for(World world : Bukkit.getWorlds()) {
				world.loadChunk(x, z);
				Chunk chunk = world.getChunkAt(x, z);
				for(Entity entity : chunk.getEntities()) {
					if (entity.getUniqueId().equals(uuid)) {
						entity.remove();
					}
				}
			}
		}
		new Save(file, new ArrayList<>());
	}
	
}
