package dev.talwat.earthsmp.commands;

import dev.talwat.earthsmp.Earthsmp;
import dev.talwat.earthsmp.nations.Nation;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static java.lang.String.format;

class PlayerList extends SubCommand {
    public PlayerList(Earthsmp plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        Nation nation = plugin.borders.getFromCache(player);
        if (nation == null) {
            sender.sendPlainMessage("You aren't part of a nation!");

            return true;
        }

        var names = nation.members().stream().map(x -> format("* %s", Bukkit.getOfflinePlayer(x).getName()));
        sender.sendPlainMessage(format("Members of: %s", nation.name()));
        sender.sendPlainMessage(String.join("\n", names.toList()));

        return true;
    }
}