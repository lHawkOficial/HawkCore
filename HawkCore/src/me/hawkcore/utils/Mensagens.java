package me.hawkcore.utils;

import lombok.Getter;
import me.hawkcore.Core;

@Getter
public class Mensagens {

	private String commandDesenchant_noMoney,
	permission,
	commandDesenchant_itemErro,
	commandDesenchant_sucess;
	
	public Mensagens() {
		commandDesenchant_sucess = replace("commandDesenchant_sucess");
		commandDesenchant_itemErro = replace("commandDesenchant_itemErro");
		permission = replace("permission");
		commandDesenchant_noMoney = replace("commandDesenchant_noMoney");
	}
	
	private String replace(String msg) {
		return Core.getInstance().getConfig().getString("Mensagens." + msg).replace("&", "§").replace("{tag}", Core.getInstance().getTag());
	}
	
	public static Mensagens get() {
		return Core.getInstance().getMensagens();
	}
	
}
