package io.chazza.unitygen.util;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import io.chazza.unitygen.Lang;
import io.chazza.unitygen.Main;
import io.chazza.unitygen.event.custom.UGCommandGenerateEvent;
import io.chazza.unitygen.event.custom.UGItemGenerateEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class GenObject {

    private String tier;
    private Material genMaterial;
    private String strLocation;
    private UUID ownerUUID;
    private Hologram hologram;
    private BukkitTask taskID;


    public GenObject(String tier, UUID ownerUUID){
        this.tier = tier;
        this.ownerUUID = ownerUUID;
    }
    public GenObject(String tier, UUID ownerUUID, Material genMaterial){
        this.tier = tier;
        this.ownerUUID = ownerUUID;
        this.genMaterial = genMaterial;
    }
    public GenObject(String tier, UUID ownerUUID, Material genMaterial, Hologram hologram){
        this.tier = tier;
        this.ownerUUID = ownerUUID;
        this.genMaterial = genMaterial;
        this.hologram = hologram;
    }

    public String getTier(){ return tier; }
    public GenObject getObject(){ return this; }
    public UUID getOwner(){ return ownerUUID; }
    public String getOwnerName(){return Bukkit.getOfflinePlayer(ownerUUID).getName();}
    public Material getGenMaterial(){ return genMaterial; }
    public Location getLocation(){ return LocationUtil.strToLocation(strLocation); }
    public String getStrLocation(){ return strLocation; }
    public Long getExpiration(){ return ConfigManager.getConfig(ownerUUID).getConfig().getLong("generator."+getStrLocation()+".expire"); }
    public void removeLocation(){
        ConfigManager cm = ConfigManager.getConfig(ownerUUID);
        FileConfiguration f = cm.getConfig();

        f.set("generator."+getStrLocation(), null);
        cm.saveConfig();
        cm.reload();
        Main.getInstance().getActiveGens().remove(ownerUUID, getTaskID());
        Main.getInstance().getActiveChunks().remove(getLocation());
    }
    public void setStrLocation(String strLocation){
        this.strLocation = strLocation;
    }
    public Hologram getHologram(){ return hologram; }
    public void setHologram(Hologram hologram){
        this.hologram = hologram;
        Main.getFileManager().addGenHologram(ownerUUID, hologram);
    }
    public void removeHologram(){
        Main.getFileManager().removeGenHologram(ownerUUID, hologram);
        this.hologram = null;
    }
    public Integer getHologramHeight(){
        return Main.getInstance().getConfig().getInt("general.hologramHeight");
    }

    public BukkitTask getTaskID(){
        return this.taskID;
    }
    public void startGeneration(){
        Player p = Bukkit.getPlayer(getOwner());

        Integer interval = Main.getInstance().getConfig().getInt("generator." + tier + ".time");
        taskID = new BukkitRunnable() {
            @Override
            public void run() {

                if(GenUtil.isLocAGen(getLocation())){
                    if(!p.isOnline()){
                        cancel();
                        if(Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays"))
                            removeHologram();
                        return;
                    }
                    // check
                    if(ConfigManager.getConfig(ownerUUID).getConfig().getLong("generator."+getStrLocation()+".expire") != -1) {
                        if (System.currentTimeMillis() >= getExpiration()) {
                            cancel();
                            removeLocation();
                            if(Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays"))
                                removeHologram();
                            p.sendMessage(Lang.EXPIRED_GENERATOR.get().replace("%tier%", tier));
                            return;
                        }
                    }
                    if(Arrays.asList(Material.CHEST, Material.TRAPPED_CHEST).contains(getLocation().getBlock().getType())){
                        for(String items : Main.getInstance().getConfig().getConfigurationSection("generator."+tier+".item").getKeys(false)){

                            FileConfiguration config = Main.getInstance().getConfig();

                            String type = config.getString("generator."+tier+".item."+items+".type").toUpperCase();

                            if(type.equalsIgnoreCase("ITEM")){
                                Integer nextRandom = ThreadLocalRandom.current().nextInt(100)+1;


                                Material item = Material.valueOf(config.getString("generator."+tier+".item."+items+".material"));
                                Integer itemData = config.getInt("generator."+tier+".item."+items+".data");
                                Integer itemAmount = config.getInt("generator."+tier+".item."+items+".amount");
                                Integer itemChance = config.getInt("generator."+tier+".item."+items+".chance");
                                String itemName = config.getString("generator."+tier+".item."+items+".name");


                                List<String> lore = ColorUtil.translate(Main.getInstance().getConfig().getStringList("generator."+tier+".item."+items+".lore"));

                                Short dataShort = Short.valueOf(itemData.toString());

                                ItemStack is = new ItemStack(item);
                                ItemMeta im = is.getItemMeta();
                                is.setAmount(itemAmount);
                                is.setDurability(dataShort);

                                if(itemName != null || !itemName.isEmpty()){
                                    im.setDisplayName(ColorUtil.translate(itemName));
                                }
                                if(lore != null || !lore.isEmpty()) im.setLore(lore);

                                is.setItemMeta(im);

                                if(nextRandom <= itemChance){
                                    Player p = Bukkit.getOfflinePlayer(UUID.fromString(ownerUUID.toString())).getPlayer();
                                    Chest chest = (Chest) getLocation().getBlock().getState();
                                    UGItemGenerateEvent generateEvent = new UGItemGenerateEvent(p, tier, is, getLocation(), chest.getInventory(), getObject());
                                    Bukkit.getPluginManager().callEvent(generateEvent);
                                }
                            }else if(type.equalsIgnoreCase("COMMAND")){
                                Integer nextRandom = ThreadLocalRandom.current().nextInt(100)+1;
                                Integer itemChance = config.getInt("generator."+tier+".item."+items+".chance");

                                if(nextRandom <= itemChance){
                                    for(String cmds : Main.getInstance().getConfig().getStringList("generator."+tier+".item."+items+".command")){
                                        UGCommandGenerateEvent commandGenerateEvent = new UGCommandGenerateEvent(p, tier, cmds);
                                        Bukkit.getPluginManager().callEvent(commandGenerateEvent);
                                    }

                                }
                            }
                        }
                    }else removeLocation();
                }else{
                    cancel();
                    removeLocation();
                    if(Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays"))
                        removeHologram();
                }
            }
        }.runTaskTimer(Main.getInstance(), interval*20, interval*20);

        Main.getInstance().getActiveGens().put(p.getUniqueId(), getTaskID());
        LoggerUtil.debugMsg("Bukkit ID " + getTaskID() + " has started to generate!");
        Main.getInstance().getActiveChunks().add(getLocation());
    }
}
