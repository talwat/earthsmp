package dev.talwat.earthsmp.nations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static dev.talwat.earthsmp.nations.NationsConfig.getUUIDs;

public class Nation {
    public final String tag;
    public final String name;
    public final String nick;
    public final int hue;
    public final UUID ruler;
    public final List<UUID> members;
    public final Map<Integer, Territory> territories;

    public Nation(String tag, String name, String nick, int hue, UUID ruler, UUID[] members, Map<Integer, Territory> territories) {
        this.tag = tag;
        this.name = name;
        this.nick = nick;
        this.hue = hue;
        this.ruler = ruler;
        this.members = List.of(members);
        this.territories = territories;
    }

    public static Nation deserialize(Map<String, Object> args) {
        List<String> rawMembers = (List<String>) args.get("members");
        UUID[] uuids;
        if (rawMembers == null) {
            uuids = new UUID[0];
        } else {
            uuids = getUUIDs(rawMembers);
        }

        Map<Integer, Territory> territories = new HashMap<>();
        Object rawTerritories = args.get("territories");

        if (rawTerritories != null) {
            for (Map<String, Object> raw : (List<Map<String, Object>>) rawTerritories) {
                Territory territory = Territory.deserialize(raw);
                territories.put(territory.saturation, territory);
            }
        }

        String rawRuler = (String) args.get("ruler");
        UUID ruler = null;

        if (rawRuler != null) {
            ruler = UUID.fromString(rawRuler);
        }

        return new Nation(
                (String) args.get("tag"),
                (String) args.get("name"),
                (String) args.get("nick"),
                (int) args.get("color"),
                ruler,
                uuids,
                territories
        );
    }
}
