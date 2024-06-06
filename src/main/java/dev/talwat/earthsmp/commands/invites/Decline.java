package dev.talwat.earthsmp.commands.invites;

import dev.talwat.earthsmp.Earthsmp;
import dev.talwat.earthsmp.InviteRequest;
import dev.talwat.earthsmp.commands.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Decline extends SubCommand {
    public Decline(Earthsmp plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        InviteRequest invite = plugin.inviteRequests.get(player.getUniqueId());
        if (invite == null) {
            sender.sendPlainMessage("Invite doesn't exist.");
            return true;
        }

        plugin.inviteRequests.remove(player.getUniqueId());
        sender.sendPlainMessage("Successfully declined invite!");

        return true;
    }
}