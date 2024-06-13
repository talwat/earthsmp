package dev.talwat.earthsmp.commands;

import dev.talwat.earthsmp.Earthsmp;
import dev.talwat.earthsmp.mapmarkers.MapMarker;
import dev.talwat.earthsmp.mapmarkers.MapMarkerType;
import dev.talwat.earthsmp.nations.Nation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

import static dev.talwat.earthsmp.mapmarkers.MapMarkers.getSection;
import static java.lang.String.format;


class Markers extends SubCommand {
    public Markers(Earthsmp plugin) {
        super(plugin);
    }

    private boolean checkAddition(Map<String, List<Map<String, Object>>> nationMarkers, String[] args, String tag, Player player) {
        var entry = getSection(nationMarkers);
        if (entry == null) {
            return false;
        }

        if (Objects.equals(entry.getKey(), tag)) {
            if (entry.getValue() == null) {
                entry.setValue(new ArrayList<>());
            } else {
                if (entry.getValue().size() > 200) {
                    player.sendPlainMessage("You've exceeded the max amount of markers! You should probably delete some.");
                    return true;
                }
            }

            entry.getValue().add(new MapMarker(args[2], MapMarkerType.valueOf(args[3]), player.getLocation()).serialize());

            return true;
        } else {
            return false;
        }
    }

    private boolean add(@NotNull Player player, @NotNull String[] args, String tag, List<Map<String, List<Map<String, Object>>>> markers) {
        Nation nation = plugin.borders.getNationFromLocation(player.getLocation());
        if (nation == null || !nation.tag().equals(tag)) {
            player.sendPlainMessage("You can't make a marker outside your own territory!");
            return true;
        }

        if (args.length <= 3) {
            return false;
        }

        player.sendPlainMessage("Adding marker...");

        for (Map<String, List<Map<String, Object>>> nationMarkers : markers) {
            if (checkAddition(nationMarkers, args, tag, player)) {
                player.sendPlainMessage("Added marker!");

                return true;
            }
        }

        Map<String, List<Map<String, Object>>> newEntry = new LinkedHashMap<>();
        newEntry.put(tag, null);

        markers.add(newEntry);
        checkAddition(newEntry, args, tag, player);

        player.sendPlainMessage("Created new marker section & added marker!");

        return true;
    }

    private boolean remove(@NotNull Player player, @NotNull String[] args, String tag, List<Map<String, List<Map<String, Object>>>> markers) {
        player.sendPlainMessage("Deleting marker...");

        if (args.length <= 2) {
            return false;
        }

        for (Map<String, List<Map<String, Object>>> nationMarkers : markers) {
            var entry = getSection(nationMarkers);
            if (entry == null) {
                continue;
            }

            if (Objects.equals(entry.getKey(), tag)) {
                if (entry.getValue() == null) {
                    player.sendPlainMessage("Marker section is empty!");

                    return true;
                }

                int index = Integer.parseInt(args[2]);

                if (entry.getValue().size() > index) {
                    entry.getValue().remove(index);
                    player.sendPlainMessage("Removed marker!");
                } else {
                    player.sendPlainMessage("Marker doesn't exist!");
                }

                return true;
            }

            plugin.getLogger().info(format("%s", entry));
        }

        player.sendPlainMessage("You haven't made any markers yet.");

        return true;
    }

    private boolean list(@NotNull Player player, String tag) {
        int i = 0;
        List<MapMarker> nationMarker = plugin.markers.markers.get(tag);
        if (nationMarker == null) {
            player.sendPlainMessage("You haven't made any markers yet!");
            return true;
        }

        for (MapMarker marker : plugin.markers.markers.get(tag)) {
            player.sendPlainMessage(format("%s - %s - (%s, %s)", i, marker.label(), marker.pos().getBlockX(), marker.pos().getBlockZ()));

            i++;
        }

        return true;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length <= 1) {
            return false;
        }

        if (!(sender instanceof Player player)) {
            return true;
        }

        String tag = null;

        File file = new File(plugin.getDataFolder(), "markers.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (Nation nation : plugin.borders.nations.values()) {
            if (nation.ruler() == null) {
                continue;
            }

            if (nation.ruler().equals(player.getUniqueId())) {
                tag = nation.tag();
            }
        }

        if (tag == null) {
            sender.sendPlainMessage("You have to be a ruler to manage markers!");
        }

        if (args[1].equals("list")) {
            list(player, tag);

            return true;
        }

        List<Map<String, List<Map<String, Object>>>> markers = (List<Map<String, List<Map<String, Object>>>>) config.getList("markers");

        boolean result = switch (args[1]) {
            case "new", "add" -> add(player, args, tag, markers);
            case "delete", "remove" -> remove(player, args, tag, markers);
            default -> false;
        };

        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Couldn't save marker config.", e);
        }

        plugin.markers.Load();

        return result;
    }
}