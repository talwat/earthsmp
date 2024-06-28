package dev.talwat.earthsmp.nations;

import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.HSVLike;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static dev.talwat.earthsmp.nations.NationsConfig.getUUIDs;

public record Nation(@NotNull String tag, @NotNull String name, @NotNull String nick, int hue, @Nullable UUID ruler,
                     @Nullable String flag, @Nullable String master, @NotNull List<UUID> members,
                     @Nullable Map<Integer, Territory> territories) implements ConfigurationSerializable {

    public static Nation deserialize(Map<String, Object> args) {
        List<String> rawMembers = (List<String>) args.get("members");
        List<UUID> uuids = List.of();
        if (rawMembers != null) {
            uuids = getUUIDs(rawMembers);
        }

        Map<Integer, Territory> territories = null;
        Object rawTerritories = args.get("territories");

        if (rawTerritories != null) {
            territories = new HashMap<>();

            for (Map<String, Object> raw : (List<Map<String, Object>>) rawTerritories) {
                Territory territory = Territory.deserialize(raw);
                territories.put(territory.saturation(), territory);
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
                (String) args.get("flag"),
                (String) args.get("master"),
                uuids,
                territories
        );
    }

    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> nation = new LinkedHashMap<>();
        nation.put("tag", tag);
        nation.put("name", name);
        nation.put("nick", nick);
        nation.put("color", hue);

        if (this.ruler != null) {
            nation.put("ruler", ruler.toString());
        }

        if (this.flag != null) {
            nation.put("flag", flag);
        }

        if (this.master != null) {
            nation.put("master", master);
        }

        if (!this.members.isEmpty()) {
            nation.put("members", this.members.stream().map(UUID::toString).toList());
        }

        return nation;
    }

    public TextColor textColor() {
        return TextColor.color(HSVLike.hsvLike(hue / 360.0f, 0.75f, 1f));
    }
}
