package dev.talwat.earthsmp.commands.invites;

import dev.talwat.earthsmp.Earthsmp;
import dev.talwat.earthsmp.InviteRequest;
import dev.talwat.earthsmp.commands.SubCommand;
import dev.talwat.earthsmp.nations.Nation;
import dev.talwat.earthsmp.nations.NationsConfig;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.lang.String.format;
import static org.bukkit.Bukkit.getOfflinePlayer;

public class Manage extends SubCommand {
    public Manage(Earthsmp plugin) {
        super(plugin);
    }

    private int getCountryFromRuler(CommandSender sender, NationsConfig config) {
        for (int i = 0; i < config.parsed.size(); i++) {
            Nation deserialized = Nation.deserialize(config.parsed.get(i));

            if (sender instanceof Player && ((Player) sender).getUniqueId().equals(deserialized.ruler())) {
                return i;
            }
        }

        return -1;
    }

    private boolean add(@NotNull CommandSender sender, @NotNull String[] args, NationsConfig config) {
        int i = getCountryFromRuler(sender, config);

        if (i == -1) {
            sender.sendPlainMessage("You're not the ruler, so you can't invite people!");
            return true;
        }

        if (args.length <= 2) {
            return false;
        }

        OfflinePlayer player = getOfflinePlayer(args[2]);

        if (!player.hasPlayedBefore()) {
            sender.sendPlainMessage("Player not found. Have they played before?");
        }
        UUID playerUUID = player.getUniqueId();

        for (Map.Entry<Integer, Nation> nation : plugin.borders.nations.entrySet()) {
            if (nation.getValue().members().contains(playerUUID)) {
                sender.sendPlainMessage(format("%s is already a part of %s! Tell them to leave before inviting them.", args[2], nation.getValue().name()));
                return true;
            }
        }

        if (plugin.inviteRequests.containsKey(playerUUID)) {
            sender.sendPlainMessage(format("%s already has a pending invite, tell them to decline before sending another.", args[2]));
        }

        Map<String, Object> nation = config.parsed.get(i);
        plugin.inviteRequests.put(playerUUID, new InviteRequest(nation.get("tag").toString()));

        Player online = Bukkit.getPlayer(playerUUID);
        if (online != null) {
            online.sendPlainMessage(format("You have received an invite to join %s.", nation.get("name").toString()));
            online.sendPlainMessage(format("Run /earth accept %s to accept the invite.", nation.get("tag")));
        }

        sender.sendPlainMessage(format("Invite request sent to %s!", getOfflinePlayer(args[2]).getName()));
        return true;
    }

    private boolean kick(@NotNull CommandSender sender, @NotNull String[] args, NationsConfig config) {
        int i = getCountryFromRuler(sender, config);

        if (i == -1) {
            sender.sendPlainMessage("You're not the ruler, so you can't kick people!");
            return true;
        }

        if (args.length <= 2) {
            return false;
        }

        UUID player = getOfflinePlayer(args[2]).getUniqueId();
        Map<String, Object> nation = config.parsed.get(i);

        boolean result = ((List<String>) nation.get("members")).remove(player.toString());
        if (result) {
            sender.sendPlainMessage(format("Successfully expelled %s!", args[2]));
            plugin.borders.loadNations();
        } else {
            sender.sendPlainMessage(format("Couldn't find %s!", args[2]));
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
            case "kick", "remove" -> kick(sender, args, config);
            default -> false;
        };
    }
}