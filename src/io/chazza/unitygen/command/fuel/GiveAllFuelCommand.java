package io.chazza.unitygen.command.fuel;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import io.chazza.unitygen.Lang;
import io.chazza.unitygen.Main;
import io.chazza.unitygen.util.ColorUtil;
import io.chazza.unitygen.util.FuelUtil;
import io.chazza.unitygen.util.GenUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandAlias("%cmd")
public class GiveAllFuelCommand extends BaseCommand {

    @Subcommand("giveallfuel")
    public void onVersion(CommandSender sender, String tier, @Optional Integer amount) {

        if (sender.hasPermission("unitygen.giveall")) {
            if (FuelUtil.getCaseGenName(tier) == null) {
                sender.sendMessage(Lang.INVALID_FUEL.get().replace("%tier%", tier));
                return;
            } else {
                tier = FuelUtil.getCaseGenName(tier);
                if(amount == null) amount = 1;
                ItemStack is = FuelUtil.createFuel(tier, amount);

                for(Player p : Bukkit.getOnlinePlayers()){
                    if (p.getInventory().firstEmpty() <= -1) {
                        p.getWorld().dropItem(p.getLocation().add(0, 1, 0), is);
                    } else {
                        p.getInventory().addItem(is);
                    }
                }

                sender.sendMessage(Lang.GIVEN_ALL_FUEL.get()
                    .replace("%tier%", tier)
                    .replace("%online%", String.valueOf(Bukkit.getOnlinePlayers().size()))
                    .replace("%amount%", String.valueOf(amount)));
                for(Player p : Bukkit.getOnlinePlayers()){
                    if(p != sender) {
                        p.sendMessage(Lang.RECEIVED_FUEL.get()
                            .replace("%amount%", String.valueOf(amount)).replace("%tier%", tier));
                    }
                }
            }
        }else
            sender.sendMessage(Lang.NO_PERMISSION.get());
    }
}
