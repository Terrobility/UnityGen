package io.chazza.unitygen.command.generator;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import io.chazza.unitygen.Lang;
import io.chazza.unitygen.Main;
import io.chazza.unitygen.util.ColorUtil;
import io.chazza.unitygen.util.InventoryUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@CommandAlias("%cmd")
public class GenGUICommand extends BaseCommand {

    Main plugin = Main.getInstance();

    @Subcommand("gui|menu")
    public void onGUI(Player p) {
        if (p.hasPermission("unitygen.gui")) {
            Main.getFileManager().loadFiles();
            String guiName = ColorUtil.translate(Main.getFileManager().getMsgFile().getString("gui-message.view.gui-name"));
            Integer size = Main.getFileManager().getMsgFile().getInt("gui-message.view.size");
            InventoryUtil inv = new InventoryUtil(p, guiName, size);

            for (String gens : plugin.getConfig().getConfigurationSection("generator").getKeys(false)) {
                boolean status = plugin.getConfig().getBoolean("generator." + gens + ".enabled");

                Material mat = Material.valueOf(plugin.getConfig().getString("generator." + gens + ".block").toUpperCase());

                ItemStack is = new ItemStack(mat, 1);
                ItemMeta im = is.getItemMeta();

                Integer interval = plugin.getConfig().getInt("generator." + gens + ".time");
                List<String> lore = new ArrayList<>();
                List<String> genLore = ColorUtil.translate(plugin.getConfig().getStringList("generator." + gens + ".lore"));
                List<String> interfaceLore = ColorUtil.translate(Main.getFileManager().getMsgFile().getStringList("gui-message.view.interface-lore"));

                for (String newLore : interfaceLore) {
                    if (newLore.equals("%gen-lore%")) {
                        for (String loreList : genLore) {
                            lore.add(loreList.replace("%time%", interval.toString()).replace("%tier%", gens));
                        }
                    } else {
                        lore.add(newLore.replace("%tier%", gens).replace("%status%", String.valueOf(status)));
                    }
                }

                im.setDisplayName(ColorUtil.translate(plugin.getConfig().getString("generator." + gens + ".name").replace("%tier%", gens)));
                im.setLore(lore);
                is.setItemMeta(im);

                inv.addItem(is);
            }
            p.openInventory(inv.getInventory());
        } else
            p.sendMessage(Lang.NO_PERMISSION.get());
    }
}
