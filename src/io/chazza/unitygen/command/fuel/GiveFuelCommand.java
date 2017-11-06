package io.chazza.unitygen.command.fuel;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.contexts.OnlinePlayer;
import io.chazza.unitygen.Lang;
import io.chazza.unitygen.Main;
import io.chazza.unitygen.util.ColorUtil;
import io.chazza.unitygen.util.FuelUtil;
import io.chazza.unitygen.util.GenUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

@CommandAlias("%cmd")
public class GiveFuelCommand extends BaseCommand {

    @Subcommand("givefuel")
    public void onGive(CommandSender sender, OnlinePlayer player, String tier, @Optional Integer amount) {
        if (sender.hasPermission("unitygen.give")) {
            if (FuelUtil.getCaseGenName(tier) == null) {
                sender.sendMessage(Lang.INVALID_FUEL.get().replace("%tier%", tier));
                return;
            } else {
                tier = FuelUtil.getCaseGenName(tier);
                if(amount == null) amount = 1;

                ItemStack is = FuelUtil.createFuel(tier, amount);

                if (player.getPlayer().getInventory().firstEmpty() <= -1) {
                    player.getPlayer().getWorld().dropItem(player.getPlayer().getLocation().add(0, 1, 0), is);
                } else {
                    player.getPlayer().getInventory().addItem(is);
                }
                sender.sendMessage(Lang.GIVEN_FUEL.get()
                    .replace("%tier%", tier)
                    .replace("%player%", player.getPlayer().getName())
                    .replace("%amount%", String.valueOf(amount)));
                if (player.getPlayer() != sender)
                    player.getPlayer().sendMessage(Lang.RECEIVED_FUEL.get()
                        .replace("%amount%", String.valueOf(amount)).replace("%tier%", tier));
            }
        } else
            sender.sendMessage(Lang.NO_PERMISSION.get());
    }
}
