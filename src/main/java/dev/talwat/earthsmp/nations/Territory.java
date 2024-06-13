package dev.talwat.earthsmp.nations;

import java.util.Map;

public record Territory(String tag, String name, int saturation, boolean colony) {

    public static Territory deserialize(Map<String, Object> args) {
        return new Territory(
                (String) args.get("tag"),
                (String) args.get("name"),
                (int) args.get("color"),
                (boolean) args.get("colony")
        );
    }
}
