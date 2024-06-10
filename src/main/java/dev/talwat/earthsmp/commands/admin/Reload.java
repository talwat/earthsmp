package dev.talwat.earthsmp.commands.admin;

import dev.talwat.earthsmp.Earthsmp;
import dev.talwat.earthsmp.commands.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class Reload extends SubCommand {
    public Reload(Earthsmp plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendPlainMessage("Reloading...");

        if (args.length > 1) {
            switch(args[1]) {
                case "news":
                    plugin.news.Load();
                    break;
                case "borders":
                    plugin.borders.Load();
                    break;
                case "markers":
                    plugin.markers.Load();
                    break;
                case "invites":
                    plugin.inviteRequests = new HashMap<>();
                    break;
            }
        } else {
            plugin.borders.Load();
            plugin.markers.Load();
            plugin.news.Load();
            plugin.inviteRequests = new HashMap<>();
        }

        sender.sendPlainMessage("Done!");

        return true;
    }
}