package io.chazza.unitygen.hook;

import io.chazza.unitygen.Main;
import io.chazza.unitygen.UnityGenAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class MVdWPlaceholderAPIEvent {

    public void hook(Main plugin) {
        be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(plugin, "unitygen_usergens", event -> {
            Player player = event.getPlayer();
            OfflinePlayer offlinePlayer = event.getPlayer();

            if (offlinePlayer == null) {
                return "Player not found!";
            }

            return String.valueOf(UnityGenAPI.getPlayerGens(player.getUniqueId()).size());
        });
        be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(plugin, "unitygen_userlimit", event -> {
            Player player = event.getPlayer();
            OfflinePlayer offlinePlayer = event.getPlayer();

            if (offlinePlayer == null) {
                return "Player not found!";
            }

            return String.valueOf(UnityGenAPI.getPlayerLimit(player));
        });
    }
}
