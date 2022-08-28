package me.hawkcore.utils.boosbar;


import java.util.concurrent.TimeUnit;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import me.hawkcore.Core;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.API;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EntityWither;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.PlayerConnection;

public class BossBar {

	public static void send(Player p, String msg, int seconds) {
		CraftPlayer cp = (CraftPlayer) p;
		EntityPlayer ep = cp.getHandle();
		PlayerConnection connection = ep.playerConnection;
		net.minecraft.server.v1_8_R3.World w = ((CraftWorld)cp.getWorld()).getHandle();
		EntityWither entity = new EntityWither(w);
		if (p.hasMetadata("barTitle")) {
			try {
				PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(((EntityWither)p.getMetadata("barTitle").get(0).value()).getId());
				connection.sendPacket(destroy);
			} catch (Exception e) {
			}
		}
		p.setMetadata("barTitle", new FixedMetadataValue(Core.getInstance(), entity));
		entity.setInvisible(true);
		entity.setHealth(entity.getMaxHealth());
		entity.setCustomNameVisible(true);
		entity.setCustomName(msg.replace("&", "§"));
		entity.setInvisible(true);
		entity.b(true);
		entity.setSize(0, 0);
		entity.r(1000);
		Location loc1 = API.get().getLocationInFrontEntity(p, 32);
		entity.setLocation(loc1.getX(), loc1.getY(), loc1.getZ(), 0, 0);
		
		PacketPlayOutSpawnEntityLiving spawn = new PacketPlayOutSpawnEntityLiving(entity);
		PacketPlayOutEntityMetadata update = new PacketPlayOutEntityMetadata(entity.getId(), entity.getDataWatcher(), false);
		PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(entity.getId());
		
		connection.sendPacket(spawn);
		connection.sendPacket(update);
		long time = System.currentTimeMillis();
		Task task = new Task();
		task.setRunnable(()-> {
			if (!p.isOnline()) {
				p.removeMetadata("barTitle", Core.getInstance());
				task.cancel();
				return;
			}
			if (!hasBoosBar(p) || !((Entity)p.getMetadata("barTitle").get(0).value()).equals(entity)) {
				task.cancel();
				connection.sendPacket(destroy);
				return;
			}
			int segundos = (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()-time);
			int porcentagem = segundos*100/seconds;
			float total = entity.getMaxHealth()*porcentagem/100;
			entity.setHealth(total+0.05f);
			entity.world = ((CraftWorld)p.getWorld()).getHandle();
			Location loc = API.get().getLocationInFrontEntity(p, 32);
			entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
			PacketPlayOutEntityTeleport teleport = new PacketPlayOutEntityTeleport(entity);
			connection.sendPacket(teleport);
			connection.sendPacket(update);
			if (segundos >= seconds) {
				task.cancel();
				connection.sendPacket(destroy);
				p.removeMetadata("barTitle", Core.getInstance());
			}
		}).run(1);
	}
	
	public static boolean hasBoosBar(Player p) {
		return p.hasMetadata("barTitle");
	}
	
	public static void send(Player p, String msg) {
		send(p, msg, 5);
	}
	
	public static void updateTextBar(Player p, String txt) {
		if (!hasBoosBar(p)) return;
		Entity entity = (Entity) p.getMetadata("barTitle").get(0).value();
		entity.setCustomName(txt.replace("&", "§"));
	}
	
}
