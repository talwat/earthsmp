package dev.talwat.earthsmp.commands.admin;

import dev.talwat.earthsmp.Borders;
import dev.talwat.earthsmp.Earthsmp;
import dev.talwat.earthsmp.commands.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadBorders extends SubCommand {
    public ReloadBorders(Earthsmp plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendPlainMessage("Reloading borders...");
        plugin.borders = new Borders(plugin);
        sender.sendPlainMessage("Done!");

        return true;
    }
}