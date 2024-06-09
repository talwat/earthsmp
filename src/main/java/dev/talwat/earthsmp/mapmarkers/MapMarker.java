package dev.talwat.earthsmp.mapmarkers;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapMarker {
    public String label;
    public MapMarkerType type;
    public Location pos;

    public MapMarker(String label, MapMarkerType type, Location pos) {
        this.label = label;
        this.type = type;
        this.pos = pos.toBlockLocation();
    }

    public static MapMarker deserialize(Map<String, Object> raw) {
        MapMarkerType type;
        try {
            type = MapMarkerType.valueOf((String) raw.get("type"));
        } catch (IllegalArgumentException e) {
            type = MapMarkerType.other;
        }

        return new MapMarker(
                raw.get("label").toString(),
                type,
                Location.deserialize((Map<String, Object>) raw.get("pos"))
        );
    }

    public Map<String, Object> serialize() {
        Map<String, Object> serialized = new LinkedHashMap<>();
        serialized.put("label", label);
        serialized.put("type", type.toString());

        Map<String, Object> serializedPos = new HashMap<>();
        serializedPos.put("x", (int) pos.x());
        serializedPos.put("z", (int) pos.z());
        serialized.put("pos", serializedPos);

        return serialized;
    }
}
