package dev.talwat.earthsmp.commands;

import dev.talwat.earthsmp.Earthsmp;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

class Sign extends SubCommand {
    public Sign(Earthsmp plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.lore() != null) {
            player.sendPlainMessage("This item cannot be signed or it has already been signed!");
            return true;
        }

        item.lore(List.of(Component.text("Signed by ", NamedTextColor.WHITE).append(Component.text(player.getName(), plugin.accent))));
        player.sendPlainMessage("Successfully signed the item!");

        return true;
    }
}