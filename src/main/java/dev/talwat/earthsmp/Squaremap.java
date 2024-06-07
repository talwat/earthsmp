package dev.talwat.earthsmp;

import org.bukkit.Bukkit;
import org.bukkit.World;
import xyz.jpenilla.squaremap.api.*;

public class Squaremap {
    public static SimpleLayerProvider SetupLayerProvider(String label, String id) {
        xyz.jpenilla.squaremap.api.Squaremap api = SquaremapProvider.get();

        World world = Bukkit.getWorld("world");
        if (world == null) {
            return null;
        }
        WorldIdentifier identifier = BukkitAdapter.worldIdentifier(world);
        MapWorld mapWorld = api.getWorldIfEnabled(identifier).orElse(null);
        if (mapWorld == null) {
            return null;
        }

        Key key = Key.key(id);

        if (mapWorld.layerRegistry().hasEntry(key)) {
            mapWorld.layerRegistry().unregister(key);
        }

        SimpleLayerProvider layerProvider = SimpleLayerProvider.builder(label)
                .showControls(true)
                .defaultHidden(false)
                .layerPriority(5)
                .zIndex(250)
                .build();

        mapWorld.layerRegistry().register(key, layerProvider);

        layerProvider.clearMarkers();

        return layerProvider;
    }
}
