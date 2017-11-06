package io.chazza.unitygen.command.fuel;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import com.google.common.base.Joiner;
import io.chazza.unitygen.Lang;
import io.chazza.unitygen.Main;
import io.chazza.unitygen.util.CenterUtil;
import io.chazza.unitygen.util.ColorUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("%cmd") @Subcommand("list|fl")
public class FuelListCommand extends BaseCommand {

    String getFuels(){
        Iterable<String> fuels = Main.getFileManager().getFuelFile().getConfigurationSection("fuel").getKeys(false);
        return Joiner.on(ChatColor.translateAlternateColorCodes('&', "&f, &c")).join(fuels);
    }
    @Subcommand("fuel")
    public void onVersion(CommandSender sender) {
        if (sender.hasPermission("unitygen.list")) {
            if(sender instanceof Player) {
                Player p = (Player) sender;
                sender.sendMessage("");
                CenterUtil.sendCenteredMessage(p, "&4-&c-&4-&c-&8[ &4&lAvailable Fuels &8]&c-&4-&c-&4-");
                sender.sendMessage("");
                CenterUtil.sendCenteredMessage(p, ColorUtil.translate("&c") + getFuels());
                sender.sendMessage("");
            }else{
                sender.sendMessage(ColorUtil.translate("&4-&c-&4-&c-&8[ &4&lAvailable Fuels &8]&c-&4-&c-&4-"));
                sender.sendMessage(ColorUtil.translate(""));
                sender.sendMessage(ColorUtil.translate("&c" + getFuels()));
                sender.sendMessage(ColorUtil.translate(""));
            }
        }else sender.sendMessage(Lang.NO_PERMISSION.get());
    }
}
