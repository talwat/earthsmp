package dev.talwat.earthsmp;

import dev.talwat.earthsmp.nations.Nation;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.HSVLike;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;

import static java.lang.String.format;

public class EventListener implements Listener {
    private final Earthsmp plugin;

    public EventListener(Earthsmp plugin) {
        this.plugin = plugin;
    }

//    @EventHandler(priority = EventPriority.LOWEST)
//    public void onBlockPlace(BlockPlaceEvent event) {
//        event.setCancelled(true);
//    }
//
//    @EventHandler(priority = EventPriority.LOWEST)
//    public void onBlockBreak(BlockBreakEvent event) {
//        event.setCancelled(true);
//    }
//
//    @EventHandler(priority = EventPriority.LOWEST)
//    public void onDamage(EntityDamageEvent event) {
//        if (event.getEntityType() == EntityType.PLAYER || event.getEntity() instanceof Monster) {
//            return;
//        }
//
//        event.setCancelled(true);
//    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractEvent event) {
        Location pos = event.getInteractionPoint();

        if (pos == null) {
            return;
        }

        Nation nation = plugin.borders.getNationFromLocation(pos);
        if (nation == null || nation.members.contains(event.getPlayer().getUniqueId())) {
            return;
        }

        event.getPlayer().sendPlainMessage(format("You aren't allowed to do this in %s!", nation.nick));
        event.setCancelled(true);
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.renderer((source, sourceDisplayName, message, viewer) -> {
            if (!plugin.borders.playerCache.containsKey(source.getUniqueId())) {
                plugin.borders.playerCache.put(source.getUniqueId(), null);

                for (Map.Entry<Integer, Nation> nation : plugin.borders.nations.entrySet()) {
                    if (nation.getValue().members.contains(source.getUniqueId())) {
                        plugin.borders.playerCache.put(source.getUniqueId(), nation.getValue());

                        break;
                    }
                }
            }

            plugin.getLogger().info(format("%s", plugin.borders.playerCache));

            Component prefix;
            Nation nation = plugin.borders.playerCache.get(source.getUniqueId());
            if (nation == null) {
                prefix = Component.text("[Uncivilized]");
            } else {
                prefix = Component.text('[', Style.empty()).
                        append(Component.text(nation.nick, TextColor.color(HSVLike.hsvLike(nation.hue / 360.0f, 0.75f, 1f))))
                        .append(Component.text(']', Style.empty()));
            }

            return prefix.appendSpace().append(sourceDisplayName).append(Component.text(": ")).append(message);
        });
    }
}
