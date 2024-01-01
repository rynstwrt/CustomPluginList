package art.ryanstew.custompluginlist;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class CustomPluginList extends JavaPlugin
{

    public final String PREFIX = "&f&l[&a&lCPL&f&l]&r ";

    public final String CONFIG_RELOAD_SUCCESS_MESSAGE = "&aConfig successfully reloaded!";
    public final String CONFIG_RELOAD_FAIL_MESSAGE = "&cFailed to reload the configuration!";
    public final String INCORRECT_USE_MESSAGE = "&cIncorrect usage! Run /cpl help for instructions.";
    public final String INCORRECT_CONFIG_MESSAGE = "&cMessage not found in config.yml!";
    public final String NO_PERMISSION_MESSAGE = "&cYou do not have permission to use that command!";

    public final String HELP_PERMISSION = "cpl.help";
    public final String SEE_MESSAGE_PERMISSION = "cpl.seemessage";
    public final String RELOAD_PERMISSION = "cpl.reload";
    public final String BYPASS_PERMISSION = "cpl.bypass";

    private File configFile;
    private FileConfiguration config;


    @Override
    public void onEnable()
    {
        loadConfig();
        Objects.requireNonNull(this.getCommand("custompluginlist")).setExecutor(new CPLCommand(this));
    }


    @Override
    public void onDisable()
    {

    }


    public FileConfiguration getCPLConfig()
    {
        return config;
    }


    public void sendMessage(CommandSender sender, String message)
    {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }


    public boolean loadConfig()
    {
        configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists())
        {
            configFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(configFile);
        return config.getString("message") != null;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
    {
        List<String> list = new ArrayList<>();

        if(sender.hasPermission("cpl.reload"))
        {
            list.add("reload");
        }

        return list;
    }
}
