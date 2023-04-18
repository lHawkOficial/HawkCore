package me.hawkcore.utils.playersdata.utils;

import lombok.Getter;
import me.hawkcore.Core;

@Getter
public class MensagensThirstHeat {

	private String waterFull;
	
	public MensagensThirstHeat() {
		waterFull = replace("waterFull");
	}
	
	public static MensagensThirstHeat get() {return Core.getInstance().getMensagensthirstheat();}
	
	private String replace(String msg) {return Core.getInstance().getConfig().getString("Config.MensagensThirstHeat." + msg).replace("&", "§");}
	
}
