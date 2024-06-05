package dev.talwat.earthsmp.commands;

import dev.talwat.earthsmp.Earthsmp;
import dev.talwat.earthsmp.config.NationsConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import static org.bukkit.Bukkit.getOfflinePlayer;


class Member extends SubCommand {
    public Member(Earthsmp plugin) {
        super(plugin);
    }

    private boolean add(@NotNull CommandSender sender, @NotNull String[] args, NationsConfig config) {
        if (args.length <= 3) {
            return false;
        }

        Map<String, Object> toAddTo = config.parsed.get(config.findNationByTag(args[2]));
        UUID player = getOfflinePlayer(args[3]).getUniqueId();
        ((List<String>) toAddTo.get("members")).add(player.toString());

        try {
            config.Save();
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

        NationsConfig config = NationsConfig.Load(plugin);

        return switch (args[1]) {
            case "invite", "add" -> add(sender, args, config);
            default -> false;
        };
    }
}