package dev.talwat.earthsmp;

import dev.talwat.earthsmp.commands.Handler;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Earthsmp extends JavaPlugin {
    public Borders borders;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        saveResource("nations.yml", false);
        borders = new Borders(this);

        getLogger().info("EarthSMP plugin loaded!");
        getServer().getPluginManager().registerEvents(new EventListener(this), this);
        Objects.requireNonNull(getCommand("earth")).setExecutor(new Handler(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
