package dev.talwat.earthsmp.commands.members;

import dev.talwat.earthsmp.Earthsmp;
import dev.talwat.earthsmp.commands.SubCommand;
import dev.talwat.earthsmp.nations.NationsConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import static java.lang.String.format;

public class Leave extends SubCommand {
    public Leave(Earthsmp plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        NationsConfig config = NationsConfig.Load(plugin);
        for (Map<String, Object> nation : config.parsed) {
            List<String> members = (List<String>) nation.get("members");
            String uuid = player.getUniqueId().toString();

            if (nation.get("ruler") == uuid) {
                sender.sendPlainMessage("You must abdicate first!");
                return true;
            }

            boolean removed = members.remove(uuid);

            if (removed) {
                sender.sendPlainMessage(format("Left %s!", nation.get("name")));

                plugin.borders.Load();
                config.Save(plugin);

                return true;
            }
        }

        sender.sendPlainMessage("You aren't in any nations!");

        return true;
    }
}