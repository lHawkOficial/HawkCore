package me.hawkcore;








import java.io.File;



import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import lombok.Setter;
import me.hawkcore.commands.CoreCommand;
import me.hawkcore.commands.DesenchantCommand;
import me.hawkcore.commands.ItemCreatorCommand;
import me.hawkcore.commands.MissionCommand;
import me.hawkcore.entities.EntityCreator;
import me.hawkcore.entities.listeners.Listeners;
import me.hawkcore.tasks.Task;
import me.hawkcore.tasks.TaskManager;
import me.hawkcore.utils.API;
import me.hawkcore.utils.ConfigGeral;
import me.hawkcore.utils.Mensagens;
import me.hawkcore.utils.PlaceHolders;
import me.hawkcore.utils.boosbar.ListenerBar;
import me.hawkcore.utils.configs.ConfigCommands;
import me.hawkcore.utils.events.CommandEvents;
import me.hawkcore.utils.events.EventManager;
import me.hawkcore.utils.events.events.bolao.utils.BolaoAPI;
import me.hawkcore.utils.events.events.fight.utils.FightAPI;
import me.hawkcore.utils.events.events.parkour.utils.ParkourAPI;
import me.hawkcore.utils.events.utils.Event;
import me.hawkcore.utils.events.utils.MenuEvents;
import me.hawkcore.utils.fragments.Fragment;
import me.hawkcore.utils.itemcreator.ItemCreator;
import me.hawkcore.utils.itemcreator.ManagerItemCreator;
import me.hawkcore.utils.missions.ManagerMissions;
import me.hawkcore.utils.missions.listeners.MenuListeners;
import me.hawkcore.utils.missions.listeners.PlayerListener;
import me.hawkcore.utils.missions.objects.ConfigMission;
import me.hawkcore.utils.playersdata.listeners.PlayerDataListener;
import me.hawkcore.utils.playersdata.managers.ManagerData;
import me.hawkcore.utils.playersdata.objects.PlayerData;
import me.hawkcore.utils.playersdata.utils.MensagensThirstHeat;
import me.hawkcore.utils.showonline.events.EventsShow;
import me.hawkcore.verifies.PluginVerifier;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;

@Getter
@Setter
public class Core extends JavaPlugin {
	
	private ManagerMissions managermissions;
	private ManagerItemCreator manageritemcreator;
	private ManagerData managerdata;
	private EventManager eventmanager;
	private TaskManager taskmanager;
	private Economy econ;
	private String tag = "§7[⚒]", version = "§dv" + getDescription().getVersion();
	private API api;
	private ConfigMission configmission;
	private ConfigGeral configgeral;
	private MensagensThirstHeat mensagensthirstheat;
	private Mensagens mensagens;
	private MenuEvents menuevents;
	
	@Getter
	private static Core instance;
	
	@Override
	public void onEnable() {
		long time = System.currentTimeMillis();
		sendConsole(tag + " &3HawkCore carregando dados.");
		if (!new PluginVerifier("Vault", "&cHawkCore foi desligado por falta da dependência Vault!").queue()) return;
		if (!new PluginVerifier("PlaceholderAPI", "&cHawkCore foi desligado por falta da dependência PlaceholderAPI!").queue()) return;
		if (!new PluginVerifier("nChat", "&cHawkCore foi desligado por falta da dependência nChat!").queue()) return;
		if (!new PluginVerifier("WorldEdit", "&cHawkCore foi desligado por falta da dependência WorldEdit!").queue()) return;
		if (!new PluginVerifier("H_Clan", "&cHawkCore foi desligado por falta da dependência H_Clan!").queue()) return;
		if (!new PluginVerifier("H_Tags", "&cHawkCore foi desligado por falta da dependência H_Tags!").queue()) return;
		saveDefaultConfig();
		instance = this;
		initFiles();
		taskmanager = new TaskManager();
		taskmanager.runTaskTimerAsynchronously(this, 0, 1);
		api = new API();
		managermissions = new ManagerMissions();
		manageritemcreator = new ManagerItemCreator();
		eventmanager = new EventManager();
		configgeral = new ConfigGeral();
		configmission = new ConfigMission();
		managerdata = new ManagerData();
		mensagensthirstheat = new MensagensThirstHeat();
		mensagens = new Mensagens();
		ItemCreator.setup();
		new CoreCommand();
		new ItemCreatorCommand();
		new ListenerBar();
		new PlayerListener();
		new MenuListeners();
		new PlayerDataListener();
		new Listeners();
		new EventsShow();
		new Fragment();
		new me.hawkcore.utils.events.utils.listeners.PlayerListener();
		new PlaceHolders().register();
		if (configmission.isActiveMissions()) new MissionCommand();
		if (configgeral.getEnable_events()) {
			new BukkitRunnable() {
				@Override
				public void run() {
					cancel();
					new CommandEvents();
					setupEvents();
					menuevents = new MenuEvents(getConfig().getString("Config.MenuEvents.title").replace("&", "§"), getConfig().getInt("Config.MenuEvents.row"), getConfig().getStringList("Config.MenuEvents.Glass"));
				}
			}.runTaskLater(this, 15);
		}
		if (ConfigCommands.get().getActiveCommandDesenchant()) new DesenchantCommand();
		ManagerMissions.checkPlayers();
		PlayerData.checkAll();
		Task.run(()-> {
			RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
			this.econ = (Economy) rsp.getProvider();
			EntityCreator.deleteAll();
			sendConsole(tag + " &3HawkCore carregou todos os dados em &b" + (System.currentTimeMillis()-time) + "ms&3!");
		});
		
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
			if (all.hasMetadata("missionplayer")) {
				all.removeMetadata("missionplayer", instance);
			}
			if (all.hasMetadata("playerdata")) {
				all.removeMetadata("playerdata", instance);
			}
		}
		
		Event.clearDatas();
		HandlerList.unregisterAll(this);
		sendConsole(" ");
		sendConsole("&cHawkCore desligado com sucesso! &6[Author lHawk_] " + version);
		sendConsole(" ");
	}
	
	public void setupEvents() {
		eventmanager.getEvents().clear();
		BolaoAPI.checkFiles();
		ParkourAPI.checkFiles();
		FightAPI.checkFiles();
	}
	
	private void initFiles() {
		File folderMissions = new File(getDataFolder() + "/missions");
		if (!folderMissions.exists()) folderMissions.mkdir();
		
		File folderPlayersMissions = new File(folderMissions + "/players");
		if (!folderPlayersMissions.exists()) folderPlayersMissions.mkdir();
	}
	
	public void sendConsole(String msg) {Bukkit.getConsoleSender().sendMessage(msg.replace("&", "§"));}
	
}
