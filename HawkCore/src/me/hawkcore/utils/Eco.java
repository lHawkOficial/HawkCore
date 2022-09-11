package me.hawkcore.utils;

import me.hawkcore.Core;
import net.milkbowl.vault.economy.Economy;

public class Eco {

	public static Economy get() {
		return Core.getInstance().getEcon();
	}
	
}
