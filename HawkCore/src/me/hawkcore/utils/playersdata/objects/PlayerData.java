package me.hawkcore.utils.playersdata.objects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import lombok.Getter;
import me.hawkcore.Core;
import me.hawkcore.utils.playersdata.managers.ManagerData;

@Getter
public class PlayerData {

	private String name;
	private Thirst thirst;
	private Heat heat;
	
	public PlayerData(String name) {
		this.name = name;
		this.thirst = new Thirst(this, 0, 0);
		this.heat = new Heat(this, 0, 0);
		ManagerData.get().getPlayers().add(this);
	}
	
	public static PlayerData check(Player p) {
		if (p.hasMetadata("playerdata")) return (PlayerData) p.getMetadata("playerdata").get(0).value();
		PlayerData pd = ManagerData.get().getPlayer(p.getName());
		if (pd == null) pd = new PlayerData(p.getName());
		p.setMetadata("playerdata", new FixedMetadataValue(Core.getInstance(), pd));
		return pd;
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayerExact(name);
	}
	
}
