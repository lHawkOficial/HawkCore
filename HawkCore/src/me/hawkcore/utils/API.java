package me.hawkcore.utils;

import java.io.ByteArrayInputStream;













import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.schematic.SchematicFormat;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import me.hawkcore.Core;
import me.hawkcore.utils.items.Item;
import me.hawkcore.utils.items.SkullCreator;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.NavigationAbstract;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PathEntity;

@SuppressWarnings("deprecation")
public class API {

	public static API get() {
		return Core.getInstance().getApi();
	}
	
	public boolean containsRegion(Location loc, List<String> names) {
		ApplicableRegionSet aregion = WorldGuardPlugin.inst().getRegionManager(loc.getWorld()).getApplicableRegions(loc.clone());
		for(String txt : names) {
			for(ProtectedRegion region : aregion.getRegions()) {
				if (region.getId().equalsIgnoreCase(txt)) return true;
			}
		}
		return false;
	}
	
	public boolean containsRegion(Location loc) {
		ApplicableRegionSet aregion = WorldGuardPlugin.inst().getRegionManager(loc.getWorld()).getApplicableRegions(loc.clone());
		for(String txt : ConfigGeral.get().getRegionsDisables()) {
			for(ProtectedRegion region : aregion.getRegions()) {
				if (region.getId().equalsIgnoreCase(txt)) return true;
			}
		}
		return false;
	}
	
	public void makeEntityMoveTo(Entity entity, Location loc, double speed) {
		NavigationAbstract na = ((EntityInsentient)((CraftEntity) entity).getHandle()).getNavigation();
		PathEntity path = na.a(loc.getX(), loc.getY(), loc.getZ());
        if (path != null) {
            na.a(path, speed);
        }
	}
	
	public void pasteSchematic(Location loc, File file, boolean randomRotate) {
		try {
			SchematicFormat sf = SchematicFormat.getFormat(file);
			if (sf==null) return;
			CuboidClipboard cc = sf.load(file);
			if (randomRotate) {
				for (int i = 0; i < 4; i++) {
					if (new Random().nextBoolean()) cc.rotate2D(90);
				}
			}
			EditSession session = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(loc.getWorld()), Integer.MAX_VALUE);
			cc.paste(session, BukkitUtil.toVector(loc), true);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String serializeItem(ItemStack is) {
	    try {
	        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
	        BukkitObjectOutputStream out = new BukkitObjectOutputStream(bytesOut);
	        out.writeObject(is);
	        out.flush();
	        out.close();
	        return Base64Coder.encodeLines(bytesOut.toByteArray());
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        return null;
	    }
	}
	
	public ItemStack unserializeItem(String base64) {
	    try {
	        byte[] data = Base64Coder.decodeLines(base64);
	        ByteArrayInputStream bytesIn = new ByteArrayInputStream(data);
	        BukkitObjectInputStream in = new BukkitObjectInputStream(bytesIn);
	        ItemStack is = (ItemStack) in.readObject();
	        in.close();
	        return is;
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        return null;
	    }
	}
	
	public boolean isInteger(String txt) {
		try {
			Integer.valueOf(txt);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isDouble(String txt) {
		try {
			Double.valueOf(txt);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public double formatValue(double valor) {
		NumberFormat formatter = new DecimalFormat("0.00");
		return Double.valueOf(formatter.format(valor).replace(",", "."));
	}
	
	public Location getLocationInFrontEntity(Entity entity, double multiply) {
		return entity.getLocation().clone().add(entity.getLocation().clone().getDirection().multiply(multiply));
	}
	
	public ItemStack getItemStack(String linha) {
		if (linha.equalsIgnoreCase("player")) return new ItemStack(Material.STONE);
		try {
			String[] args = linha.split(">");
			Item item = new Item(Integer.valueOf(args[0]), Integer.valueOf(args[1]));
			return item.build();
		} catch (Exception e) {
			return SkullCreator.itemFromBase64(linha);
		}
	}
	
	public Boolean isInventoryClean(Player p) {
		List<ItemStack> items = new ArrayList<>();
		for(ItemStack item : p.getInventory().getContents()) {
			if (item != null && item.getType() != Material.AIR) items.add(item);
		}
		for(ItemStack item : p.getInventory().getArmorContents()) {
			if (item != null && item.getType() != Material.AIR) items.add(item);
		}
		return items.isEmpty();
	}
	
	public void sendActionBarMessage(Player player, String message) {
		PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(message), (byte) 2);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}
	
	public List<Location> getLocsAround(Location location, float raio) {
		Location loc = location.clone();
		List<Location> list = new ArrayList<>();
		for (int i = 0; i < 360; i++) {
			double x = raio * Math.cos(i);
			double z = raio * Math.sin(i);
			loc.add(x,0,z);
			list.add(loc.clone());
			loc.subtract(x,0,z);
		}
		return list;
	}
	
	public String serialize(Location loc) {
		return loc.getWorld().getName() + ":" + loc.getX() + ":" + loc.getY() + ":" + loc.getZ() + ":" + loc.getYaw()
				+ ":" + loc.getPitch();
	}

	public String formatTime(int segundos) {
		int seconds = segundos;
		long DD = segundos / 86400;
		long HH = seconds / 3600;
		long MM = (seconds % 3600) / 60;
		long SS = seconds % 60;
		String data = " ";
		if (DD > 0) data+=" "+DD+"d";
		if (HH > 0) data+=" "+HH+"h";
		if (MM > 0) data+=" "+MM+"m";
		if (SS > 0) data+=" "+SS+"s";
		while(data.startsWith(" ")) {
			data = data.replaceFirst(" ", new String());
		}
		return data.length() > 0 ? data : "0s";
	}
	
	public String getData() {
		return new SimpleDateFormat("dd/MM/yyyy - HH:mm").format(Calendar.getInstance().getTime());
	}

	public String serializeItems(ItemStack[] items) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
			dataOutput.writeInt(items.length);
			for (int i = 0; i < items.length; i++) {
				dataOutput.writeObject(items[i]);
			}
			dataOutput.close();
			return Base64Coder.encodeLines(outputStream.toByteArray());
		} catch (Exception e) {
			return null;
		}
	}

	public ItemStack[] unserializeItems(String data) {
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
			BukkitObjectInputStream dataInput = null;
			try {
				dataInput = new BukkitObjectInputStream(inputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
			ItemStack[] items = null;
			try {
				items = new ItemStack[dataInput.readInt()];
			} catch (IOException e) {
				e.printStackTrace();
			}
			for (int i = 0; i < items.length; i++) {
				try {
					items[i] = (ItemStack) dataInput.readObject();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				dataInput.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return items;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public final org.bukkit.block.Block getTargetBlock(Player player, int range) {
		BlockIterator iter = new BlockIterator(player, range);
		org.bukkit.block.Block lastBlock = iter.next();
		while (iter.hasNext()) {
			lastBlock = iter.next();
			if (lastBlock.getType() == Material.AIR)
				continue;
			break;
		}
		return lastBlock;
	}

	public Location unserialize(String linha) {
		String[] args = linha.split(":");
		World world = Bukkit.getWorld(args[0]);
		double x = Double.valueOf(args[1]);
		double y = Double.valueOf(args[2]);
		double z = Double.valueOf(args[3]);
		float yaw = Float.valueOf(args[4]);
		float pitch = Float.valueOf(args[5]);
		return new Location(world, x, y, z, yaw, pitch);
	}

	public String descriptografar(String linha) {
		return new String(Base64.getDecoder().decode(linha));
	}

	public String criptografar(String linha) {
		return Base64.getEncoder().encodeToString(linha.getBytes());
	}

}
