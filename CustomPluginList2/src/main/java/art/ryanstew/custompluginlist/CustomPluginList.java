package art.ryanstew.custompluginlist;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class CustomPluginList extends JavaPlugin
{

    @Override
    public void onEnable()
    {
        Objects.requireNonNull(this.getCommand("custompluginlist")).setExecutor(new CPLCommand(this));
    }

    @Override
    public void onDisable()
    {

    }
}
