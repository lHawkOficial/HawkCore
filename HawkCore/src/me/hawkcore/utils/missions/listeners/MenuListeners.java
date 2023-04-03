package me.hawkcore.utils.missions.listeners;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.hawkcore.Core;
import me.hawkcore.utils.PaginaCreator;
import me.hawkcore.utils.missions.listeners.plugin.MissionCollectItemEvent;
import me.hawkcore.utils.missions.menus.menuCategorys;
import me.hawkcore.utils.missions.menus.menuMissions;
import me.hawkcore.utils.missions.objects.Mission;
import me.hawkcore.utils.missions.objects.MissionCategory;
import me.hawkcore.utils.missions.objects.MissionPlayer;

public class MenuListeners implements Listener {

	public MenuListeners() {
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
	}
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if(p.hasMetadata("pMissions")) {
			PaginaCreator pagina = (PaginaCreator) p.getMetadata("pMissions").get(0).value();
			String nome = menuCategorys.get().getName().replace("{pagina}", String.valueOf(pagina.getPaginaAtual())).replace("{total}", String.valueOf(pagina.getTotalPaginas()));
			if (e.getView().getTitle().equals(nome)) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;
				p.playSound(p.getLocation(), Sound.CLICK, 0.5f, 10);
				MissionPlayer mp = MissionPlayer.check(p);
				
				if (e.getCurrentItem().isSimilar(menuCategorys.get().getIconBack().build())) {
					if (pagina.containsPagina(pagina.getPaginaAtual()-1)) {
						pagina.setPaginaAtual(pagina.getPaginaAtual()-1);
						menuCategorys.get().open(p);
					}
					return;
				}
				
				if (e.getCurrentItem().isSimilar(menuCategorys.get().getIconNext().build())) {
					if (pagina.containsPagina(pagina.getPaginaAtual()+1)) {
						pagina.setPaginaAtual(pagina.getPaginaAtual()+1);
						menuCategorys.get().open(p);
					}
					return;
				}
				
				MissionCategory category = MissionCategory.get(e.getCurrentItem(), p);
				if (category != null) {
					if (p.hasMetadata("pMissions")) p.removeMetadata("pMissions", Core.getInstance());
					if (mp.getCategoryToComplete() == null || !mp.getCategoryToComplete().equals(category)) {
						mp.setCategoryToComplete(category);
						mp.save();
						menuCategorys.get().open(p);
						p.playSound(p.getLocation(), Sound.LEVEL_UP, 0.5f, 10);
						return;
					}
					mp.setCategorySelected(category);
					menuMissions.get().open(p);
				}
			}
			
			MissionPlayer mp = MissionPlayer.check(p);
			MissionCategory category = mp.getCategorySelected();
			if (category==null) return;
			nome = menuMissions.get().getName().replace("{nome}", category.getName()).replace("{pagina}", String.valueOf(pagina.getPaginaAtual())).replace("{total}", String.valueOf(pagina.getTotalPaginas()));
			if (e.getView().getTitle().equals(nome)) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;
				p.playSound(p.getLocation(), Sound.CLICK, 0.5f, 10);
				if (e.getCurrentItem().isSimilar(menuMissions.get().getIconCategorys().build())) {
					if(p.hasMetadata("pMissions")) p.removeMetadata("pMissions", Core.getInstance());
					menuCategorys.get().open(p);
					return;
				}
				if (e.getCurrentItem().isSimilar(menuMissions.get().getIconBack().build())) {
					if (pagina.containsPagina(pagina.getPaginaAtual()-1)) {
						pagina.setPaginaAtual(pagina.getPaginaAtual()-1);
						menuMissions.get().open(p);
					}
					return;
				}
				if (e.getCurrentItem().isSimilar(menuMissions.get().getIconNext().build())) {
					if (pagina.containsPagina(pagina.getPaginaAtual()+1)) {
						pagina.setPaginaAtual(pagina.getPaginaAtual()+1);
						menuMissions.get().open(p);
					}
					return;
				}
				
				Mission m = mp.getCategorySelected().getMissionToComplete().getMission();
				Mission mission = mp.getCategorySelected().getMission(mp, e.getCurrentItem());
				if (mission==null) return;
				if (m==null)return;
				MissionCollectItemEvent event = new MissionCollectItemEvent(mp, mission);
				Bukkit.getPluginManager().callEvent(event);
			}
		}
		
	}
	
}
