package me.hawkcore.utils.events.events.parkour;


import java.io.File;



import java.util.ArrayList;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import lombok.Getter;
import me.HTags.ListenersPlugin.PlayerUpdateTagEvent;
import me.hawkcore.Core;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.API;
import me.hawkcore.utils.Eco;
import me.hawkcore.utils.Save;
import me.hawkcore.utils.Scoreboard;
import me.hawkcore.utils.events.EventManager;
import me.hawkcore.utils.events.events.parkour.menus.MenuParkour;
import me.hawkcore.utils.events.events.parkour.utils.ConfigParkour;
import me.hawkcore.utils.events.events.parkour.utils.MensagensParkour;
import me.hawkcore.utils.events.utils.Event;
import me.hawkcore.utils.events.utils.enums.EventStatus;
import me.hawkcore.utils.events.utils.enums.EventType;
import me.hawkcore.utils.events.utils.enums.PlayerType;
import me.hawkcore.utils.events.utils.interfaces.EventExecutor;
import me.hawkcore.utils.events.utils.interfaces.EventListeners;
import me.hawkcore.utils.events.utils.listeners.ChangeTopEvent;
import me.hawkcore.utils.items.Item;
import me.hawkcore.utils.menus.MenuAPI;

@Getter
public class Parkour extends Event implements EventExecutor, EventListeners {

	private Task task;
	private ConfigParkour configparkour;
	private String timeRestante = "N.A";
	private MenuParkour menu;
	private Item iconLeave,
	IconSetCheckpoint,
	IconTeleportCheckpoint;
	
	public Parkour(String name, File folder, FileConfiguration config, EventType type, boolean enabled) {
		super(name, folder, config, type, enabled);
		setupConfig();
		setupListeners();
	}

	public static Parkour get() {
		return (Parkour) EventManager.get().getEvent("parkour");
	}
	
	@Override
	public void setupConfig() {
		configparkour = new ConfigParkour(this);
		setConfigEvent(configparkour);
		setMessages(new MensagensParkour(this));
		setIcon(MenuAPI.getItemFromSection(getConfig().getConfigurationSection("Icon")));
		this.menu = new MenuParkour(getConfig().getString("MenuMain.title").replace("&", "§"), getConfig().getInt("MenuMain.row"), getConfig().getStringList("MenuMain.Glass"));
		File fileSaves = new File(getFolder() + "/saves.json");
		if (!fileSaves.exists()) {
			try {
				fileSaves.createNewFile();
				setFileSaves(fileSaves);
				save();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.iconLeave = MenuAPI.getItemFromSection(getConfig().getConfigurationSection("IconLeave"));
		this.IconSetCheckpoint = MenuAPI.getItemFromSection(getConfig().getConfigurationSection("IconSetCheckpoint"));
		this.IconTeleportCheckpoint = MenuAPI.getItemFromSection(getConfig().getConfigurationSection("IconTeleportCheckpoint"));
		setFileSaves(fileSaves);
		load();
	}
	
	@Override
	public void updateScore() {
		List<Scoreboard> scores = getScoreBoardPlayers();
		for(Scoreboard score : scores) {
			score.setObjectiveName(configparkour.getScore_title());
			for (int i = 0; i < configparkour.getScore_lines().size(); i++) {
				score.setLine(i, configparkour.getScore_lines().get(i).replace("{total}", String.valueOf(getPlayers().size()))
						.replace("{tempo}", timeRestante)
						.replace("{valor}", Eco.get().format(configparkour.getValueJoin())));
			}
			score.create();
		}
	}
	
	@Override
	public boolean containsPlayerOnEvent(Player p) {
		return getPlayers().containsKey(p);
	}
	
	@Override
	public void onTeleport(PlayerTeleportEvent e) {
		Player p = e.getPlayer();
		Task.run(()->{
			Scoreboard board = getScoreBoardPlayer(p.getName());
			if (board!=null)board.destroy();
		});
	}
	
	@Override
	public void addPlayerToEvent(Player p, Event event) {
		EventExecutor.super.addPlayerToEvent(p, event);
		teleportPlayer(p, getLocationLobby());
		getPlayers().put(p, PlayerType.PLAYING);
		if (!getRanking().getTops().containsKey(p.getName())) getRanking().getTops().put(p.getName().toLowerCase(), 0);
		Eco.get().withdrawPlayer(p, configparkour.getValueJoin());
		Object[] objects = new Object[2];
		objects[0] = p.getInventory().getArmorContents().clone();
		objects[1] = p.getInventory().getContents().clone();
		objects[2] = getLocationStart().clone();
		p.setMetadata("parkour", new FixedMetadataValue(Core.getInstance(), objects));
	}
	
	@Override
	public void removePlayerFromEvent(Player p) {
		EventExecutor.super.removePlayerFromEvent(p);
		removeScoreBoard(p);
		teleportPlayer(p, getLocationExit());
		p.getInventory().setContents(getContent(p));
		p.getInventory().setArmorContents(getArmor(p));;
		p.removeMetadata("parkour", Core.getInstance());
		p.sendMessage(MensagensParkour.get().getExit());
	}

	@Override
	public void removePlayerFromEspectator(Player p) {
	}

	@Override
	public void start() {
		
	}

	@Override
	public void closed() {
	}

	@Override
	public void forceStart() {
	}

	@Override
	public void warning() {
	}

	@Override
	public void stop() {
		
	}
	
	@Override
	public void finish() {
		
	}

	public Location getCheckpoint(Player p) {
		return (Location) ((Object[])p.getMetadata("parkour").get(0).value())[2];
	}
	
	public ItemStack[] getArmor(Player p) {
		return (ItemStack[]) ((Object[])p.getMetadata("parkour").get(0).value())[0];
	}
	
	public ItemStack[] getContent(Player p) {
		return (ItemStack[]) ((Object[])p.getMetadata("parkour").get(0).value())[1];
	}
	
	@Override
	public void onChat(ChatMessageEvent e) {
		Player p = e.getSender();
		if (!e.getTags().contains("parkour")) return;
		if (getRanking().getTop() == null) return;
		if (!getRanking().getTop().equalsIgnoreCase(p.getName())) return;
		e.setTagValue("parkour", configparkour.getTag_mito());
	}

	@Override
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		removeScoreBoard(p);
		removePlayerFromEvent(p);
	}

	@Override
	public void save() {
		List<Object> lista = new ArrayList<>();
		lista.add(getLocationExit() == null ? "N.A" : API.get().serialize(getLocationExit()));
		lista.add(getLocationLobby() == null ? "N.A" : API.get().serialize(getLocationLobby()));
		lista.add(getLocationStart() == null ? "N.A" : API.get().serialize(getLocationStart()));
		List<String> nomes = new ArrayList<>();
		for(String name : getRanking().getTops().keySet()) {
			nomes.add(name+":"+getRanking().getTops().get(name));
		}
		lista.add(nomes);
		new Save(getFileSaves(), lista);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void load() {
		List<Object> lista = Save.load(getFileSaves());
		if (lista == null || lista.isEmpty()) return;
		setLocationExit(((String)lista.get(0)).equalsIgnoreCase("N.A")?null:API.get().unserialize(((String)lista.get(0))));
		setLocationLobby(((String)lista.get(1)).equalsIgnoreCase("N.A")?null:API.get().unserialize(((String)lista.get(1))));
		setLocationStart(((String)lista.get(2)).equalsIgnoreCase("N.A")?null:API.get().unserialize(((String)lista.get(2))));
		List<Object> objects = (List<Object>) lista.get(3);
		for(Object object : objects) {
			String[] args = ((String)object).split(":");
			String name = args[0];
			int value = Integer.valueOf(args[1]);
			getRanking().getTops().put(name, value);
		}
		getRanking().update();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onRankingUpdate(ChangeTopEvent e) {
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.sendMessage(" ");
			p.sendMessage(configparkour.getMito().split("<nl>")[0].replace("{player}", e.getTop()));
			p.sendMessage(configparkour.getMito().split("<nl>")[1].replace("{player}", e.getTop()));
			p.sendMessage(" ");
			p.sendTitle(configparkour.getMito().split("<nl>")[0].replace("{player}", e.getTop()), configparkour.getMito().split("<nl>")[1].replace("{player}", e.getTop()));
			p.playSound(p.getLocation(), Sound.NOTE_PLING, 0.5f, 10);
		}
	}

	@Override public void addPlayerToEspectator(Player p) {}
	
	@Override public void onCommands(PlayerCommandPreprocessEvent e) {
		e.setCancelled(true);
		Player p = e.getPlayer();
		String[] args = e.getMessage().replaceFirst("/", new String()).split(" ");
		for(String cmd : configparkour.getCommands()) {
			if (cmd.equalsIgnoreCase(args[0])) {
				e.setCancelled(false);
				return;
			}
		}
		p.sendMessage(MensagensParkour.get().getCommandBloqued());
		p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
	}

	@Override public void onDamageEntity(EntityDamageByEntityEvent e) {
		e.setCancelled(true);
	}

	@Override public void onInteract(PlayerInteractEvent e) {
		e.setCancelled(true);
		Player p = e.getPlayer();		if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
			p.updateInventory();
			ItemStack item = p.getItemInHand();
			if (item == null) return;
			if (item.isSimilar(iconLeave.build())) {
				removePlayerFromEvent(p);
				return;
			}
			if (item.isSimilar(IconSetCheckpoint.build())) {
				Object[] objects = (Object[]) p.getMetadata("parkour").get(0).value();
				objects[2] = p.getLocation().clone();
				p.sendMessage(MensagensParkour.get().getCheckPointSet());
				p.playSound(p.getLocation(), Sound.LEVEL_UP, 0.5f, 10);
				return;
			}
			if (item.isSimilar(IconTeleportCheckpoint.build())) {
				teleportPlayer(p, getCheckpoint(p));;
				return;
			}
		}
	}

	@Override public void onBreakBlock(BlockBreakEvent e) {}

	@Override public void onPlaceBlock(BlockPlaceEvent e) {}

	@Override public void onDeath(PlayerDeathEvent e) {
		e.getDrops().clear();
		Player p = e.getEntity();
		p.spigot().respawn();
		p.teleport(p);
	}

	@Override public void onHunger(FoodLevelChangeEvent e) {
		e.setCancelled(true);
	}
	
	@Override public void onJoin(PlayerJoinEvent e) {}
	
	@Override public void onChatVanilla(AsyncPlayerChatEvent e) {}

	@Override public void onMove(PlayerMoveEvent e) {}

	@Override public void onClickInventory(InventoryClickEvent e) {
		e.setCancelled(true);
	}

	@Override
	public void playerDropItem(PlayerDropItemEvent e) {
		e.setCancelled(true);
		e.getPlayer().updateInventory();
	}

	@Override
	public void tagUpdate(PlayerUpdateTagEvent e) {
		if (getEventStatus() == EventStatus.WARNING)
			e.setPrefix("§f§k");
		else {
			e.setPrefix(getTag()+" §f");		
		}
	}

}
