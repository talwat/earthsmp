package dev.talwat.earthsmp.config;

import java.util.Map;

public class Territory {
    public final String tag;
    public final String name;
    public final int saturation;
    public final boolean colony;

    public Territory(String tag, String name, int saturation, boolean colony) {
        this.tag = tag;
        this.name = name;
        this.saturation = saturation;
        this.colony = colony;
    }

    public static Territory deserialize(Map<String, Object> args) {
        return new Territory(
                (String) args.get("tag"),
                (String) args.get("name"),
                (int) args.get("color"),
                (boolean) args.get("colony")
        );
    }
}
