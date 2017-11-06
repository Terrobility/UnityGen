package io.chazza.unitygen;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import io.chazza.unitygen.util.ColorUtil;
import io.chazza.unitygen.util.LoggerUtil;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class FileManager {

    private File configFile;
    private File messageFile;
    private File hologramFile;
    private File fuelFile;
    private FileConfiguration config,message,hologram, fuel;

    public FileConfiguration getMsgFile(){
        return this.message;
    }
    public FileConfiguration getHoloFile(){
        return this.hologram;
    }
    public FileConfiguration getFuelFile(){
        return this.fuel;
    }

    public void saveMsgFile(){
        try {
            message.save(messageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getFormat(String format){
        return getMsgFile().getString("format."+format);
    }

    public boolean loadFiles(){
        config = new YamlConfiguration();
        message = new YamlConfiguration();
        hologram = new YamlConfiguration();
        fuel = new YamlConfiguration();
        try{
            config.load(configFile);
            message.load(messageFile);
            hologram.load(hologramFile);
            fuel.load(fuelFile);
            return true;
        }catch (IOException | InvalidConfigurationException e) {
            LoggerUtil.warnMsg("Error, "+e.getLocalizedMessage()+".");
            return false;
        }
    }
    void createFiles() throws IOException{
        File pluginFolder = Main.getInstance().getDataFolder();
        configFile = new File(pluginFolder, "config.yml");
        messageFile = new File(pluginFolder, "messages.yml");
        hologramFile = new File(pluginFolder, "holograms.yml");
        fuelFile = new File(pluginFolder, "fuel.yml");

        for(File f : Arrays.asList(configFile, messageFile, hologramFile, fuelFile)){
            if(!f.exists()){
                f.getParentFile().mkdirs();
                Main.getInstance().saveResource(f.getName(), false);
                if(!f.getName().equals("holograms.yml")){
                    LoggerUtil.logMsg("Couldn't find %file%, Creating!".replace("%file%", f.getName()));
                }
            }
        }

        config = new YamlConfiguration();
        message = new YamlConfiguration();
        hologram = new YamlConfiguration();
        fuel = new YamlConfiguration();
        try{
            config.load(configFile);
            message.load(messageFile);
            hologram.load(hologramFile);
            fuel.load(fuelFile);
        }catch (IOException | InvalidConfigurationException e) {
            LoggerUtil.warnMsg("Error, "+e.getLocalizedMessage()+".");
        }
    }

    void removeHologramFile(){ if(hologramFile.exists()) hologramFile.delete();}
    public void addGenHologram(UUID uuid, Hologram hologram){
        List<String> userHolograms = getHoloFile().getStringList("user-hologram."+uuid);
        userHolograms.add(hologram.getLocation().toString());
        getHoloFile().set("user-hologram."+uuid, userHolograms);
    }
    public void removeGenHologram(UUID uuid, Hologram hologram){
        List<String> userHolograms = getHoloFile().getStringList("user-hologram."+uuid);
        userHolograms.remove(hologram.getLocation().toString());
        getHoloFile().set("user-hologram."+uuid, userHolograms);
        for (Hologram holos : HologramsAPI.getHolograms(Main.getInstance())){
            if(holos.getLocation().equals(hologram.getLocation())){
                holos.delete();
            }
        }
    }
    public List<String> getUserHolograms(UUID UUID){
        List<String> userHolos = getHoloFile().getStringList("user-hologram."+UUID);
        if(userHolos != null){
            return userHolos;
        }
        return null;        
    }
    public boolean doesUserContainHologram(UUID UUID, String HOLO) {
        List<String> userHolos = getHoloFile().getStringList("user-hologram." + UUID);
        if (userHolos.contains(HOLO)) {
            return true;
        }
        return false;
    }

}
