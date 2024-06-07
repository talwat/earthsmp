package dev.talwat.earthsmp;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.Point;
import xyz.jpenilla.squaremap.api.SimpleLayerProvider;
import xyz.jpenilla.squaremap.api.marker.Marker;
import xyz.jpenilla.squaremap.api.marker.MarkerOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dev.talwat.earthsmp.Squaremap.SetupLayerProvider;
import static java.lang.String.format;

public class Markers {
    Map<String, List<IconMarker>> markers;
    Earthsmp plugin;

    public Markers(Earthsmp plugin) {
        this.plugin = plugin;
        this.Load();
    }

    public void Load() {
        File file = new File(plugin.getDataFolder(), "markers.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        this.markers = new HashMap<>();

        for (Map<String, List<Map<String, Object>>> nationMarkers : (List<Map<String, List<Map<String, Object>>>>) config.getList("markers")) {
            Map.Entry<String, List<Map<String, Object>>> entry = nationMarkers.entrySet().stream().findFirst().get();

            if (entry.getValue() == null) {
                continue;
            }

            for (Map<String, Object> marker : entry.getValue()) {
                markers.putIfAbsent(entry.getKey(), new ArrayList<>());
                markers.get(entry.getKey()).add(new IconMarker(
                        marker.get("label").toString(),
                        marker.get("type").toString(),
                        Location.deserialize((Map<String, Object>) marker.get("pos"))
                ));
            }
        }

        SimpleLayerProvider layerProvider = SetupLayerProvider("Markers", "markers");
        if (layerProvider == null) {
            return;
        }

        int i = 0;
        for (Map.Entry<String, List<IconMarker>> entry : markers.entrySet()) {
            for (IconMarker iconMarker : entry.getValue()) {
                plugin.getLogger().info(format("%s", iconMarker.label));

                Point point = Point.of(iconMarker.pos.getBlockX(), iconMarker.pos.getBlockZ());
                Marker marker = Marker.icon(point, Key.key("squaremap-spawn_icon"), 16);
                marker.markerOptions(MarkerOptions.builder().hoverTooltip(iconMarker.label));

                layerProvider.addMarker(Key.key(format("marker_%d", i)), marker);

                i++;
            }
        }
    }
}
