package me.hawkcore.utils.itemcreator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.Getter;
import me.hawkcore.Core;

@Getter
public class ManagerItemCreator {

	private List<ItemCreator> items = new ArrayList<>();
	
	public ItemCreator getItem(String nome) {
		Iterator<ItemCreator> it = items.iterator();
		while(it.hasNext()) {
			ItemCreator ic = it.next();
			if (ic.getNome().equalsIgnoreCase(nome)) return ic;
		}
		return null;
	}
	
	public static ManagerItemCreator get() {
		return Core.getInstance().getManageritemcreator();
	}
	
}
