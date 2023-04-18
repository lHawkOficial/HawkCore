package me.hawkcore.utils.playersdata.groups;

import org.bukkit.entity.Player;

import me.hawkcore.verifies.PluginVerifier;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;

public class GroupAPI {

	private static LuckPerms luckPerms = LuckPermsProvider.get();

    public static String getGroup(Player player) {
    	if (!new PluginVerifier("LuckyPerms", new String()).queue()) return null;
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user == null) return null;
        return user.getPrimaryGroup();
    }

    public static void addGroup(Player player, String group) {
    	if (!new PluginVerifier("LuckyPerms", new String()).queue()) return;
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user == null) return;
        Node node = Node.builder("group." + group)
                .build();
        user.data().add(node);
        luckPerms.getUserManager().saveUser(user);
    }

    public static void removeGroup(Player player, String group) {
    	if (!new PluginVerifier("LuckyPerms", new String()).queue()) return;
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user == null) return;

        Node node = Node.builder("group." + group)
                .build();
        user.data().remove(node);
        luckPerms.getUserManager().saveUser(user);
    }
    
}
