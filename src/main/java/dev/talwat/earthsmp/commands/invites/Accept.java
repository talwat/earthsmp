package dev.talwat.earthsmp.commands.invites;

import dev.talwat.earthsmp.Earthsmp;
import dev.talwat.earthsmp.InviteRequest;
import dev.talwat.earthsmp.commands.SubCommand;
import dev.talwat.earthsmp.nations.NationsConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static java.lang.String.format;

public class Accept extends SubCommand {
    public Accept(Earthsmp plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        InviteRequest invite = plugin.inviteRequests.get(player.getUniqueId());

        if (invite == null) {
            sender.sendPlainMessage("Invite didn't exist.");

            return true;
        } else if (!Objects.equals(invite.nation, args[1])) {
            sender.sendPlainMessage("Nation didn't match.");

            return true;
        }

        NationsConfig config = NationsConfig.Load(plugin);
        Map<String, Object> nation = config.parsed.get(config.findNationByTag(invite.nation));
        ((List<String>) nation.get("members")).add(player.getUniqueId().toString());

        plugin.inviteRequests.remove(player.getUniqueId());

        String nationName = (String) nation.get("name");

        sender.sendPlainMessage(format("Successfully joined %s!", nationName));
        Player ruler = Bukkit.getPlayer(UUID.fromString((String) nation.get("ruler")));
        if (ruler != null) {
            ruler.sendPlainMessage(format("%s has joined %s!", player.getName(), nationName));
        }

        plugin.borders.loadNations();
        config.Save(plugin);

        return true;
    }
}