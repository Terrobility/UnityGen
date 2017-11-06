package io.chazza.unitygen.hook;

import io.chazza.unitygen.Main;
import io.chazza.unitygen.UnityGenAPI;
import me.clip.placeholderapi.external.EZPlaceholderHook;
import org.bukkit.entity.Player;

public class ClipPlaceholderAPIEvent extends EZPlaceholderHook {

    @SuppressWarnings("unused")
    private Main UnityGen;

    public ClipPlaceholderAPIEvent(Main UnityGen) {
        // this is the plugin that is registering the placeholder and the identifier for our placeholder.
        // the format for placeholders is this:
        // %<placeholder identifier>_<anything you define as an identifier in your method below>%
        // the placeholder identifier can be anything you want as long as it is not already taken by another
        // registered placeholder.
        super(UnityGen, "unitygen");
        // this is so we can access our main class below
        this.UnityGen = UnityGen;
    }

    @Override
    public String onPlaceholderRequest(Player p, String identifier) {
        if (identifier.equals("userlimit")) {
            return String.valueOf(UnityGenAPI.getPlayerLimit(p));
        }
        // always check if the player is null for placeholders related to the player!
        if (p == null) {
            return "";
        }
        // placeholder: %customplaceholder_is_staff%
        if (identifier.equals("usergens")) {
            return String.valueOf(UnityGenAPI.getPlayerGens(p.getUniqueId()).size());
        }
        // anything else someone types is invalid because we never defined %customplaceholder_<what they want a value for>%
        // we can just return null so the placeholder they specified is not replaced.
        return null;
    }
    
}
