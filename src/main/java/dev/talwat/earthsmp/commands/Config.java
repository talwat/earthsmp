package dev.talwat.earthsmp.commands;

import dev.talwat.earthsmp.Earthsmp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

import static java.lang.String.format;

class Config extends SubCommand {
    private boolean nation(@NotNull CommandSender sender, @NotNull String[] args) {
        File file = new File(plugin.getDataFolder(), "nations.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        List<Map<String, Object>> nations = (List<Map<String, Object>>) config.getList("nations");
        if (nations == null) {
            return true;
        }

        switch (args[2]) {
            case "add", "new":
                Map<String, Object> nation = new LinkedHashMap<>();
                nation.put("tag", args[3]);
                nation.put("name", args[4].replace("_", " "));
                nation.put("nick", args[5].replace("_", " "));
                nation.put("color", Integer.valueOf(args[6]));
                nation.put("ruler", UUID.fromString(args[7]));
                nation.put("members", new String[0]);

                nations.add(nation);
                sender.sendPlainMessage(format("Added %s (%s)!", nation.get("name"), nation.get("tag")));

                break;
            case "remove", "delete":
                int i = 0;

                for (; i < nations.size(); i++) {
                    String tag = (String) nations.get(i).get("tag");

                    if (Objects.equals(tag, args[3])) {
                        nations.remove(i);
                        sender.sendPlainMessage(format("Removed %s!", tag));
                        break;
                    }
                }

                break;
            default:
                return false;
        }

        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Couldn't save the config file", e);
        }

        return true;
    }

    public Config(Earthsmp plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length <= 1) {
            return false;
        }

        return switch (args[1]) {
            case "nation" -> nation(sender, args);
            default -> true;
        };

    }
}