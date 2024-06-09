package dev.talwat.earthsmp.commands;

import dev.talwat.earthsmp.Earthsmp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

class Help extends SubCommand {
    public Help(Earthsmp plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendPlainMessage(
                "\nearth test\nearth member add <username>\nearth member kick <username>\nearth accept <tag>\nearth decline\nearth leave\nearth marker new <label> <type>\nearth marker list\nearth marker delete <index>\n"
        );

        if (sender.isOp()) {
            sender.sendPlainMessage(
                    "earth reload\nearth config nation add <tag> <name> <nick> <hue> <ruler>\n"
            );
        }

        return true;
    }
}