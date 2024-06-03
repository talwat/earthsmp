package dev.talwat.earthsmp.commands;

import dev.talwat.earthsmp.Earthsmp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

class Test extends SubCommand {
    public Test(Earthsmp plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendPlainMessage("hello!");
        return true;
    }
}