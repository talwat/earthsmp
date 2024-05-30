package dev.talwat.earthsmp;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

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
        plugin.getLogger().info(String.valueOf(plugin.borders.getColor(event.getInteractionPoint().toBlockLocation())));
    }
}
