package dev.talwat.earthsmp.commands;

import dev.talwat.earthsmp.Earthsmp;
import dev.talwat.earthsmp.commands.admin.Config;
import dev.talwat.earthsmp.commands.admin.ReloadBorders;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public final class Handler implements CommandExecutor {
    private final Earthsmp plugin;

    public Handler(Earthsmp plugin) {
        this.plugin = plugin;
    }

    private SubCommand getSubcommand(String[] args, Earthsmp plugin, CommandSender sender) {
        if (args.length == 0) {
            return null;
        }

        if (sender.isOp()) {
            switch (args[0]) {
                case "reloadborders":
                    return new ReloadBorders(plugin);
                case "config":
                    return new Config(plugin);
            }
        }

        return switch (args[0]) {
            case "test" -> new Test(plugin);
            case "member", "members" -> new dev.talwat.earthsmp.commands.invites.Manage(plugin);
            case "accept" -> new dev.talwat.earthsmp.commands.invites.Accept(plugin);
            case "decline" -> new dev.talwat.earthsmp.commands.invites.Decline(plugin);
            default -> null;
        };

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        CommandExecutor subcommand = getSubcommand(args, plugin, sender);
        if (subcommand == null) {
            return false;
        }

        return subcommand.onCommand(sender, command, label, args);
    }
}
