package dev.talwat.earthsmp;

import dev.talwat.earthsmp.commands.Handler;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Earthsmp extends JavaPlugin {
    public BorderImage borders;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        borders = new BorderImage(this);

        getLogger().info("EarthSMP plugin loaded!");
        getServer().getPluginManager().registerEvents(new EventListener(this), this);
        Objects.requireNonNull(getCommand("earth")).setExecutor(new Handler(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
