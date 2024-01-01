package art.ryanstew.custompluginlist;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;


public class CPLCommand implements CommandExecutor
{
    private static CustomPluginList plugin;


    CPLCommand(CustomPluginList customPluginList)
    {
        plugin = customPluginList;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings)
    {
        // Help message (/custompluginlist, /cpl, /cpl help)
        if (strings.length == 0 || strings[0].equalsIgnoreCase("help"))
        {
            if (!commandSender.hasPermission(plugin.HELP_PERMISSION))
            {
                plugin.sendMessage(commandSender, plugin.PREFIX + plugin.NO_PERMISSION_MESSAGE);
                return true;
            }

            String[] lines = {
                    "&7✧･ﾟ: *✧･ﾟ:* &a&lCustomPluginList &7*:･ﾟ✧*:･ﾟ✧",
                    "&f- &a/custompluginlist &for &a/cpl help",
                    "&f- &a/cpl seemessage &for &a/cpl seemsg",
                    "&f- &a/cpl reload"
            };
            plugin.sendMessage(commandSender, String.join("\n", lines));
            return true;
        }

        // /cpl seemessage or /cpl seemsg
        if (strings[0].equalsIgnoreCase("seemessage") || strings[0].equalsIgnoreCase("seemsg"))
        {
            if (!commandSender.hasPermission(plugin.SEE_MESSAGE_PERMISSION))
            {
                plugin.sendMessage(commandSender, plugin.PREFIX + plugin.NO_PERMISSION_MESSAGE);
                return true;
            }

            String message = plugin.getCPLConfig().getString("message");
            if (message == null)
            {
                plugin.sendMessage(commandSender, plugin.PREFIX + plugin.INCORRECT_CONFIG_MESSAGE);
                return true;
            }

            plugin.sendMessage(commandSender, plugin.PREFIX + "&fHere is the custom plugin list:");
            plugin.sendMessage(commandSender, message);
            return true;
        }

        // /cpl reload
        if (strings[0].equalsIgnoreCase("reload"))
        {
            if (!commandSender.hasPermission(plugin.RELOAD_PERMISSION))
            {
                plugin.sendMessage(commandSender, plugin.PREFIX + plugin.NO_PERMISSION_MESSAGE);
                return true;
            }

            boolean success = plugin.loadConfig();
            plugin.sendMessage(commandSender, plugin.PREFIX + (success ? plugin.CONFIG_RELOAD_SUCCESS_MESSAGE : plugin.CONFIG_RELOAD_FAIL_MESSAGE));
            return true;
        }

        // Incorrect usage
        plugin.sendMessage(commandSender, plugin.PREFIX + plugin.INCORRECT_USE_MESSAGE);
        return true;
    }
}
