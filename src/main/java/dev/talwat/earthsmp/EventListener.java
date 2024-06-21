package dev.talwat.earthsmp;

import dev.talwat.earthsmp.nations.Nation;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.HSVLike;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import static java.lang.String.format;

public class EventListener implements Listener {
    private final Earthsmp plugin;

    public EventListener(Earthsmp plugin) {
        this.plugin = plugin;
    }

    private boolean isAllowed(Location pos, Player player) {
        Nation nation = plugin.borders.getNationFromLocation(pos);

        if (nation == null || (player != null && nation.members().contains(player.getUniqueId()))) {
            return true;
        }

        if (player != null) {
            player.sendPlainMessage(format("You aren't allowed to do this in %s!", nation.nick()));
        }

        return false;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntityType() == EntityType.PLAYER || event.getEntity() instanceof Monster) {
            return;
        }

        if (event.getDamager().getType() != EntityType.PLAYER) {
            return;
        }

        Player player = ((Player) event.getDamager());

        if (isAllowed(event.getEntity().getLocation(), player)) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (event.useItemInHand() == Event.Result.ALLOW) {
            plugin.getLogger().info("Allowed!");
            return;
        }

        Block interacted = event.getClickedBlock();
        if (interacted == null) {
            return;
        }

        Location pos = interacted.getLocation();

        if (isAllowed(pos, event.getPlayer())) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onExplosion(EntityExplodeEvent event) {
        event.blockList().clear();
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.renderer((source, sourceDisplayName, message, viewer) -> {
            Component prefix;
            Nation nation = plugin.borders.cache.playerToNation(source);
            if (nation == null) {
                prefix = Component.text("[Uncivilized]");
            } else {
                prefix = Component.text('[', Style.empty()).
                        append(Component.text(nation.nick(), TextColor.color(HSVLike.hsvLike(nation.hue() / 360.0f, 0.75f, 1f))))
                        .append(Component.text(']', Style.empty()));
            }

            return prefix.appendSpace().append(sourceDisplayName).append(Component.text(": ")).append(message);
        });
    }
}
