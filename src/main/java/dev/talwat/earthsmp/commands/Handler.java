package dev.talwat.earthsmp.commands;

import dev.talwat.earthsmp.Earthsmp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static java.lang.String.format;

public final class Handler implements CommandExecutor {
    private final Earthsmp plugin;

    public Handler(Earthsmp plugin) {
        this.plugin = plugin;
    }

    private SubCommand getSubcommand(String[] args, Earthsmp plugin) {
        if (args.length == 0) {
            return null;
        }

        switch (args[0]) {
            case "test":
                return new Test(plugin);
            case "reloadborders":
                return new ReloadBorders(plugin);
            default:
                return null;
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        CommandExecutor subcommand = getSubcommand(args, plugin);
        if (subcommand == null) {
            return false;
        }

        return subcommand.onCommand(sender, command, label, args);
    }
}
