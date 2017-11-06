package io.chazza.unitygen.util;

import io.chazza.unitygen.Main;
import org.bukkit.Bukkit;

import java.util.logging.Level;

public class LoggerUtil {

    public static void logMsg(String msg) {
        Bukkit.getLogger().log(Level.INFO, "[UnityGen] " + msg);
    }
    public static void debugMsg(String msg) {
        if(Main.getInstance().DEBUG) {
            Bukkit.getLogger().log(Level.INFO, "[DEBUG] [UnityGen] " + msg);
        }
    }
    public static void warnMsg(String msg) {
        Bukkit.getLogger().log(Level.WARNING, "[UnityGen] " + msg);
    }
    
    

}
