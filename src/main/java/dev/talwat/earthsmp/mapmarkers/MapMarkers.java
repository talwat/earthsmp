package dev.talwat.earthsmp.mapmarkers;

import dev.talwat.earthsmp.Earthsmp;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.Point;
import xyz.jpenilla.squaremap.api.SimpleLayerProvider;
import xyz.jpenilla.squaremap.api.marker.Marker;
import xyz.jpenilla.squaremap.api.marker.MarkerOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dev.talwat.earthsmp.Squaremap.SetupLayerProvider;
import static java.lang.String.format;

public class MapMarkers {
    private final Earthsmp plugin;
    public Map<String, List<MapMarker>> markers;

    public MapMarkers(Earthsmp plugin) throws Exception {
        this.plugin = plugin;
        this.Load();

        for (MapMarkerType type : MapMarkerType.values()) {
            File target = new File(format("plugins/squaremap/web/images/icon/registered/%s.png", type.toString()));

            if (!target.exists()) {
                OutputStream outStream;
                outStream = new FileOutputStream(target);
                outStream.write(plugin.getResource(format("icons/%s.png", type)).readAllBytes());
            }
        }
    }

    public static Map.Entry<String, List<Map<String, Object>>> getSection(Map<String, List<Map<String, Object>>> raw) {
        var first = raw.entrySet().stream().findFirst();
        if (first.isEmpty()) {
            return null;
        }

        var entry = first.get();

        if (entry.getValue() == null) {
            return null;
        }

        return entry;
    }

    public void Load() {
        File file = new File(plugin.getDataFolder(), "markers.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        this.markers = new HashMap<>();

        if (config.getList("markers") == null) {
            return;
        }

        var list = (List<Map<String, List<Map<String, Object>>>>) config.getList("markers");
        if (list == null) {
            return;
        }

        for (Map<String, List<Map<String, Object>>> nationMarkers : list) {
            var entry = getSection(nationMarkers);
            if (entry == null) {
                continue;
            }

            for (Map<String, Object> marker : entry.getValue()) {
                markers.putIfAbsent(entry.getKey(), new ArrayList<>());
                markers.get(entry.getKey()).add(MapMarker.deserialize(marker));
            }
        }

        SimpleLayerProvider layerProvider = SetupLayerProvider("Markers", "markers");
        if (layerProvider == null) {
            return;
        }

        int i = 0;
        for (Map.Entry<String, List<MapMarker>> entry : markers.entrySet()) {
            for (MapMarker mapMarker : entry.getValue()) {
                Point point = Point.of(mapMarker.pos().getBlockX(), mapMarker.pos().getBlockZ());
                Marker marker = Marker.icon(point, Key.key(mapMarker.type().toString()), 16);
                marker.markerOptions(MarkerOptions.builder().hoverTooltip(mapMarker.label()));

                layerProvider.addMarker(Key.key(format("marker_%d", i)), marker);

                i++;
            }
        }
    }
}
