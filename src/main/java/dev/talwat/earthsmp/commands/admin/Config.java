package dev.talwat.earthsmp.commands.admin;

import dev.talwat.earthsmp.Earthsmp;
import dev.talwat.earthsmp.commands.SubCommand;
import dev.talwat.earthsmp.nations.Nation;
import dev.talwat.earthsmp.nations.NationsConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.lang.String.format;

public class Config extends SubCommand {
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
                if (args.length <= 6) {
                    return false;
                }

                UUID ruler = null;
                List<UUID> members = new ArrayList<>();
                if (args.length > 7) {
                    ruler = Bukkit.getOfflinePlayer(args[7]).getUniqueId();
                    members.add(ruler);
                }

                Nation nation = new Nation(
                        args[3],
                        args[4].replace('_', ' '),
                        args[5].replace('_', ' '),
                        Integer.parseInt(args[6]),
                        ruler,
                        null,
                        null,
                        members,
                        null
                );

                nations.add(nation.serialize());
                sender.sendPlainMessage(format("Added %s (%s)!", nation.name(), nation.tag()));

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

        nationsConfig.Save(plugin);

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