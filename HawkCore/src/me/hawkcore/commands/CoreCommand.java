package me.hawkcore.commands;


import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.hawkcore.Core;
import me.hawkcore.tasks.TaskManager;

public class CoreCommand implements CommandExecutor {

	public CoreCommand() {
		Core.getInstance().getCommand("hawkcore").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String lb, String[] args) {
		if (!s.hasPermission("*")) return false;
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("reload")) {
				Plugin plugin = Bukkit.getPluginManager().getPlugin(args[1]);
				try {
					PluginManager manager = Bukkit.getPluginManager();
					manager.disablePlugin(plugin);
					manager.loadPlugin(getPluginFile(plugin));
					Bukkit.getConsoleSender().sendMessage("§aPlugin §f" + plugin.getName() + " §arecarregado com sucesso!");
					return false;
				} catch (Exception e) {
					s.sendMessage("§cOcorreu um erro ao tentar recarregar este plugin!");
					return false;
				}
			}
		}
		s.sendMessage(" ");
		s.sendMessage(Core.getInstance().getTag() + " §7Atualização do Plugin " + Core.getInstance().getVersion());
		s.sendMessage("§7Tasks em execução: §e" + TaskManager.get().getTasks().size());
		s.sendMessage("§7Latência per/Tick: §e" + TaskManager.get().getLastTick() + "ms");
		s.sendMessage("§7Tasks ignoradas: §e" + TaskManager.get().getErrors());
		s.sendMessage(" ");
		return false;
	}
	
	private static File getPluginFile(Plugin plugin) {
        Method method;
        try {
            method = JavaPlugin.class.getDeclaredMethod("getFile");
            method.setAccessible(true);
            return (File) method.invoke(plugin);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
	
}
