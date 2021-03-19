package art.ryanstew.custompluginslist;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class CPLCommand implements CommandExecutor
{
    // Class variables
    private final Main plugin;
    private final String LACK_PERM_MSG = "&cYou do not have permission to use this command!";

    // Constructor
    public CPLCommand(Main plugin) { this.plugin = plugin; }

    // onCommand
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        // CPL help command
        if (args.length == 0 || args[0].equalsIgnoreCase("help"))
        {
            if (!sender.hasPermission(plugin.PERM_HELP))
            {
                sender.sendMessage(plugin.parse(LACK_PERM_MSG));
                return true;
            }

            List<String> helpMessageLines = Arrays.asList(
                    "&0",
                    "&b✪ &a&lCUSTOM PLUGIN LIST &b✪",
                    "&0",
                    "&b&l• &a&l/cpl&f or &a&l/cpl help&f - Show this message!",
                    "&b&l• &a&l/cpl seemessage&f or &a&l/cpl seemsg&f - See the message that players without the bypass permission see when running /plugins.",
                    "&b&l• &a&l/cpl reload&f - Reload the config.",
                    "&0"
            );

            String helpMessage = String.join("\n", helpMessageLines);
            sender.sendMessage(plugin.parse(helpMessage));
            return true;
        }

        // CPL seemessage command
        if (args[0].equalsIgnoreCase("seemessage") || args[0].equalsIgnoreCase("seemsg"))
        {
            if (!sender.hasPermission(plugin.PERM_SEEMESSAGE))
            {
                sender.sendMessage(plugin.parse(LACK_PERM_MSG));
                return true;
            }

            sender.sendMessage(plugin.parse(plugin.getPluginText()));
            return true;
        }

        // CPL reload command
        if (args[0].equalsIgnoreCase("reload"))
        {
            if (!sender.hasPermission(plugin.PERM_RELOAD))
            {
                sender.sendMessage(plugin.parse(LACK_PERM_MSG));
                return true;
            }

            plugin.reloadCPLConfig(sender);
            return true;
        }

        sender.sendMessage(plugin.parse("&cIncorrect arguments! Run /cpl for help."));
        return true;
    }
}
