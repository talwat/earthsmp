package dev.talwat.earthsmp.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static dev.talwat.earthsmp.config.NationsConfig.getUUIDs;
import static java.lang.String.format;
import static org.bukkit.Bukkit.getServer;

public class Nation {
    public String tag;
    public String name;
    public String nick;
    public int hue;
    public UUID ruler;
    public UUID[] members;
    public Map<Integer, Territory> territories;

    public Nation(String tag, String name, String nick, int hue, UUID ruler, UUID[] members, Map<Integer, Territory> territories) {
        this.tag = tag;
        this.name = name;
        this.nick = nick;
        this.hue = hue;
        this.ruler = ruler;
        this.members = members;
        this.territories = territories;
    }

    public static Nation deserialize(Map<String, Object> args) {
        getServer().getLogger().info(format("%s", args.get("members")));
        UUID[] uuids = getUUIDs((List<String>) args.get("members"));

        Map<Integer, Territory> territories = new HashMap<>();
        Object rawTerritories = args.get("territories");

        if (rawTerritories != null) {
            for (Map<String, Object> raw : (List<Map<String, Object>>) rawTerritories) {
                Territory territory = Territory.deserialize(raw);
                territories.put(territory.saturation, territory);
            }
        }

        return new Nation(
                (String) args.get("tag"),
                (String) args.get("name"),
                (String) args.get("nick"),
                (int) args.get("color"),
                UUID.fromString((String) args.get("ruler")),
                uuids,
                territories
        );
    }
}
