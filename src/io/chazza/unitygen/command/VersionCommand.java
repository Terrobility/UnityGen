package io.chazza.unitygen.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Subcommand;
import io.chazza.unitygen.Lang;
import io.chazza.unitygen.Main;
import io.chazza.unitygen.util.CenterUtil;
import io.chazza.unitygen.util.ColorUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("%cmd")
public class VersionCommand extends BaseCommand {

    @Subcommand("version|v")
    @CommandCompletion("version|v")
    public void onVersion(CommandSender sender) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(p.hasPermission("unitygen.version")){
                CenterUtil.sendCenteredMessage(p, "&4-&c-&4-&c-&8[ &4&lUnityGen &8]&c-&4-&c-&4-");
                CenterUtil.sendCenteredMessage(p, "");
                CenterUtil.sendCenteredMessage(p, "&7Running v%version%"
                    .replace("%version%", Main.getInstance().getDescription().getVersion()));
                CenterUtil.sendCenteredMessage(p, "");
            }else sender.sendMessage(Lang.NO_PERMISSION.get());
        }else{
            sender.sendMessage(ColorUtil.translate("&4-&c-&4-&c-&8[ &f&lUnityGen &8]&c-&4-&c-&4-"));
            sender.sendMessage(ColorUtil.translate(""));
            sender.sendMessage(ColorUtil.translate(""));
            sender.sendMessage(ColorUtil.translate("&7Running v%version%"
                .replace("%version%", Main.getInstance().getDescription().getVersion())));
            sender.sendMessage(ColorUtil.translate(""));
        }
    }



}
