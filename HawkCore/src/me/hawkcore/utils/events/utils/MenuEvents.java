package me.hawkcore.utils.events.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import lombok.Getter;
import lombok.Setter;
import me.hawkcore.Core;
import me.hawkcore.utils.PaginaCreator;
import me.hawkcore.utils.events.EventManager;
import me.hawkcore.utils.events.events.bolao.Bolao;
import me.hawkcore.utils.events.events.fight.Fight;
import me.hawkcore.utils.events.events.parkour.Parkour;
import me.hawkcore.utils.items.Item;
import me.hawkcore.utils.menus.Menu;
import me.hawkcore.utils.menus.MenuAPI;
import me.hawkcore.utils.menus.interfaces.MenuExecutor;
import me.hawkcore.utils.menus.interfaces.MenuInterface;
import me.hawkcore.utils.menus.listeners.MenuClickEvent;
import me.hawkcore.utils.menus.listeners.MenuCloseEvent;

@Getter
@Setter
public class MenuEvents extends Menu implements MenuExecutor, MenuInterface {

	private HashMap<Player, PaginaCreator> paginas = new HashMap<>();
	private List<Integer> slots;
	private Item iconNext,
	iconBack;
	
	public MenuEvents(String title, int row, List<String> glass) {
		super(title, row, glass);
		this.slots = new ArrayList<>();
		for(int slot : Core.getInstance().getConfig().getIntegerList("Config.MenuEvents.slots")) {
			this.slots.add(slot-1);
		}
		iconNext = MenuAPI.getItemFromSection(Core.getInstance().getConfig().getConfigurationSection("Config.MenuEvents.Icons.iconNext"));
		iconBack = MenuAPI.getItemFromSection(Core.getInstance().getConfig().getConfigurationSection("Config.MenuEvents.Icons.iconBack"));
		register();
	}

	@Override
	public void open(Player p) {
		try {
			
			PaginaCreator pagina = paginas.containsKey(p) ? paginas.get(p) : new PaginaCreator(new ArrayList<>(EventManager.get().getEvents()), slots.size());
			
			Inventory inv = Bukkit.createInventory(null, 9*getRow(), getTitle());
			for(Item item : getGlass().getVidros()) {
				inv.setItem(item.getSlot(), item.build().clone());
			}
			
			for (int i = 0; i < slots.size(); i++) {
				try {
					int slot = slots.get(i);
					Event event = (Event) pagina.getPagina(pagina.getPaginaAtual()).get(i);
					inv.setItem(slot, event.getIcon().build().clone());
				} catch (Exception e) {
					continue;
				}
			}
			
			if (pagina.containsPagina(pagina.getPaginaAtual()+1)) inv.setItem(iconNext.getSlot(), iconNext.build().clone());
			if (pagina.containsPagina(pagina.getPaginaAtual()-1)) inv.setItem(iconBack.getSlot(), iconBack.build().clone());
			
			p.openInventory(inv);
			p.updateInventory();	
			p.playSound(p.getLocation(), Sound.NOTE_STICKS, 0.5f, 10);
			
		} catch (Exception e) {
			e.printStackTrace();
			p.closeInventory();
			p.sendMessage(Core.getInstance().getTag() + " §cNão foi possível abrir este menu, contate um administrador!");
		}
	}
	
	@Override
	public void click(MenuClickEvent e) {
		Player p = e.getPlayer();
		e.setClickSound(true);
		e.setCancelled(true);
		if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
			Event event = EventManager.get().getEvent(e.getCurrentItem());
			if (event != null) {
				if (event instanceof Bolao) ((Bolao)event).getMenu().open(p);
				if (event instanceof Parkour) ((Parkour)event).getMenu().open(p);
				if (event instanceof Fight) ((Fight)event).getMenu().open(p);
				return;
			}
		}
	}

	@Override
	public void close(MenuCloseEvent e) {
		
	}
	
	public static MenuEvents get() {
		return Core.getInstance().getMenuevents();
	}

}
