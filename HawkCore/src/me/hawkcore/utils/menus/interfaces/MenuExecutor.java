package me.hawkcore.utils.menus.interfaces;


import me.hawkcore.utils.menus.listeners.MenuClickEvent;
import me.hawkcore.utils.menus.listeners.MenuCloseEvent;

public interface MenuExecutor {

	public void click(MenuClickEvent e);
	public void close(MenuCloseEvent e);
	
}
