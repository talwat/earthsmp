package dev.talwat.earthsmp.commands;

import dev.talwat.earthsmp.Earthsmp;
import dev.talwat.earthsmp.nations.Nation;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static java.lang.String.format;
import static org.bukkit.Bukkit.getServer;

public class Flag extends SubCommand {
    public Flag(Earthsmp plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        Nation nation = plugin.borders.getNationFromRuler(player.getUniqueId());
        if (nation == null) {
            player.sendPlainMessage("Only rulers can spawn flags!");

            return true;
        }

        if (nation.flag() == null) {
            player.sendPlainMessage("Your nation doesn't have a flag yet!");

            return true;
        }

        getServer().dispatchCommand(Bukkit.getConsoleSender(), format("give %s %s 16", player.getName(), nation.flag()));

        return true;
    }
}
