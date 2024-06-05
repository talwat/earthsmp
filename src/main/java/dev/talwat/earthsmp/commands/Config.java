package dev.talwat.earthsmp.commands;

import dev.talwat.earthsmp.Earthsmp;
import dev.talwat.earthsmp.config.NationsConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import static java.lang.String.format;

class Config extends SubCommand {
    public Config(Earthsmp plugin) {
        super(plugin);
    }

    private boolean nation(@NotNull CommandSender sender, @NotNull String[] args) {
        NationsConfig nationsConfig = NationsConfig.Load(plugin);
        List<Map<String, Object>> nations = nationsConfig.parsed;

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

                UUID ruler = Bukkit.getOfflinePlayer(args[7]).getUniqueId();
                nation.put("ruler", ruler.toString());
                nation.put("members", new String[0]);

                nations.add(nation);
                sender.sendPlainMessage(format("Added %s (%s)!", nation.get("name"), nation.get("tag")));

                break;
            case "remove", "delete":
                int index = nationsConfig.findNationByTag(args[3]);
                if (index == -1) {
                    sender.sendPlainMessage(format("%s not found!", args[3]));
                } else {
                    nations.remove(index);
                    sender.sendPlainMessage(format("Deleted %s!", args[3]));
                }

                break;
            default:
                return false;
        }

        try {
            nationsConfig.Save();
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Couldn't save the config file", e);
        }

        return true;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length <= 1) {
            return false;
        }

        return switch (args[1]) {
            case "nation", "nations" -> nation(sender, args);
            default -> true;
        };

    }
}