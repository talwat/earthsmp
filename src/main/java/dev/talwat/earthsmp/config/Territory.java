package dev.talwat.earthsmp.config;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static dev.talwat.earthsmp.config.NationsConfig.getUUIDs;

public class Territory {
    public String tag;
    public String name;
    public int saturation;
    public UUID ruler;
    public UUID[] members;
    public boolean colony;

    public Territory(String tag, String name, int saturation, UUID ruler, UUID[] members, boolean colony) {
        this.tag = tag;
        this.name = name;
        this.saturation = saturation;
        this.ruler = ruler;
        this.members = members;
        this.colony = colony;
    }

    public static Territory deserialize(Map<String, Object> args) {
        return new Territory(
                (String) args.get("tag"),
                (String) args.get("name"),
                (int) args.get("color"),
                UUID.fromString((String) args.get("ruler")),
                getUUIDs((List<String>) args.get("members")),
                (boolean) args.get("colony")
        );
    }
}
