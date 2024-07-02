package dev.talwat.earthsmp.commands;

import dev.talwat.earthsmp.Earthsmp;
import dev.talwat.earthsmp.commands.admin.Config;
import dev.talwat.earthsmp.commands.admin.Reload;
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
                case "reload":
                    return new Reload(plugin);
                case "config":
                    return new Config(plugin);
            }
        }

        return switch (args[0]) {
            case "test" -> new Test(plugin);
            case "news" -> new News(plugin);
            case "member", "members" -> new dev.talwat.earthsmp.commands.members.Manage(plugin);
            case "accept" -> new dev.talwat.earthsmp.commands.members.Accept(plugin);
            case "decline" -> new dev.talwat.earthsmp.commands.members.Decline(plugin);
            case "leave" -> new dev.talwat.earthsmp.commands.members.Leave(plugin);
            case "marker", "markers" -> new dev.talwat.earthsmp.commands.Markers(plugin);
            case "flag" -> new Flag(plugin);
            case "abdicate" -> new TransferLeader(plugin);
            case "sign" -> new Sign(plugin);
            case "help" -> new Help(plugin);
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
