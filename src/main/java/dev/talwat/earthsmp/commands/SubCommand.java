package dev.talwat.earthsmp.commands;

import dev.talwat.earthsmp.Earthsmp;
import org.bukkit.command.CommandExecutor;

public abstract class SubCommand implements CommandExecutor {
    public Earthsmp plugin;

    public SubCommand(Earthsmp plugin) {
        this.plugin = plugin;
    }
}
