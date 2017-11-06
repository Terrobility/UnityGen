package io.chazza.unitygen.command.generator;

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

@CommandAlias("%cmd")
public class GenListCommand extends BaseCommand {

    String getGenerators(){
        Iterable<String> generators = Main.getInstance().getConfig().getConfigurationSection("generator").getKeys(false);
        return Joiner.on(ChatColor.translateAlternateColorCodes('&', "&f, &c")).join(generators);
    }
    @Subcommand("list|l")
    public void onVersion(CommandSender sender) {
        if (sender.hasPermission("unitygen.list")) {
            if(sender instanceof Player) {
                Player p = (Player) sender;
                sender.sendMessage("");
                CenterUtil.sendCenteredMessage(p, "&4-&c-&4-&c-&8[ &4&lAvailable Generators &8]&c-&4-&c-&4-");
                sender.sendMessage("");
                CenterUtil.sendCenteredMessage(p, ColorUtil.translate("&c") + getGenerators());
                sender.sendMessage("");
            }else{
                sender.sendMessage(ColorUtil.translate("&4-&c-&4-&c-&8[ &4&lAvailable Generators &8]&c-&4-&c-&4-"));
                sender.sendMessage(ColorUtil.translate(""));
                sender.sendMessage(ColorUtil.translate("&c" + getGenerators()));
                sender.sendMessage(ColorUtil.translate(""));
            }
        }else sender.sendMessage(Lang.NO_PERMISSION.get());
    }
}
