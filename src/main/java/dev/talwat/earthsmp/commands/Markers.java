package dev.talwat.earthsmp.commands;

import dev.talwat.earthsmp.Earthsmp;
import dev.talwat.earthsmp.IconMarker;
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


class Markers extends SubCommand {
    public Markers(Earthsmp plugin) {
        super(plugin);
    }

    private boolean checkAddition(Map<String, List<Map<String, Object>>> nationMarkers, String[] args, String tag, Player player) {
        Map.Entry<String, List<Map<String, Object>>> entry = nationMarkers.entrySet().stream().findFirst().get();
        if (Objects.equals(entry.getKey(), tag)) {
            if (entry.getValue() == null) {
                entry.setValue(new ArrayList<>());
            }

            entry.getValue().add(new IconMarker(args[2], args[3], player.getLocation()).serialize());

            return true;
        } else {
            return false;
        }
    }

    private boolean add(@NotNull Player player, @NotNull String[] args, String tag, YamlConfiguration config) {
        player.sendPlainMessage("Adding marker...");
        List<Map<String, List<Map<String, Object>>>> markers = (List<Map<String, List<Map<String, Object>>>>) config.getList("markers");

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

    private boolean remove(@NotNull Player player, @NotNull String[] args, String tag, YamlConfiguration config) {


        return true;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length <= 1) {
            return false;
        }

        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        String tag = null;
        File file = new File(plugin.getDataFolder(), "markers.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (Nation nation : plugin.borders.nations.values()) {
            if (nation.ruler.equals(player.getUniqueId())) {
                tag = nation.tag;
            }
        }

        if (tag == null) {
            sender.sendPlainMessage("You have to be the ruler to manage markers!");
        }

        boolean result = switch (args[1]) {
            case "new", "add" -> add(player, args, tag, config);
            case "delete", "remove" -> remove(player, args, tag, config);
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