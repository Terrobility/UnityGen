package io.chazza.unitygen.command.generator;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.contexts.OnlinePlayer;
import io.chazza.unitygen.Lang;
import io.chazza.unitygen.Main;
import io.chazza.unitygen.util.ColorUtil;
import io.chazza.unitygen.util.GenUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

@CommandAlias("%cmd")
public class GiveGenCommand extends BaseCommand {

    @Subcommand("give")
    public void onGive(CommandSender sender, OnlinePlayer player, String tier, @Optional Integer amount) {
        if (sender.hasPermission("unitygen.give")) {
            if (GenUtil.getCaseGenName(tier) == null) {
                sender.sendMessage(Lang.INVALID_GENERATOR.get().replace("%tier%", tier));
                return;
            } else {
                tier = GenUtil.getCaseGenName(tier);
                if(amount == null) amount = 1;

                ItemStack is = GenUtil.createGeneratorIS(tier, amount);

                if (player.getPlayer().getInventory().firstEmpty() <= -1) {
                    player.getPlayer().getWorld().dropItem(player.getPlayer().getLocation().add(0, 1, 0), is);
                } else {
                    player.getPlayer().getInventory().addItem(is);
                }
                String alias;
                if ((Main.getInstance().getConfig().get("generator." + tier + ".alias") != null)
                    && (!Main.getInstance().getConfig().getString("generator." + tier + ".alias").isEmpty())) {
                    alias = ColorUtil.translate(Main.getInstance().getConfig().getString("generator." + tier + ".alias"));
                } else alias = "";

                sender.sendMessage(Lang.GIVEN_GENERATOR.get()
                    .replace("%tier%", tier)
                    .replace("%player%", player.getPlayer().getName())
                    .replace("%alias%", alias)
                    .replace("%amount%", String.valueOf(amount)));
                if (player.getPlayer() != sender)
                    player.getPlayer().sendMessage(Lang.RECEIVED_GENERATOR.get()
                        .replace("%alias%", alias)
                        .replace("%amount%", String.valueOf(amount)).replace("%tier%", tier));
            }
        } else
            sender.sendMessage(Lang.NO_PERMISSION.get());
    }
}
