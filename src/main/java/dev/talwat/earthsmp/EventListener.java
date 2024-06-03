package dev.talwat.earthsmp;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.awt.*;

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
        plugin.getLogger().info("Interaction!");

        Location pos = event.getInteractionPoint();

        if (pos == null) {
            return;
        }

        pos = pos.toBlockLocation();

        Color color = plugin.borders.getColor(pos);
        plugin.getLogger().info(format("%s", color));
    }
}
