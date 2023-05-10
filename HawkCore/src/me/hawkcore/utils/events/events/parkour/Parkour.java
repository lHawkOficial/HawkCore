package me.hawkcore.utils.events.events.parkour;


import java.io.File;





import java.util.ArrayList;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import lombok.Getter;
import lombok.Setter;
import me.HClan.ListenersPlugin.PlayerDamageClanAlly;
import me.HClan.ListenersPlugin.PlayerDamageClanMember;
import me.HTags.ListenersPlugin.PlayerUpdateTagEvent;
import me.HTags.Objects.Tag;
import me.hawkcore.Core;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.API;
import me.hawkcore.utils.ConfigGeral;
import me.hawkcore.utils.Eco;
import me.hawkcore.utils.Save;
import me.hawkcore.utils.Scoreboard;
import me.hawkcore.utils.events.EventManager;
import me.hawkcore.utils.events.events.parkour.menus.MenuParkour;
import me.hawkcore.utils.events.events.parkour.utils.ConfigParkour;
import me.hawkcore.utils.events.events.parkour.utils.MensagensParkour;
import me.hawkcore.utils.events.utils.Event;
import me.hawkcore.utils.events.utils.RankingEvent;
import me.hawkcore.utils.events.utils.enums.EventStatus;
import me.hawkcore.utils.events.utils.enums.EventType;
import me.hawkcore.utils.events.utils.enums.PlayerType;
import me.hawkcore.utils.events.utils.interfaces.EventExecutor;
import me.hawkcore.utils.events.utils.interfaces.EventListeners;
import me.hawkcore.utils.events.utils.listeners.ChangeTopEvent;
import me.hawkcore.utils.items.Item;
import me.hawkcore.utils.locations.Distance;
import me.hawkcore.utils.menus.MenuAPI;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

@Getter
public class Parkour extends Event implements EventExecutor, EventListeners {

	private Task task;
	private ConfigParkour configparkour;
	private String timeRestante = "N.A";
	private MenuParkour menu;
	private Item iconLeave,
	IconSetCheckpoint,
	IconTeleportCheckpoint;
	private Player win;
	@Setter
	private Location locationFinish;
	
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
			Location checkpoint = getCheckpoint(score.player);
			for (int i = 0; i < configparkour.getScore_lines().size(); i++) {
				score.setLine(i, configparkour.getScore_lines().get(i).replace("{total}", String.valueOf(getPlayers().size()))
						.replace("{tempo}", timeRestante)
						.replace("{checkpoint}", "X:" + checkpoint.getBlockX() + " Y:" + checkpoint.getBlockY() + " Z:"+checkpoint.getBlockZ())
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
		Eco.get().withdrawPlayer(p, configparkour.getValueJoin());
		Object[] objects = new Object[3];
		objects[0] = p.getInventory().getArmorContents().clone();
		objects[1] = p.getInventory().getContents().clone();
		objects[2] = getLocationStart().clone();
		p.setMetadata("parkour", new FixedMetadataValue(Core.getInstance(), objects));
		p.getInventory().setArmorContents(new ItemStack[4]);
		p.getInventory().setContents(new ItemStack[9*4]);
		p.getInventory().setItem(iconLeave.getSlot(), iconLeave.build());
		p.updateInventory();
		p.setFoodLevel(20);
		p.setGameMode(GameMode.SURVIVAL);
		p.setAllowFlight(false);
		Tag.updateAllTag();
	}
	
	@Override
	public void removePlayerFromEvent(Player p) {
		EventExecutor.super.removePlayerFromEvent(p);
		removeScoreBoard(p);
		teleportPlayer(p, getLocationExit());
		p.getInventory().setContents(getContent(p));
		p.getInventory().setArmorContents(getArmor(p));;
		p.removeMetadata("parkour", Core.getInstance());
		if (getEventStatus() != EventStatus.CLOSED) p.sendMessage(MensagensParkour.get().getExit());
		Tag.updateAllTag();
		getPlayers().remove(p);
		p.updateInventory();
	}

	@Override
	public void removePlayerFromEspectator(Player p) {
	}

	@Override public void closed() {}

	@Override public void forceStart() {}

	@Override public void warning() {}

	@Override
	public void start() {
		if (EventManager.get().hasEventPlaying()) return;
		if (getEventStatus() != EventStatus.STOPPED || !isConfigured()) return;
		if (task != null) task.cancel();
		setEventStatus(EventStatus.WARNING);
		List<Location> circle = API.get().getLocsAround(getLocationFinish().clone().add(0,0.25f,0), 0.5f);
		task = new Task(new Runnable() {
			long timeWarn,updateScores, time;
			int warns = configparkour.getAmountWarn();
			int i = 0;
			@Override
			public void run() {
				switch (getEventStatus()) {
				case WARNING:
					if ((configparkour.getTimeWarn() - (int)TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()-timeWarn) <= 0)) {
						timeWarn = System.currentTimeMillis();
						if (warns > 0) {
							Bukkit.getOnlinePlayers().forEach(p -> {
								MensagensParkour.get().getOpen().forEach(msg -> p.sendMessage(msg
										.replace("{total}", String.valueOf(getPlayers().size()))
										.replace("{tempo}", String.valueOf(warns))
										.replace("{valor}", Eco.get().format(configparkour.getValueJoin()))));
								p.playSound(p.getLocation(), Sound.NOTE_BASS, 0.5f, 0.5f);
							});
							warns--;
						} else {
							setEventStatus(EventStatus.INGAME);
							time = System.currentTimeMillis();
							timeRestante = API.get().formatTime((configparkour.getTime()*60 - (int)TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()-time)));
							Bukkit.getOnlinePlayers().forEach(p -> {
								MensagensParkour.get().getClosed().forEach(msg -> p.sendMessage(msg
										.replace("{tempo}", timeRestante)));
								p.playSound(p.getLocation(), Sound.NOTE_BASS, 0.5f, 0.5f);
							});
							for(Player p : getPlayers().keySet()) {
								p.getInventory().setItem(IconSetCheckpoint.getSlot(), IconSetCheckpoint.build());
								p.getInventory().setItem(IconTeleportCheckpoint.getSlot(), IconTeleportCheckpoint.build());
								p.updateInventory();
								teleportPlayer(p, getLocationStart());
							}
						}
					}
					break;
				case INGAME:
					timeRestante = API.get().formatTime((configparkour.getTime()*60 - (int)TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()-time)));
					for(Player p : getPlayers().keySet()) {
						Distance distance = new Distance(p.getLocation(), locationFinish);
						if (distance.value() <= 0.5) {
							win = p;
							break;
						}
					}
					if (TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis()-time) >= configparkour.getTime() || getPlayers().size() == 0 || win != null) {
						time = -1;
						task.cancel();
						finish();
					}
					break;
				default:
					finish();
				}
				if (getEventStatus() != EventStatus.STOPPED) {
					if (configparkour.isScore_active() && System.currentTimeMillis()-updateScores >= 1000) {
						updateScores = System.currentTimeMillis();
						updateScore();
					}
					if (i < circle.size()) {
						Location loc = circle.get(i);
						for(Player p : getPlayers().keySet()) {
							if (!p.getWorld().equals(loc.getWorld())) continue;
							CraftPlayer cp = (CraftPlayer) p;
							PacketPlayOutWorldParticles particle = new PacketPlayOutWorldParticles(EnumParticle.FIREWORKS_SPARK, false, (float)loc.getX(), (float)loc.getY(), (float)loc.getZ(), 0, 0, 0, 0, 1);
							cp.getHandle().playerConnection.sendPacket(particle);
						}
						i++;
					}else i=0;
				}
			}
		});
	}
	
	@Override
	public void stop() {
		if (getEventStatus() == EventStatus.STOPPED) return;
		if (task != null) task.cancel();
		setEventStatus(EventStatus.STOPPED);
		Task.run(()->Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ConfigGeral.get().getCommand_reloadScore()));
	}
	
	@Override
	public void finish() {
		if (getEventStatus() == EventStatus.STOPPED) return;
		stop();
		setEventStatus(EventStatus.CLOSED);
		if (win != null) {
			Bukkit.getOnlinePlayers().forEach(p -> {
				MensagensParkour.get().getFinish().forEach(msg -> p.sendMessage(msg
						.replace("{player}", win.getName()).replace("{total}", String.valueOf(getPlayers().size()))));
				p.playSound(p.getLocation(), Sound.NOTE_BASS, 0.5f, 0.5f);
			});
			runRewardToPlayer(win, configparkour.getRewards());
			String name = win.getName();
			RankingEvent ranking = getRanking();
			ranking.getTops().put(name.toLowerCase(), ranking.getTops().containsKey(name.toLowerCase()) ? ranking.getTops().get(name.toLowerCase())+1 : 1);
		}else {
			Bukkit.getOnlinePlayers().forEach(p -> {
				MensagensParkour.get().getStop().forEach(msg -> p.sendMessage(msg));
				p.playSound(p.getLocation(), Sound.NOTE_BASS, 0.5f, 0.5f);
			});
		}
		while(!getPlayers().isEmpty()) {
			removePlayerFromEvent((Player) getPlayers().keySet().toArray()[0]);
		}
		setEventStatus(EventStatus.STOPPED);
		setLastStart(System.currentTimeMillis());
		getRanking().update();
		getPlayers().clear();
		Event.clearDatas();
		Tag.updateAllTag();
		win = null;
		timeRestante = "N.A";
		save();
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
		lista.add(getLocationFinish() == null ? "N.A" : API.get().serialize(getLocationFinish()));
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
		setLocationFinish(((String)lista.get(3)).equalsIgnoreCase("N.A")?null:API.get().unserialize(((String)lista.get(3))));
		List<Object> objects = (List<Object>) lista.get(4);
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
		if (args[0].equalsIgnoreCase("sair")) {
			removePlayerFromEvent(p);
			return;
		}
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
	
	@Override
	public void damage(EntityDamageEvent e) {
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

	@Override public void onBreakBlock(BlockBreakEvent e) {
		e.setCancelled(true);
	}

	@Override public void onPlaceBlock(BlockPlaceEvent e) {
		e.setCancelled(true);
	}

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

	public boolean isConfigured() {
		return getLocationExit() != null && getLocationLobby() != null && getLocationStart() != null && getLocationFinish() != null;
	}
	
	@Override
	public void playerDropItem(PlayerDropItemEvent e) {
		e.setCancelled(true);
		e.getPlayer().updateInventory();
	}

	@Override
	public void tagUpdate(PlayerUpdateTagEvent e) {
		Player p = e.getJogador().getPlayer();
		Player target = e.getReflection();
		if (!containsPlayerOnEvent(p)) return;
		if (!containsPlayerOnEvent(target)) return;
		e.setSuffix(new String());
		if (getEventStatus() == EventStatus.WARNING)
			e.setPrefix("§7[Parkour] ");
		else if (getEventStatus() == EventStatus.INGAME) {
			e.setPrefix("§e");		
		}
	}

	@Override
	public void updatePlayersEveryTime() {}

	@Override
	public void pickItemEvent(PlayerPickupItemEvent e) {
		e.setCancelled(true);
	}

	@Override
	public void damageClanAlly(PlayerDamageClanAlly e) {}

	@Override
	public void damageClanMember(PlayerDamageClanMember e) {}

}
