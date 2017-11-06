package io.chazza.unitygen;

import co.aikar.commands.ACF;
import co.aikar.commands.CommandManager;
import co.aikar.commands.CommandReplacements;
import com.stringer.annotations.HideAccess;
import com.stringer.annotations.StringEncryption;
import io.chazza.unitygen.command.*;
import io.chazza.unitygen.command.fuel.FuelListCommand;
import io.chazza.unitygen.command.fuel.GiveAllFuelCommand;
import io.chazza.unitygen.command.fuel.GiveFuelCommand;
import io.chazza.unitygen.command.generator.GenGUICommand;
import io.chazza.unitygen.command.generator.GiveAllGenCommand;
import io.chazza.unitygen.command.generator.GiveGenCommand;
import io.chazza.unitygen.command.generator.GenListCommand;
import io.chazza.unitygen.command.help.HelpPage1Command;
import io.chazza.unitygen.command.help.HelpPage2Command;
import io.chazza.unitygen.event.*;
import io.chazza.unitygen.hook.ClipPlaceholderAPIEvent;
import io.chazza.unitygen.hook.MVdWPlaceholderAPIEvent;
import io.chazza.unitygen.hook.QAPluginHook;
import io.chazza.unitygen.util.*;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


@StringEncryption @HideAccess
public class Main extends JavaPlugin {
    private static Main instance;

    private List<Location> activeChunks = new ArrayList();

    public List<Location> getActiveChunks() {
        return activeChunks;
    }

    public static Main getInstance() {
        return instance;
    }

    private static UnityGenAPI ugAPI = null;

    public static UnityGenAPI getAPI() {
        return ugAPI;
    }

    private static FileManager fManager = null;

    public static FileManager getFileManager() {return fManager;}

    private HashMap<UUID, BukkitTask> activeGens = new HashMap<>();
    public HashMap<UUID, BukkitTask> getActiveGens() {
        return activeGens;
    }

//    private void BlacklistCheck(){ AntiLeakUtil.auth(); }

    public boolean DEBUG = false;

    @StringEncryption
    String cmds = "unitygen|ug|gen|generator|ugen";

    private void defineAliases(CommandReplacements cr){
        StringBuilder sb = new StringBuilder();
        sb.append(cmds);
        if(getConfig().getStringList("aliases") != null){
            sb.append("|");
            int listSize = getConfig().getStringList("aliases").size();
            int size = 0;
            for(String alias : getConfig().getStringList("aliases")){
                size++;
                LoggerUtil.logMsg("Registered alias /"+alias+".");
                if(size == listSize){
                    sb.append(alias);
                }else sb.append(alias+"|");

            }
            cr.addReplacement("cmd", sb.toString());
        }
    }

    private void registerCommands() {
        CommandManager manager = ACF.createManager(this);

        defineAliases(manager.getCommandReplacements());

        manager.registerCommand(new MainCommand());
        manager.registerCommand(new ReloadCommand());
        manager.registerCommand(new VersionCommand());
        manager.registerCommand(new DebugCommand());

        manager.registerCommand(new GiveGenCommand());
        manager.registerCommand(new GiveAllGenCommand());
        manager.registerCommand(new GenGUICommand());
        manager.registerCommand(new GenListCommand());

        manager.registerCommand(new GiveFuelCommand());
        manager.registerCommand(new GiveAllFuelCommand());
        manager.registerCommand(new FuelListCommand());

        manager.registerCommand(new HelpPage1Command());
        manager.registerCommand(new HelpPage2Command());
    }

    private void registerListeners() {
        new GeneratorPlaceEvent(this);
        new GeneratorBreakEvent(this);
        new GeneratorInteractEvent(this);
        new PlayerLeaveEvent(this);
        new PlayerEnterEvent(this);
        new GUIClickEvent(this);
        new HopperEvents(this);
        new ChunkEvent(this);
        new ExplodeEvent(this);
        new GenerateEvent(this);
        new GeneratorFuelEvent(this);
    }

    private void registerHooks() {
        if(Bukkit.getPluginManager().getPlugin("QAPlugin") != null){
            new QAPluginHook();
            LoggerUtil.logMsg("Successfully Hooked into QAPlugin.");
        }
    }


    @Override
    public void onEnable() {

        ugAPI = new UnityGenAPI();
        fManager = new FileManager();

        instance = this;
        DEBUG = false;

        registerCommands();
        registerListeners();
        registerHooks();

        this.saveDefaultConfig();
        new MetricUtil(this);
        LogUtil.start();

        try {
            getFileManager().createFiles();
           // BlacklistCheck();
        } catch (IOException e) {
            LoggerUtil.warnMsg(e.getLocalizedMessage());
        }
        PluginManager pm = Bukkit.getPluginManager();

        if (pm.getPlugin("PlaceholderAPI") != null) {
            LoggerUtil.logMsg("Successfully Hooked into PlaceholderAPI.");
            new ClipPlaceholderAPIEvent(this).hook();
        }

        if (pm.getPlugin("MVdWPlaceholderAPI") != null) {
            LoggerUtil.logMsg("Successfully Hooked into MVdWPlaceholderAPI.");
            new MVdWPlaceholderAPIEvent().hook(this);
        }

        File dataFolder = new File(getDataFolder() + File.separator + "user-data");

        ConfigManager cm;
        List<String> gens = new ArrayList<>();
        String name;

        if(!dataFolder.exists()) return;

        for(File f : dataFolder.listFiles()){
            if(!f.getName().contains(".yml")) return;
            name = f.getName().replace(".yml", "");
            cm = ConfigManager.getConfig(UUID.fromString(name));
            for(String gen : cm.getConfig().getStringList("generator")) {
                gens.add(gen);
            }
            cm.getConfig().set("generator", null);

            for(String gen : gens) {
                String[] str = gen.split(",");
                gen = str[0] + "," + str[1] + "," + str[2] + "," + str[3];
                cm.getConfig().set("generator." + gen + ".tier", str[4]);
                cm.getConfig().set("generator." + gen + ".expire", -1);
                cm.saveConfig();
            }

        }
    }
    @Override
    public void onDisable() {
        //ConfigManager.saveAll();
        getFileManager().removeHologramFile();
        instance = null;
        fManager = null;
        ugAPI = null;
    }
}
