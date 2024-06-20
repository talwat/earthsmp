package dev.talwat.earthsmp;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.*;

public class VehicleEventListener implements Listener {
    @EventHandler(priority = EventPriority.LOW)
    public void onVehicleCreate(VehicleCreateEvent event) {
        event.setCancelled(false);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onVehicleBreak(VehicleDestroyEvent event) {
        event.setCancelled(false);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onVehicleEnter(VehicleEnterEvent event) {
        event.setCancelled(false);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onVehicleExit(VehicleExitEvent event) {
        event.setCancelled(false);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onVehicleDamage(VehicleDamageEvent event) {
        event.setCancelled(false);
    }
}
