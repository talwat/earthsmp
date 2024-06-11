package dev.talwat.earthsmp.commands;

import dev.talwat.earthsmp.Earthsmp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;

class News extends SubCommand {
    public News(Earthsmp plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (plugin.news.current == null) {
            sender.sendPlainMessage("There aren't any articles yet!");

            return true;
        }

        sender.sendPlainMessage(DateFormat.getDateInstance(DateFormat.MEDIUM).format(plugin.news.current.getKey()));
        sender.sendPlainMessage(plugin.news.current.getValue());
        return true;
    }
}