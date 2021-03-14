package art.ryanstew.custompluginslist;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class Main extends JavaPlugin implements Listener {

    String pluginPrefix = ChatColor.translateAlternateColorCodes('&', "&c&l[&b&lCustomPluginsList&c&l]&7&l: &r");

    @Override
    public void onEnable()
    {
        List<String> sl = new ArrayList<String>();
        sl.add("Why");
        sl.add("Are");
        sl.add("You");
        sl.add("So");
        sl.add("Concerned");
        sl.add("With");
        sl.add("The");
        sl.add("Plugins");
        sl.add("On");
        sl.add("This");
        sl.add("Server?");
        sl.add(":)");

        Map<String, Object> defaults = new HashMap<>();
        defaults.put("plugins", sl);

        getConfig().addDefaults(defaults);
        getConfig().options().copyDefaults(true);
        saveConfig();

        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent e)
    {
        if (!e.getMessage().startsWith("/plugins")) return;
        Player player = e.getPlayer();
        String[] cmdParts = e.getMessage().trim().split(" ");

        // called when /plugins
        // return the fake plugin list
        if (cmdParts.length == 1 || !player.hasPermission("custompluginlist.admin"))
        {
            e.setCancelled(true);

            List<String> pluginList = getConfig().getStringList("plugins");
            String message = String.format("&fPlugins (%d): ", pluginList.size());

            for (int i = 0; i < pluginList.size(); ++i)
            {
                message += "&a" + pluginList.get(i);
                if (i != pluginList.size() - 1)
                    message += "&f, ";
            }

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return;
        }

        // /plugins adminview or /plugins av
        String firstArgument = cmdParts[1];
        if (firstArgument.equalsIgnoreCase("adminview") || firstArgument.equalsIgnoreCase("av")) return;

        if (firstArgument.equalsIgnoreCase("reload"))
        {
            e.setCancelled(true);
            reloadConfig();
            player.sendMessage("");
            player.sendMessage(pluginPrefix + ChatColor.GREEN + "The config has been successfully reloaded.");
            player.sendMessage("");
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, 1);
            return;
        }

        e.setCancelled(true);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cIncorrect usage! Usage: &a&l/plugins <adminview/av/reload>"));
    }
}
