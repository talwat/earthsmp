package dev.talwat.earthsmp;

import dev.talwat.earthsmp.nations.Nation;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class EventListener implements Listener {
    private final Earthsmp plugin;

    public EventListener(Earthsmp plugin) {
        this.plugin = plugin;
    }

    private boolean isAllowed(Location pos, Player player) {
        Nation nation = plugin.borders.get(pos);

        if (nation == null || (player != null && nation.members().contains(player.getUniqueId()))) {
            return true;
        }

        if (player != null) {
            player.sendActionBar(
                    Component.text("You aren't allowed to do this in ")
                            .append(Component.text(nation.nick(), nation.textColor())
                                    .append(Component.text("!", NamedTextColor.WHITE))));
        }

        return false;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPearl(PlayerTeleportEvent event) {
        if (isAllowed(event.getTo().toBlockLocation(), event.getPlayer())) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity().getType() == EntityType.VILLAGER) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntityType() == EntityType.PLAYER || event.getEntity() instanceof Monster) {
            event.setCancelled(false);
            return;
        }

        if (!(event.getDamager() instanceof Player player)) {
            return;
        }

        if (isAllowed(event.getEntity().getLocation(), player)) {
            event.setCancelled(false);
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (event.useItemInHand() == Event.Result.ALLOW) {
            return;
        }

        Block interacted = event.getClickedBlock();
        if (interacted == null) {
            return;
        }

        if (event.getAction().isRightClick()) {
            if (event.getMaterial().isEdible() || event.getMaterial().isEmpty()) {
                return;
            }
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

    @EventHandler(priority = EventPriority.LOW)
    public void onEat(FoodLevelChangeEvent event) {
        event.setCancelled(false);
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.renderer((source, sourceDisplayName, message, viewer) -> {
            Component prefix = plugin.borders.formatUsername(source, sourceDisplayName);
            return prefix.append(Component.text(": ")).append(message);
        });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().playerListName(plugin.borders.formatUsername(event.getPlayer(), null));
        event.getPlayer().sendPlayerListHeader(Component.text("Talwat's Earth SMP!\nPlugin Version:").appendSpace().append(Component.text(plugin.getPluginMeta().getVersion(), TextColor.color(60, 248, 100))));
        event.getPlayer().setScoreboard(plugin.borders.scoreboard);

        Nation nation = plugin.borders.getFromCache(event.getPlayer());
        if (nation != null) {
            plugin.borders.teams.get(nation.tag()).addPlayer(event.getPlayer());
        }
    }
}
