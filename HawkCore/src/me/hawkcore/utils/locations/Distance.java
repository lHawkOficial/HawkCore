package me.hawkcore.utils.locations;

import org.bukkit.Location;

import lombok.Getter;

@Getter
public class Distance {
	
	private Location from, to;
	private double x, y, z;
	
	public Distance(Location from, Location to) {
		this.from = from;
		this.to = to;
		if (this.from == null) return;
		if (this.to == null) return;
		if (!from.getWorld().equals(to.getWorld())) return;
		x = from.getX() > to.getX() ? (+from.getX()) - (+to.getX()) : (+to.getX()) - (+from.getX());
		y = from.getY() > to.getY() ? (+from.getY()) - (+to.getY()) : (+to.getY()) - (+from.getY());
		z = from.getZ() > to.getZ() ? (+from.getZ()) - (+to.getZ()) : (+to.getZ()) - (+from.getZ());
	}
	
	public Boolean hight(double value) {
		return x > value || y > value || z > value;
	}
	
	public double value() {
		return y > x && y > z ? y : x > y && x > z ? x : z > y && z > x ? z : x;
	}

}
