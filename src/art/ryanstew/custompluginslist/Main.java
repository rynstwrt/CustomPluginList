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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends JavaPlugin implements Listener {

    String pluginPrefix = ChatColor.RED + "[" + ChatColor.AQUA + "CustomPluginsList" + ChatColor.RED + "] " + ChatColor.RESET;

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
        if (e.getMessage().startsWith("/plugins"))
        {
            Player player = e.getPlayer();

            if (e.getMessage().indexOf("reload") != -1 && player.hasPermission("custompluginlist.reload"))
            {
                e.setCancelled(true);

                reloadConfig();
                player.sendMessage("\n");
                player.sendMessage(pluginPrefix + ChatColor.GREEN + "The plugin has been successfully reloaded.");
                player.sendMessage("\n");

                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, 1);
            }
            else if (player.hasPermission("custompluginlist.listplugins"))
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
            }
        }
    }
}
