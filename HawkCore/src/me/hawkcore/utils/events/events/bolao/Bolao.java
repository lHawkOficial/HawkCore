package me.hawkcore.utils.events.events.bolao;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import lombok.Getter;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.events.EventManager;
import me.hawkcore.utils.events.events.bolao.utils.ConfigBolao;
import me.hawkcore.utils.events.utils.Event;
import me.hawkcore.utils.events.utils.enums.EventType;
import me.hawkcore.utils.events.utils.interfaces.EventExecutor;
import me.hawkcore.utils.events.utils.interfaces.EventListeners;

@Getter
public class Bolao extends Event implements EventExecutor, EventListeners {

	private Task task;
	
	public Bolao(String name, FileConfiguration config, EventType type, boolean enabled) {
		super(name, config, type, enabled);
		setupConfig();
		setupListeners();
		EventManager.get().getEvents().add(this);
	}

	@Override
	public void setupConfig() {
		ConfigBolao config = new ConfigBolao(this);
		setConfigEvent(config);
	}
	
	@Override
	public void updateScore() {
	}

	@Override
	public boolean containsPlayerOnEvent(Player p) {
		return getPlayers().containsKey(p);
	}

	@Override
	public void addPlayerToEspectator(Player p) {
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

	@Override
	public void onChat(AsyncPlayerChatEvent e) {
	}

	@Override
	public void onMove(PlayerMoveEvent e) {
	}

	@Override
	public void onClickInventory(InventoryClickEvent e) {
	}

	@Override
	public void onQuit(PlayerQuitEvent e) {
	}

	@Override
	public void onCommands(PlayerCommandPreprocessEvent e) {
	}

	@Override
	public void onDamageEntity(EntityDamageByEntityEvent e) {
	}

	@Override
	public void onInteract(PlayerInteractEvent e) {
		
	}

	@Override
	public void onBreakBlock(BlockBreakEvent e) {
	}

	@Override
	public void onPlaceBlock(BlockPlaceEvent e) {
	}

	@Override
	public void onDeath(PlayerDeathEvent e) {
	}

	@Override
	public void onHunger(FoodLevelChangeEvent e) {
	}

}
