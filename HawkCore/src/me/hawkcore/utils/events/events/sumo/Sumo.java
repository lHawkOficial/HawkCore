package me.hawkcore.utils.events.events.sumo;



import java.io.File;



import java.util.ArrayList;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
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
import me.hawkcore.utils.events.events.sumo.menus.MenuSumo;
import me.hawkcore.utils.events.events.sumo.utils.ConfigSumo;
import me.hawkcore.utils.events.events.sumo.utils.MensagensSumo;
import me.hawkcore.utils.events.utils.Event;
import me.hawkcore.utils.events.utils.RankingEvent;
import me.hawkcore.utils.events.utils.enums.EventStatus;
import me.hawkcore.utils.events.utils.enums.EventType;
import me.hawkcore.utils.events.utils.enums.PlayerType;
import me.hawkcore.utils.events.utils.interfaces.EventExecutor;
import me.hawkcore.utils.events.utils.interfaces.EventListeners;
import me.hawkcore.utils.events.utils.listeners.ChangeTopEvent;
import me.hawkcore.utils.items.Item;
import me.hawkcore.utils.menus.MenuAPI;

@Getter
@Setter
public class Sumo extends Event implements EventExecutor, EventListeners {

	private Task task;
	private ConfigSumo configsumo;
	private MenuSumo menu;
	private Item iconLeave;
	private Player win;
	private ItemStack[] armor_kit = new ItemStack[4], content_kit = new ItemStack[9*4];
	private Location locationEspectator,
	locationPlayer1, locationPlayer2;
	private long preparing, timeParty;
	private Player[] player = new Player[2];
	private boolean resetPlayers = false;
	
	public Sumo(String name, File folder, FileConfiguration config, EventType type, boolean enabled) {
		super(name, folder, config, type, enabled);
		setupConfig();
		setupListeners();
	}

	public static Sumo get() {
		return (Sumo) EventManager.get().getEvent("sumo");
	}
	
	@Override
	public void setupConfig() {
		configsumo = new ConfigSumo(this);
		setConfigEvent(configsumo);
		setMessages(new MensagensSumo(this));
		setIcon(MenuAPI.getItemFromSection(getConfig().getConfigurationSection("Icon")));
		this.menu = new MenuSumo(getConfig().getString("MenuMain.title").replace("&", "§"), getConfig().getInt("MenuMain.row"), getConfig().getStringList("MenuMain.Glass"));
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
		setFileSaves(fileSaves);
		load();
	}
	
	@Override
	public void updateScore() {
		List<Scoreboard> scores = getScoreBoardPlayers();
		for(Scoreboard score : scores) {
			score.setObjectiveName(configsumo.getScore_title());
			for (int i = 0; i < configsumo.getScore_lines().size(); i++) {
				score.setLine(i, configsumo.getScore_lines().get(i).replace("{total}", String.valueOf(getPlayers(PlayerType.PLAYING).size()))
						.replace("{valor}", Eco.get().format(configsumo.getValueJoin()))
						.replace("{players}", player[0] != null && player[1] != null ? player[0].getName() + " vs " + player[1].getName() : "aguardando")
						.replace("{player}", player[0] == null ? "aguardando" : player[0].getName())
						.replace("{player2}", player[1] == null ? "aguardando" : player[1].getName()));
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
			destroyScore(p);
			updateScore();
		});
	}
	
	public void destroyScore(Player p) {
		Scoreboard board = getScoreBoardPlayer(p.getName());
		if (board!=null)board.destroy();
	}
	
	@Override
	public void addPlayerToEvent(Player p, Event event) {
		EventExecutor.super.addPlayerToEvent(p, event);
		teleportPlayer(p, getLocationLobby());
		getPlayers().put(p, PlayerType.PLAYING);
		Object[] objects = new Object[3];
		objects[0] = p.getInventory().getArmorContents().clone();
		objects[1] = p.getInventory().getContents().clone();
		objects[2] = p.getLevel();
		p.setMetadata("sumo", new FixedMetadataValue(Core.getInstance(), objects));
		p.getInventory().setContents(new ItemStack[9*4]);
		p.getInventory().setArmorContents(new ItemStack[4]);
		p.updateInventory();
		p.setGameMode(GameMode.SURVIVAL);
		p.setTotalExperience(0);
		p.setLevel(0);
		updateScore();
		Tag.updateAllTag();
		updatePlayersEveryTime();
	}
	
	@Override
	public void addPlayerToEspectator(Player p) {
		EventExecutor.super.addPlayerToEvent(p, Sumo.get());
		getPlayers().put(p, PlayerType.ESPECTATING);
		Object[] objects = new Object[3];
		objects[0] = p.getInventory().getArmorContents().clone();
		objects[1] = p.getInventory().getContents().clone();
		objects[2] = p.getLevel();
		p.setMetadata("sumo", new FixedMetadataValue(Core.getInstance(), objects));
		p.getInventory().setContents(new ItemStack[9*4]);
		p.getInventory().setArmorContents(new ItemStack[4]);
		p.updateInventory();
		p.setTotalExperience(0);
		p.setLevel(0);
		updateScore();
		Tag.updateAllTag();
		updatePlayersEveryTime();
		p.setGameMode(GameMode.ADVENTURE);
		p.setAllowFlight(true);
		teleportPlayer(p, getLocationEspectator());
		p.sendMessage(MensagensSumo.get().getEspectatorJoin());
	}
	
	@Override
	public void removePlayerFromEvent(Player p) {
		EventExecutor.super.removePlayerFromEvent(p);
		if (getPlayers().get(p) == PlayerType.ESPECTATING) {
			Task.run(()->{
				for(Player all : Bukkit.getOnlinePlayers()) {
					if (all == p) continue;
					all.showPlayer(p);
				}
			});
			removePlayerFromEspectator(p);
			return;
		}
		getPlayers().remove(p);
		if (player[0] != null && player[1] != null) {
			Player win = null;
			if (player[0] == p) {
				player[1].getInventory().setContents(new ItemStack[9*4]);
				player[1].getInventory().setArmorContents(new ItemStack[4]);
				player[1].updateInventory();
				win = player[1];
				Task.run(()->{
					if (player[1] != null) player[1].teleport(getLocationStart());
				});
			}
			else if (player[1] == p) {
				player[0].getInventory().setContents(new ItemStack[9*4]);
				player[0].getInventory().setArmorContents(new ItemStack[4]);
				player[0].updateInventory();
				win = player[0];
				Task.run(()->{
					if (player[0] != null) player[1].teleport(getLocationStart());
				});
			}
			for(Player all : getPlayers().keySet()) {
				all.sendMessage(MensagensSumo.get().getPlayerLoss().replace("{player}", win.getName()));
			}
			Task.run(()->{
				resetPlayers = true;
				timeParty = System.currentTimeMillis();
			});
		}
		Object[] objects = (Object[]) p.getMetadata("sumo").get(0).value();
		p.getInventory().setArmorContents((ItemStack[]) objects[0]);
		p.getInventory().setContents((ItemStack[]) objects[1]);
		p.setLevel((int) objects[2]);
		p.removeMetadata("sumo", Core.getInstance());
		p.updateInventory();
		Tag.updateAllTag();
		updateScore();
		teleportPlayer(p, getLocationExit());
		if (getEventStatus() != EventStatus.INGAME) p.sendMessage(MensagensSumo.get().getExit());
	}

	@Override
	public void removePlayerFromEspectator(Player p) {
		EventExecutor.super.removePlayerFromEvent(p);
		getPlayers().remove(p);
		Object[] objects = (Object[]) p.getMetadata("sumo").get(0).value();
		p.getInventory().setArmorContents((ItemStack[]) objects[0]);
		p.getInventory().setContents((ItemStack[]) objects[1]);
		p.setLevel((int) objects[2]);
		p.removeMetadata("sumo", Core.getInstance());
		p.updateInventory();
		Tag.updateAllTag();
		updateScore();
		Task.run(()->{
			p.setGameMode(GameMode.SURVIVAL);
			p.setAllowFlight(false);
		});
		teleportPlayer(p, getLocationExit());
		p.sendMessage(MensagensSumo.get().getEspectatorLeft());
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
		task = new Task(new Runnable() {
			long timeWarn, updateScores, updatePlayers;
			int warns = configsumo.getAmountWarn();
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				switch (getEventStatus()) {
				case WARNING:
					if ((configsumo.getTimeWarn() - (int)TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()-timeWarn) <= 0)) {
						timeWarn = System.currentTimeMillis();
						if (warns > 0) {
							Bukkit.getOnlinePlayers().forEach(p -> {
								MensagensSumo.get().getOpen().forEach(msg -> p.sendMessage(msg
										.replace("{total}", String.valueOf(getPlayers().size()))
										.replace("{tempo}", String.valueOf(warns))
										.replace("{valor}", Eco.get().format(configsumo.getValueJoin()))));
								p.playSound(p.getLocation(), Sound.NOTE_BASS, 0.5f, 0.5f);
							});
							warns--;
						} else {
							for(Player p : getPlayers().keySet()) {
								if (getPlayers().get(p) == PlayerType.ESPECTATING) {
									teleportPlayer(p, getLocationEspectator());
								}else {
									teleportPlayer(p, getLocationStart());
								}
							}
							setEventStatus(EventStatus.INGAME);
							Bukkit.getOnlinePlayers().forEach(p -> {
								MensagensSumo.get().getClosed().forEach(msg -> p.sendMessage(msg));
								p.playSound(p.getLocation(), Sound.NOTE_BASS, 0.5f, 0.5f);
							});
						}
					}
					break;
				case INGAME:
					if (getTeleportQueue().isTeleporting()) return;
					if (System.currentTimeMillis()-timeParty < configsumo.getTimeOfPartyUpdate()) return;
					if (resetPlayers) {
						resetPlayers = false;
						player[0] = null;
						player[1] = null;
						return;
					}
					checkPlayers();
					List<Player> players = getPlayers(PlayerType.PLAYING);
					if (players.size() > 1 && (player[0] == null || player[1] == null)) {
						while((player[0] == null || player[1] == null)) {
							if (player[0] == null) player[0] = players.get(new Random().nextInt(players.size()));
							if (player[1] == null) player[1] = players.get(new Random().nextInt(players.size()));
						}
						if (player[0] == player[1]) {
							while(player[1] == player[0]) {
								player[1] = players.get(new Random().nextInt(players.size()));
							}
						}
						Bukkit.getOnlinePlayers().forEach(p -> {
							MensagensSumo.get().getVersus().forEach(msg -> p.sendMessage(msg
									.replace("{total}", String.valueOf(getPlayers().size()))
									.replace("{player}", player[0].getName())
									.replace("{player2}", player[1].getName())));
							p.playSound(p.getLocation(), Sound.VILLAGER_DEATH, 0.5f, 0.5f);
						});
						teleportPlayer(player[0], getLocationPlayer1());
						teleportPlayer(player[1], getLocationPlayer2());
						Task.run(()->{
							player[0].setHealth(player[0].getMaxHealth());
							player[0].getInventory().setContents(content_kit);
							player[0].getInventory().setArmorContents(armor_kit);
							player[0].updateInventory();
							player[1].setHealth(player[1].getMaxHealth());
							player[1].getInventory().setContents(content_kit);
							player[1].getInventory().setArmorContents(armor_kit);
							player[1].updateInventory();
							player[0].sendMessage(MensagensSumo.get().getPreparing());
							player[1].sendMessage(MensagensSumo.get().getPreparing());
							updatePlayersEveryTime();
							Tag.updateAllTag();
							destroyScore(player[0]);
							destroyScore(player[1]);
							for(Player p : getPlayers().keySet()) {
								p.sendTitle(MensagensSumo.get().getTitle().split("<nl>")[0].replace("{player}", player[0].getName()).replace("{player2}", player[1].getName()), MensagensSumo.get().getTitle().split("<nl>")[1].replace("{player}", player[0].getName()).replace("{player2}", player[1].getName()));
								p.playSound(p.getLocation(), Sound.BAT_LOOP, 0.5f, 10f);
							}
						});
						preparing = System.currentTimeMillis();
					} else if (players.size() <= 1) {
						win = players.isEmpty() ? null : players.get(0);
						finish();
					} else if (preparing != -1 && !getTeleportQueue().isTeleporting()) {
						if (configsumo.getTimePreparing() - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()-preparing) >= 0) {
							API.get().sendActionBarMessage(player[0], MensagensSumo.get().getPreparingTime().replace("{time}", API.get().formatTime((int) (configsumo.getTimePreparing() - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()-preparing)))));
							API.get().sendActionBarMessage(player[1], MensagensSumo.get().getPreparingTime().replace("{time}", API.get().formatTime((int) (configsumo.getTimePreparing() - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()-preparing)))));
						} else {
							player[0].sendMessage(MensagensSumo.get().getFight());
							player[0].playSound(player[0].getLocation(), Sound.IRONGOLEM_HIT, 0.5f, 0.5f);
							player[1].sendMessage(MensagensSumo.get().getFight());
							player[1].playSound(player[0].getLocation(), Sound.IRONGOLEM_HIT, 0.5f, 0.5f);
							preparing = -1;
						}
					}
					break;
				default:
					finish();
				}
				if (getEventStatus() != EventStatus.STOPPED && configsumo.isScore_active()) {
					if (System.currentTimeMillis()-updateScores >= 1000) {
						updateScores = System.currentTimeMillis();
						updateScore();
					}
					if (System.currentTimeMillis()-updatePlayers >= 5000) {
						updatePlayers = System.currentTimeMillis();
						Task.run(()->updatePlayersEveryTime());
					}
				}
			}
		});
	}
	
	public void checkPlayers() {
		if (player[0] == null || player[1] == null) return;
		String name = configsumo.getBlockName().toLowerCase();
		if (player[0].getLocation().getBlock().getType().toString().toLowerCase().contains(name)) {
			removePlayerFromEvent(player[0]);
			return;
		}
		if (player[1].getLocation().getBlock().getType().toString().toLowerCase().contains(name)) {
			removePlayerFromEvent(player[1]);
			return;
		}
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
		if (task != null) task.cancel();
		stop();
		setEventStatus(EventStatus.CLOSED);
		if (win != null) {
			Bukkit.getOnlinePlayers().forEach(p -> {
				MensagensSumo.get().getFinish().forEach(msg -> p.sendMessage(msg
						.replace("{player}", win.getName()).replace("{total}", String.valueOf(getPlayers().size()))));
				p.playSound(p.getLocation(), Sound.NOTE_BASS, 0.5f, 0.5f);
			});
			runRewardToPlayer(win, configsumo.getRewards());
			String name = win.getName();
			RankingEvent ranking = getRanking();
			ranking.getTops().put(name.toLowerCase(), ranking.getTops().containsKey(name.toLowerCase()) ? ranking.getTops().get(name.toLowerCase())+1 : 1);
		}else {
			Bukkit.getOnlinePlayers().forEach(p -> {
				MensagensSumo.get().getStop().forEach(msg -> p.sendMessage(msg));
				p.playSound(p.getLocation(), Sound.NOTE_BASS, 0.5f, 0.5f);
			});
		}
		while(!getPlayers().isEmpty()) {
			Player p = (Player) getPlayers().keySet().toArray()[0];
			removePlayerFromEvent(p);
			Task.run(()->{
				for(Player all : Bukkit.getOnlinePlayers()) {
					all.showPlayer(p);
					p.showPlayer(all);
				}
			});
		}
		setEventStatus(EventStatus.STOPPED);
		setLastStart(System.currentTimeMillis());
		getRanking().update();
		getPlayers().clear();
		Event.clearDatas();
		Tag.updateAllTag();
		preparing = -1;
		timeParty = -1;
		win = null;
		player[0] = null;
		player[1] = null;
		save();
	}
	
	@Override
	public void onChat(ChatMessageEvent e) {
		Player p = e.getSender();
		if (!e.getTags().contains("sumo")) return;
		if (getRanking().getTop() == null) return;
		if (!getRanking().getTop().equalsIgnoreCase(p.getName())) return;
		e.setTagValue("sumo", configsumo.getTag_mito());
	}

	@Override
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		removePlayerFromEvent(p);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onRankingUpdate(ChangeTopEvent e) {
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.sendMessage(" ");
			p.sendMessage(configsumo.getMito().split("<nl>")[0].replace("{player}", e.getTop()));
			p.sendMessage(configsumo.getMito().split("<nl>")[1].replace("{player}", e.getTop()));
			p.sendMessage(" ");
			p.sendTitle(configsumo.getMito().split("<nl>")[0].replace("{player}", e.getTop()), configsumo.getMito().split("<nl>")[1].replace("{player}", e.getTop()));
			p.playSound(p.getLocation(), Sound.NOTE_PLING, 0.5f, 10);
		}
	}
	
	@Override public void onCommands(PlayerCommandPreprocessEvent e) {
		e.setCancelled(true);
		Player p = e.getPlayer();
		String[] args = e.getMessage().replaceFirst("/", new String()).split(" ");
		if (args[0].equalsIgnoreCase("sair")) {
			removePlayerFromEvent(p);
			return;
		}
		for(String cmd : configsumo.getCommands()) {
			if (cmd.equalsIgnoreCase(args[0])) {
				e.setCancelled(false);
				return;
			}
		}
		p.sendMessage(MensagensSumo.get().getCommandBloqued());
		p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
	}

	@Override public void onDamageEntity(EntityDamageByEntityEvent e) {
		Player p = (Player) e.getDamager();
		e.setCancelled(true);
		if (player[0] == p || player[1] == p) {
			if (e.getEntity() instanceof Player) {
				Player target = (Player) e.getEntity();
				if ((player[0] == target || player[1] == target) && preparing == -1) {
					e.setCancelled(false);
				}
			}
		}
	}
	
	@Override
	public void damage(EntityDamageEvent e) {
		Player p = (Player) e.getEntity();
		if (player[0] == p || player[1] == p) {
			p.setHealth(p.getMaxHealth());
			return;
		}
		e.setCancelled(true);
	}

	@Override public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (player[0] == p || player[1] == p) return;
		e.setCancelled(true);	
	}

	@Override public void onBreakBlock(BlockBreakEvent e) {
		e.setCancelled(true);	
	}

	@Override public void onPlaceBlock(BlockPlaceEvent e) {
		e.setCancelled(true);
	}

	@Override public void onDeath(PlayerDeathEvent e) {}

	@Override public void onHunger(FoodLevelChangeEvent e) {
		e.setCancelled(true);
	}
	
	@Override public void onJoin(PlayerJoinEvent e) {}
	
	@Override public void onChatVanilla(AsyncPlayerChatEvent e) {}

	@Override public void onMove(PlayerMoveEvent e) {}

	@Override public void onClickInventory(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (!(player[0] == p || player[1] == p)) {
			e.setCancelled(true);
			p.updateInventory();
		}
	}

	public boolean isConfigured() {
		return getLocationExit() != null && getLocationLobby() != null && getLocationStart() != null && getLocationEspectator() != null;
	}
	
	@Override
	public void playerDropItem(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if (!(player[0] == p || player[1] == p)) {
			e.setCancelled(true);
			p.updateInventory();
		}
	}

	@Override
	public void tagUpdate(PlayerUpdateTagEvent e) {
		Player p = e.getJogador().getPlayer();
		Player target = e.getReflection();
		if (!containsPlayerOnEvent(p)) return;
		if (!containsPlayerOnEvent(target)) return;
		PlayerType type = getPlayers().get(p);
		e.setSuffix(new String());
		if (type == PlayerType.ESPECTATING) {
			e.setPrefix("§7[Espectador] ");
		}else {
			if (player[0] == p || player[1] == p) {
				e.setPrefix("§c");
			}else {
				e.setPrefix("§7[Sumo] ");
			}
		}
	}

	@Override
	public void updatePlayersEveryTime() {
		Tag.updateAllTag();
		List<Player> espectadores = getPlayers(PlayerType.ESPECTATING);
		for(Player p : espectadores) {
			loop: for(Player all : Bukkit.getOnlinePlayers()) {
				if (all == p) continue loop;
				all.hidePlayer(p);
			}
		}
	}
	
	@Override
	public void save() {
		List<Object> lista = new ArrayList<>();
		lista.add(getLocationExit() == null ? "N.A" : API.get().serialize(getLocationExit()));
		lista.add(getLocationLobby() == null ? "N.A" : API.get().serialize(getLocationLobby()));
		lista.add(getLocationStart() == null ? "N.A" : API.get().serialize(getLocationStart()));
		lista.add(getLocationEspectator() == null ? "N.A" : API.get().serialize(getLocationEspectator()));
		lista.add(getLocationPlayer1() == null ? "N.A" : API.get().serialize(getLocationPlayer1()));
		lista.add(getLocationPlayer2() == null ? "N.A" : API.get().serialize(getLocationPlayer2()));
		lista.add(API.get().serializeItems(armor_kit.clone()));
		lista.add(API.get().serializeItems(content_kit.clone()));
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
		setLocationEspectator(((String)lista.get(3)).equalsIgnoreCase("N.A")?null:API.get().unserialize(((String)lista.get(3))));
		setLocationPlayer1(((String)lista.get(4)).equalsIgnoreCase("N.A")?null:API.get().unserialize(((String)lista.get(4))));
		setLocationPlayer2(((String)lista.get(5)).equalsIgnoreCase("N.A")?null:API.get().unserialize(((String)lista.get(5))));
		armor_kit = API.get().unserializeItems(((String)lista.get(6)));
		content_kit = API.get().unserializeItems(((String)lista.get(7)));
		List<Object> objects = (List<Object>) lista.get(8);
		for(Object object : objects) {
			String[] args = ((String)object).split(":");
			String name = args[0];
			int value = Integer.valueOf(args[1]);
			getRanking().getTops().put(name, value);
		}
		getRanking().update();
	}

	@Override
	public void pickItemEvent(PlayerPickupItemEvent e) {
		Player p = e.getPlayer();
		if (!(player[0] == p || player[1] == p)) {
			e.setCancelled(true);
		}
	}

	@Override
	public void damageClanAlly(PlayerDamageClanAlly e) {
		e.setCancelled(true);
	}

	@Override
	public void damageClanMember(PlayerDamageClanMember e) {
		e.setCancelled(true);
	}

}
