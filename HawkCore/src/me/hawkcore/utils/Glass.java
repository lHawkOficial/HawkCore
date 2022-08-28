package me.hawkcore.utils;

import java.util.ArrayList;


import java.util.List;
import java.util.Random;

import me.hawkcore.utils.items.Item;


public class Glass {
	
	private List<Item> vidros = new ArrayList<>();

	public Glass(List<String> lista, int tamanhoMenu) {
		for (String linha : lista) {
			String[] args = linha.split(":");
			String ID = args[0];
			
			if (args[1].equalsIgnoreCase("all")) {
				for (int i = 0; i < 9*tamanhoMenu; i++) {
					Item item = new Item(API.get().getItemStack(ID));
					item.setDisplayName("§"+new Random().nextInt(9)+"●");
					item.setSlot(i);
					vidros.add(item);
				}
			}else {
				if (args[1].contains(",")) {
					for(String slot : args[1].split(",")) {
						Item item = new Item(API.get().getItemStack(ID));
						item.setDisplayName("§"+new Random().nextInt(9)+"●");
						item.setSlot(Integer.valueOf(slot)-1);
						vidros.add(item);
					}
				}else {
					Item item = new Item(API.get().getItemStack(ID));
					item.setDisplayName("§"+new Random().nextInt(9)+"●");
					item.setSlot(Integer.valueOf(args[1])-1);
					vidros.add(item);
				}
			}
			
		}
	}

	public List<Item> getVidros() {
		return this.vidros;
	}
}