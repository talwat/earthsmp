package dev.talwat.earthsmp;

import org.bukkit.plugin.java.JavaPlugin;

public final class Earthsmp extends JavaPlugin {
    public BorderImage borders;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        borders = new BorderImage(this);

        getLogger().info("EarthSMP plugin loaded!");
        getServer().getPluginManager().registerEvents(new EventListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
