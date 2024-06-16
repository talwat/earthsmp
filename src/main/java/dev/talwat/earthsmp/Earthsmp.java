package dev.talwat.earthsmp;

import dev.talwat.earthsmp.commands.Handler;
import dev.talwat.earthsmp.mapmarkers.MapMarkers;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

public final class Earthsmp extends JavaPlugin {
    public Borders borders;
    public MapMarkers markers;
    public Map<UUID, InviteRequest> inviteRequests;
    public News news;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        saveResource("nations.yml", false);
        saveResource("markers.yml", false);
        saveResource("titles.yml", false);
        saveResource("news/news.yml", false);

        try {
            borders = new Borders(this);
            inviteRequests = new HashMap<>();
            markers = new MapMarkers(this);
            news = new News(this);
            Recipes.register(this);
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "An error occurred while loading!", e);
        }

        getLogger().info("EarthSMP plugin loaded!");
        getServer().getPluginManager().registerEvents(new EventListener(this), this);
        Objects.requireNonNull(getCommand("earth")).setExecutor(new Handler(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
