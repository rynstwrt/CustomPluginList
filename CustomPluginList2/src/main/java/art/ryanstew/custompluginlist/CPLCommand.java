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

    private static final String PREFIX = "&f&l[&a&lCPL&f&l]&r ";
    private static final String CONFIG_RELOAD_SUCCESS_MESSAGE = "&aConfig successfully reloaded!";
    private static final String CONFIG_RELOAD_FAIL_MESSAGE = "&cFailed to reload the configuration!";
    private static final String INCORRECT_USE_MESSAGE = "&cIncorrect usage! Run /cpl help for instructions.";
    private static final String INCORRECT_CONFIG_MESSAGE = "&cMessage not found in config.yml!";

    private static String resultMessage;


    CPLCommand(CustomPluginList customPluginList)
    {
        plugin = customPluginList;
        createOrLoadConfig();
    }


    private static void createOrLoadConfig()
    {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists())
        {
            configFile.getParentFile().mkdirs();
            plugin.saveResource("config.yml", false);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        resultMessage = config.getString("message");
    }


    private static void sendMessage(CommandSender sender, String message)
    {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }


    private static boolean reloadConfig()
    {
        createOrLoadConfig();
        return true;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings)
    {
        // Help message (/custompluginlist, /cpl, /cpl help)
        if (strings.length == 0 || strings[0].equalsIgnoreCase("help"))
        {
            String[] lines = {
                    "&a✧･ﾟ: *✧･ﾟ:* &lCustomPluginList &a*:･ﾟ✧*:･ﾟ✧",
                    "&f- &a/custompluginlist &for &a/cpl help",
                    "&f- &a/cpl seemessage &for &a/cpl seemsg",
                    "&f- &a/cpl reload"
            };
            sendMessage(commandSender, String.join("\n", lines));
            return true;
        }

        // /cpl seemessage or /cpl seemsg
        if (strings[0].equalsIgnoreCase("seemessage") || strings[0].equalsIgnoreCase("seemsg"))
        {
            if (resultMessage == null)
            {
                sendMessage(commandSender, PREFIX + INCORRECT_CONFIG_MESSAGE);
                return true;
            }

            sendMessage(commandSender, resultMessage);
            return true;
        }

        // /cpl reload
        if (strings[0].equalsIgnoreCase("reload"))
        {
            sendMessage(commandSender, PREFIX + (reloadConfig() ? CONFIG_RELOAD_SUCCESS_MESSAGE : CONFIG_RELOAD_FAIL_MESSAGE));
            return true;
        }

        // Incorrect usage
        sendMessage(commandSender, PREFIX + INCORRECT_USE_MESSAGE);
        return true;
    }
}
