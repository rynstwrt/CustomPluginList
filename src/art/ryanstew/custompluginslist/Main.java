package art.ryanstew.custompluginslist;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Main extends JavaPlugin implements Listener
{
    // variables
    private final List<String> aliasesToOverride = Arrays.asList("pl", "plugins", "?");
    protected String pluginText = "Why are you trying to see the plugins?"; // TODO: do with config

    protected final String PERM_HELP = "cpl.help";
    protected final String PERM_RELOAD = "cpl.reload";
    protected final String PERM_SEEMESSAGE = "cpl.seemessage";
    private final String PERM_BYPASS = "cpl.bypass";

    private File configFile;
    private FileConfiguration config;


    // onEnable
    @Override
    public void onEnable()
    {
        // set up config
        configFile = new File(getDataFolder(), "custompluginlist.yml");

        // if doesn't exist, create it.
        if (!configFile.exists())
        {
            configFile.getParentFile().mkdirs();
            saveResource("custompluginlist.yml", false);
        }

        // attempt to load
        config = new YamlConfiguration();
        try
        {
            config.load(configFile);
        }
        catch (InvalidConfigurationException | IOException e)
        {
            String msg = String.join("\n", Arrays.asList("&c&l&nCPL: That YAML configuration is invalid! Check it with a yaml parser online!", "&0"));
            getServer().getConsoleSender().sendMessage(parse(msg));
            e.printStackTrace();
        }

        // load the plugin message text from the config
        loadMessageFromConfig(getServer().getConsoleSender());

        // set command executor for /cpl
        Objects.requireNonNull(getCommand("custompluginlist")).setExecutor(new CPLCommand(this));

        // register events for this class
        getServer().getPluginManager().registerEvents(this, this);
    }


    // player command pre-process event
    @EventHandler
    public void onPrePlayerCommand(PlayerCommandPreprocessEvent event)
    {
        String[] split = event.getMessage().split(" ");
        String cmd = split[0].replaceFirst("/", "");
        Player player = event.getPlayer();
        if (!aliasesToOverride.contains(cmd) || player.hasPermission(PERM_BYPASS)) return;

        event.setCancelled(true);
        player.sendMessage(parse(getPluginText()));
    }


    protected void reloadCPLConfig(CommandSender sender)
    {
        // if file doesn't exist, send message to sender.
        if (!configFile.exists())
        {
            sender.sendMessage(parse("&cThe config file was not found! Restart the server to generate a new one."));
            return;
        }

        // load the config and attempt to save it to file.
        config = YamlConfiguration.loadConfiguration(configFile);
        try
        {
            config.save(configFile);
        }
        catch (IOException e)
        {
            sender.sendMessage(parse("&cThe config could not be saved! Do you have write permission?"));
            e.printStackTrace();
            return;
        }

        loadMessageFromConfig(sender);
    }


    // helper function to load the message stored in the config
    private void loadMessageFromConfig(CommandSender sender)
    {
        // send message if couldn't find the message section
        String message = config.getString("message");
        if (message == null)
        {
            sender.sendMessage(parse("&cThe message entry was not found in the config file!"));
            return;
        }

        pluginText = message;
        sender.sendMessage(parse("&aSuccessfully reloaded the config!"));
    }


    // parse chat colors helper function
    protected String parse(String msg)
    {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }


    // convert the stored pluginText to be in "Plugins (X): Y, Z, Q" format.
    protected String getPluginText()
    {
        List<String> plugins = Arrays.asList(pluginText.split(" "));
        return "&fPlugins (" + plugins.size() + "): &a" + String.join("&f, &a", plugins);
    }
}
