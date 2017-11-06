package io.chazza.unitygen.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import io.chazza.unitygen.Lang;
import io.chazza.unitygen.Main;
import io.chazza.unitygen.util.CenterUtil;
import io.chazza.unitygen.util.ColorUtil;
import mkremins.fanciful.FancyMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by charliej on 19/05/2017.
 */

public class MainCommand extends BaseCommand {

    @CommandAlias("%cmd")
    public void onCommand(CommandSender sender) {
        sender.sendMessage("§4[§cUnityGen§4] §7Running §cv%version% §7by §cfiver.io§7."
            .replace("%version%", Main.getInstance().getDescription().getVersion()));
        sender.sendMessage("§4[§cUnityGen§4] §7Developer: §cChazmondo§7.");
        sender.sendMessage("§4[§cUnityGen§4] §7Use §c/gen help §7for commands.");
    }
}
