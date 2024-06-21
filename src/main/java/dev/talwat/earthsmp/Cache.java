package dev.talwat.earthsmp;

import dev.talwat.earthsmp.nations.Nation;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Cache {
    private final Map<UUID, Nation> playerToNation;
    private final Borders borders;

    public Cache(Borders borders) {
        this.borders = borders;
        this.playerToNation = new HashMap<>();
    }

    public Nation playerToNation(Player player) {
        if (!playerToNation.containsKey(player.getUniqueId())) {
            playerToNation.put(player.getUniqueId(), null);

            for (Nation nation : borders.nations.values()) {
                if (nation.members().contains(player.getUniqueId())) {
                    playerToNation.put(player.getUniqueId(), nation);
                    break;
                }
            }
        }

        return playerToNation.get(player.getUniqueId());
    }
}
