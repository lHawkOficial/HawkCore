package me.hawkcore.utils.strings;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;

public class StringAPI {

	public static String removeLastChar(String txt) {
		return txt.substring(0, txt.length()-1);
	}
	
	public static String createBar(String txt, String colorDefault, String colorBackground, double value, double maxValue) {
		if (txt.length() == 0) return new String();
		String line = ChatColor.stripColor(txt.replace("&", "§"));
		int percentTotal = Double.valueOf(value * 100 / maxValue).intValue();
		int totalChar = line.length();
		int newPercent = totalChar*percentTotal/100;
		txt = colorBackground+new String();
		for (int i = 0; i < line.length(); i++) {
			if (i==newPercent) txt += colorDefault+line.toCharArray()[i];
			else txt += line.toCharArray()[i];
		}
		return txt;
	}
	
	public static String randomCode(int lenght) {
		if (lenght<=0) return new String();
		String linha = new String();
		List<Character> chars = new ArrayList<>();
		for(char i = 'a'; i < 'z'; i++) {
			chars.add(i);
		}
		while(linha.length() < lenght) {
			linha+=new Random().nextBoolean() ? String.valueOf(chars.get(new Random().nextInt(chars.size()-1 == 0 ? 1 : chars.size()))).toLowerCase() : String.valueOf(chars.get(new Random().nextInt(chars.size()-1 == 0 ? 1 : chars.size()))).toUpperCase();
		}
		return linha;
	}
	
}
