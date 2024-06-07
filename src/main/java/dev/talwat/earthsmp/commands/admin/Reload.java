package dev.talwat.earthsmp.commands.admin;

import dev.talwat.earthsmp.Earthsmp;
import dev.talwat.earthsmp.commands.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Reload extends SubCommand {
    public Reload(Earthsmp plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendPlainMessage("Reloading...");
        plugin.borders.Load();
        plugin.markers.Load();
        sender.sendPlainMessage("Done!");

        return true;
    }
}