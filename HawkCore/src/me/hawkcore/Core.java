package me.hawkcore;






import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.hawkcore.commands.CoreCommand;
import me.hawkcore.commands.MissionCommand;
import me.hawkcore.tasks.TaskManager;
import me.hawkcore.utils.API;
import me.hawkcore.utils.boosbar.ListenerBar;
import me.hawkcore.utils.missions.ManagerMissions;
import me.hawkcore.utils.missions.listeners.PlayerListener;
import me.hawkcore.utils.missions.objects.ConfigMission;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;

@Getter
public class Core extends JavaPlugin {
	
	private String tag = "§7[⚒]", version = "§dv" + getDescription().getVersion();
	private TaskManager taskmanager;
	private ManagerMissions managermissions;
	private API api;
	private ConfigMission configmission;
	
	@Getter
	private static Core instance;
	
	@Override
	public void onEnable() {
		instance = this;
		saveDefaultConfig();
		api = new API();
		taskmanager = new TaskManager();
		taskmanager.runTaskTimerAsynchronously(this, 0, 1);
		managermissions = new ManagerMissions();
		configmission = new ConfigMission();
		new CoreCommand();
		new MissionCommand();
		new ListenerBar();
		new PlayerListener();
		ManagerMissions.checkPlayers();
		
		sendConsole(" ");
		sendConsole("&aHawkCore iniciado com sucesso! &6[Author lHawk_] " + version);
		sendConsole(" ");
		
	}
	
	@Override
	public void onDisable() {
		taskmanager.cancel();
		for(Player all : Bukkit.getOnlinePlayers()) {
			if (all.hasMetadata("barTitle")) {
				Entity entity = (Entity) all.getMetadata("barTitle").get(0).value();
				PacketPlayOutEntityDestroy a = new PacketPlayOutEntityDestroy(entity.getId());
				((CraftPlayer)all).getHandle().playerConnection.sendPacket(a);
				all.removeMetadata("barTitle", instance);
			}
			if (all.hasMetadata("pMissions")) {
				all.removeMetadata("pMissions", instance);
			}
		}
		sendConsole(" ");
		sendConsole("&cHawkCore desligado com sucesso! &6[Author lHawk_] " + version);
		sendConsole(" ");
	}
	
	private void sendConsole(String msg) {Bukkit.getConsoleSender().sendMessage(msg.replace("&", "§"));}
	
}
