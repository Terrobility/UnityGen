package io.chazza.unitygen.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Subcommand;
import io.chazza.unitygen.Lang;
import io.chazza.unitygen.Main;
import org.bukkit.command.CommandSender;


@CommandAlias("%cmd")
public class ReloadCommand extends BaseCommand {

    @Subcommand("reload|r")
    @CommandCompletion("reload|r")
    public void onReload(CommandSender sender) {
        if(sender.hasPermission("unitygen.reload")){
            sender.sendMessage(Lang.RELOAD.get());

            Main.getInstance().reloadConfig();
            Main.getFileManager().loadFiles();
        }else sender.sendMessage(Lang.NO_PERMISSION.get());
    }
}
