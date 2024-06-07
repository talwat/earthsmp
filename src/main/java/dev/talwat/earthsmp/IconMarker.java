package dev.talwat.earthsmp;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class IconMarker {
    String label;
    String type;
    Location pos;

    public IconMarker(String label, String type, Location pos) {
        this.label = label;
        this.type = type;
        this.pos = pos.toBlockLocation();
    }

    public Map<String, Object> serialize() {
        Map<String, Object> serialized = new LinkedHashMap<>();
        serialized.put("label", label);
        serialized.put("type", type);

        Map<String, Object> serializedPos = new HashMap<>();
        serializedPos.put("x", (int) pos.x());
        serializedPos.put("z", (int) pos.z());
        serialized.put("pos", serializedPos);

        return serialized;
    }
}
