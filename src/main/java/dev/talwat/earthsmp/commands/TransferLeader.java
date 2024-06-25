package dev.talwat.earthsmp.commands;

import dev.talwat.earthsmp.Earthsmp;
import dev.talwat.earthsmp.nations.NationsConfig;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import static java.lang.String.format;

class TransferLeader extends SubCommand {
    public TransferLeader(Earthsmp plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length <= 1) {
            return false;
        }

        NationsConfig config = NationsConfig.Load(plugin);
        int i = config.get(sender);

        if (i == -1) {
            sender.sendPlainMessage("You're not the ruler, so you can't abdicate!");
            return true;
        }

        Map<String, Object> nation = config.parsed.get(i);
        OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
        String newRuler = player.getUniqueId().toString();

        for (String member : (List<String>) nation.get("members")) {
            if (newRuler.equals(member)) {
                nation.put("ruler", newRuler);

                sender.sendPlainMessage(format("Transferred control of %s to %s!", nation.get("name"), player.getName()));

                config.Save(plugin);
                plugin.borders.Load();

                return true;
            }
        }

        sender.sendPlainMessage(format("Couldn't find %s in your nation.", args[1]));

        return true;
    }
}