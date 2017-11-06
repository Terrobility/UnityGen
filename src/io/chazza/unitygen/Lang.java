package io.chazza.unitygen;

import io.chazza.unitygen.util.LoggerUtil;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Created by charliej on 13/05/2017.
 */
public enum Lang {
    NO_PERMISSION("&8[&cUnityGen&8] &7No permission to do that!"),
    NO_ARGS("&8[&cUnityGen&8] &7Invalid Arguments!"),
    RELOAD("&8[&cUnityGen&8] &7Reloaded Configuration!"),
    NO_PLAYER("&8[&cUnityGen&8] &7That player was not found!"),
    GIVEN_GENERATOR("&8[&cUnityGen&8] &7Given &c%player% &7%amount%x %tier%"),
    GIVEN_ALL_GENERATOR("&8[&cUnityGen&8] &7Given &4%online%x &7players, &b%amount%x &c%tier%&7!"),
    INVALID_GENERATOR("&8[&cUnityGen&8] &7Sorry, &c%tier% &7is invalid generator"),
    RECEIVED_GENERATOR("&8[&cUnityGen&8] &7Received &7%amount%x %tier% generator"),
    UNKNOWN_COMMAND("&8[&cUnityGen&8] &7That command was not found!"),
    PLACED_GENERATOR("&8[&cUnityGen&8] &7Placed a(n) &c%tier% &7generator"),
    REMOVED_GENERATOR("&8[&cUnityGen&8] &7Removed a(n) &c%tier% &7generator"),
    GENERATOR_NO_PERMISSION("&8[&cUnityGen&8] &7No permission to use a(n) &c%tier%"),
    BREAK_GENERATOR("&8[&cUnityGen&8] &7Shift + Right Click &7to obtain your generator"),
    OWNED_GENERATOR("&8[&cUnityGen&8] &7This generator belongs to &c%player%"),
    PLACE_LIMIT("&8[&cUnityGen&8] &7Generator Limit of &c%limit% &7reached"),
    GENERATOR_FULL("&8[&cUnityGen&8] &7Your &c&l%tier% &7generator is full!"),
    GENERATED("&4&lUNITY &c&l%tier% &8> &7Generated %amount%x %item%!"),
    ADMIN_REMOVED("&8[&cUnityGen&8] &7You removed &c%player%''s &7Generator!"),
    DISABLED_PLACE("&8[&cUnityGen&8] &7The &c%tier% &7generator you tried to place is disabled!"),
    DISABLED_JOIN("&8[&cUnityGen&8] &7Your &c%tier% &7generator has been disabled by an administrator!"),
    EXPIRED_GENERATOR("&8[&cUnityGen&8] &7Your &c%tier% &7generator has expired!"),
    EXPIRE_REMOVED("&8[&cUnityGen&8] &7You cannot remove this generator, as it isset to expire."),
    DOUBLE_CHEST("&8[&cUnityGen&8] &7Generators cannot be Double Chests."),

    GIVEN_FUEL("&8[&cUnityGen&8] &7Given %player% (%amount%x) %tier% fuel."),
    GIVEN_ALL_FUEL("&8[&cUnityGen&8] &7Given %online% online players, (%amount%x) %tier% fuel."),
    INVALID_FUEL("&8[&cUnityGen&8] &7Sorry, &c%tier% &7is an invalid fuel type."),
    RECEIVED_FUEL("&8[&cUnityGen&8] &7Received &7%amount%x %tier% fuel."),
    USED_FUEL("&8[&cUnityGen&8] &7Fuel Used! Set to expire in &c%expire%&7."),
    USED_FUEL_OTHER("&8[&cUnityGen&8] &7Fuel used for %owner%''s generator! Expires in &c%expire%&7."),
    CANNOT_USE_FUEL("&8[&cUnityGen&8] &7This generator will not expire."),
    NOT_APPLICABLE("&8[&cUnityGen&8] &7This fuel does not work on &c%tier% &7generators."),
    FUEL_DISABLED("&8[&cUnityGen&8] &7The &c%tier% &7fuel you tried to use is disabled!");

    private String def;

    private String fileName;
    private File configFile;
    private FileConfiguration config;
    private Main plugin;

    Lang(String def) {
        this.def = def;
        plugin = Main.getPlugin(Main.class);
        fileName = "messages.yml";
        configFile = new File(plugin.getDataFolder(), fileName);
        saveDefault();
        reload();
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    private void saveDefault() {
        if (!configFile.exists()) {
            plugin.saveResource(fileName, false);
        }
    }


    public String getKey() {
        return name().toLowerCase().replace("_", "-");
    }

    public String get() {
        String value = config.getString("message."+getKey(), def);
        if (value == null) {
            LoggerUtil.warnMsg("Message not found for " + getKey());
            LoggerUtil.warnMsg("Creating default value.");
            value = getKey();
            config.set("message."+getKey(), value);
            try {
                config.save(configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return value;
        }
        return ChatColor.translateAlternateColorCodes('&', value);
    }
}
