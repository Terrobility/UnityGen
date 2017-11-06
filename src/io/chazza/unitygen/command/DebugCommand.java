package io.chazza.unitygen.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Subcommand;
import io.chazza.unitygen.Lang;
import io.chazza.unitygen.Main;
import io.chazza.unitygen.util.ColorUtil;
import org.bukkit.command.CommandSender;

@CommandAlias("%cmd")
public class DebugCommand extends BaseCommand {

    @Subcommand("debug|d")
    @CommandCompletion("debug|d")
    public void onVersion(CommandSender sender) {
        if (sender.hasPermission("unitygen.debug")) {
            if(Main.getInstance().DEBUG) {
                sender.sendMessage(ColorUtil.translate("&8[&cUnityGen&8] &4Disabled &7Debugging."));
                Main.getInstance().DEBUG = false;
            }else{
                sender.sendMessage(ColorUtil.translate("&8[&cUnityGen&8] &2Enabled &7Debugging."));
                Main.getInstance().DEBUG = true;
            }
        }else sender.sendMessage(Lang.NO_PERMISSION.get());
    }
}
