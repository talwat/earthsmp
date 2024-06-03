package dev.talwat.earthsmp.commands;

import dev.talwat.earthsmp.BorderImage;
import dev.talwat.earthsmp.Earthsmp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static java.lang.String.format;

class ReloadBorders extends SubCommand {
    public ReloadBorders(Earthsmp plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) {
            sender.sendPlainMessage("Nice try, bozo.");
        }

        sender.sendPlainMessage("Reloading borders...");
        plugin.borders = new BorderImage(plugin);
        sender.sendPlainMessage("Done!");

        return true;
    }
}