package io.chazza.unitygen.command.generator;

import co.aikar.commands.BaseCommand;
import io.chazza.unitygen.Lang;
import io.chazza.unitygen.Main;
import io.chazza.unitygen.util.ColorUtil;
import io.chazza.unitygen.util.GenUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;

@CommandAlias("%cmd")
public class GiveAllGenCommand extends BaseCommand {

    @Subcommand("giveall")
    public void onVersion(CommandSender sender, String tier, @Optional Integer amount) {
        if (sender.hasPermission("unitygen.giveall")) {
            if (GenUtil.getCaseGenName(tier) == null) {
                sender.sendMessage(Lang.INVALID_GENERATOR.get().replace("%tier%", tier));
                return;
            } else {
                tier = GenUtil.getCaseGenName(tier);
                if(amount == null) amount = 1;
                ItemStack is = GenUtil.createGeneratorIS(tier, amount);

                for(Player p : Bukkit.getOnlinePlayers()){
                    if (p.getInventory().firstEmpty() <= -1) {
                        p.getWorld().dropItem(p.getLocation().add(0, 1, 0), is);
                    } else {
                        p.getInventory().addItem(is);
                    }
                }

                String alias;
                if((Main.getInstance().getConfig().get("generator."+tier+".alias") != null)
                    && (!Main.getInstance().getConfig().getString("generator."+tier+".alias").isEmpty())){
                    alias = ColorUtil.translate(Main.getInstance().getConfig().getString("generator."+tier+".alias"));
                }else alias = "";

                sender.sendMessage(Lang.GIVEN_ALL_GENERATOR.get()
                    .replace("%alias%", alias)
                    .replace("%tier%", tier)
                    .replace("%online%", String.valueOf(Bukkit.getOnlinePlayers().size()))
                    .replace("%amount%", String.valueOf(amount)));
                for(Player p : Bukkit.getOnlinePlayers()){
                    if(p != sender) {
                        p.sendMessage(Lang.RECEIVED_GENERATOR.get().replace("%alias%", alias)
                            .replace("%amount%", String.valueOf(amount)).replace("%tier%", tier));
                    }
                }
            }
        }else
            sender.sendMessage(Lang.NO_PERMISSION.get());
    }
}
