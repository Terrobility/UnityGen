package io.chazza.unitygen.command.help;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import io.chazza.unitygen.Lang;
import io.chazza.unitygen.util.CenterUtil;
import io.chazza.unitygen.util.ColorUtil;
import mkremins.fanciful.FancyMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by charliej on 19/05/2017.
 */
@CommandAlias("%cmd")
public class HelpPage1Command extends BaseCommand {

    public void tellCommand(Player p, String cmd, String info, String permission){
        new FancyMessage(ColorUtil.translate("&4» &c/gen "+cmd))
            .tooltip(ColorUtil.translate("&4» &c/gen "+cmd), ColorUtil.translate("&7Info: &f"+info), ColorUtil.translate("&7Perm: &f"+permission))
            .suggest("/gen " + cmd)
            .send(p);
    }

    @Subcommand("help|h")
    public void onCommand(CommandSender sender) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(p.hasPermission("unitygen.help")){
                CenterUtil.sendCenteredMessage(p, "&4-&c-&4-&c-&8[ &4&lUnityGen &8]&c-&4-&c-&4-");
                CenterUtil.sendCenteredMessage(p, "&7Hover for more information!");
                CenterUtil.sendCenteredMessage(p, "");

                tellCommand(p, "give <player> <tier> [amount]", "Give a generator","unitygen.give");
                tellCommand(p, "giveall <tier> [amount]", "Give everyone a generator","unitygen.giveall");
                tellCommand(p, "list", "List generator types","unitygen.list");
                tellCommand(p, "gui", "View Generators","unitygen.gui");
                tellCommand(p, "reload", "Reload configuration","unitygen.reload");
                tellCommand(p, "debug", "Toggle Debugging","unitygen.debug");
                CenterUtil.sendCenteredMessage(p, "");
                CenterUtil.sendCenteredMessage(p, "&7Page (1/2) &c/gen help 2");
                CenterUtil.sendCenteredMessage(p, "");
            }else sender.sendMessage(Lang.NO_PERMISSION.get());
        }else{
            sender.sendMessage(ColorUtil.translate("&4-&c-&4-&c-&8[ &f&lUnityGen &8]&c-&4-&c-&4-"));
            sender.sendMessage(ColorUtil.translate("&7Available Commands!"));
            sender.sendMessage(ColorUtil.translate(""));
            sender.sendMessage(ColorUtil.translate("&c/gen give <player> <tier> [amount]"));
            sender.sendMessage(ColorUtil.translate("&c/gen giveall <tier> [amount]"));
            sender.sendMessage(ColorUtil.translate("&c/gen list"));
            sender.sendMessage(ColorUtil.translate("&c/gen gui"));
            sender.sendMessage(ColorUtil.translate("&c/gen reload"));
            sender.sendMessage(ColorUtil.translate("&c/gen debug"));
            sender.sendMessage(ColorUtil.translate(""));
            sender.sendMessage(ColorUtil.translate("&7Page (1/2) &c/gen help 2"));
            sender.sendMessage(ColorUtil.translate(""));
        }
    }
}
