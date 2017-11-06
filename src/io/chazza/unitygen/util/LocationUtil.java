package io.chazza.unitygen.util;

import io.chazza.unitygen.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.UUID;

public class LocationUtil {

    /**
     * Converts Location to String Eg, World,x,y,z
     *
     * @param loc
     * @return
     */
    public static String strToStrLocation(Location loc) {
        if (loc == null)
            return "";
        return loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
    }

    /**
     * Converts String to Location Eg, World,x,y,z,tier to Location
     *
     * @param loc
     * @return
     */
    public static Location strToLocation(String loc) {
        if (loc == null)
            return null;
        String[] splitStr = loc.split(",");
        World world = Bukkit.getServer().getWorld(splitStr[0]);
        Integer x = Integer.parseInt(splitStr[1]);
        Integer y = Integer.parseInt(splitStr[2]);
        Integer z = Integer.parseInt(splitStr[3]);
        return new Location(world, x, y, z);
    }


    /**
     * Fetches tier of block at a Location
     * @param loc
     * @param uuid
     * @return
     */
    public static String tierFromLocation(Location loc, UUID uuid) {
        File userFile = new File(Main.getInstance().getDataFolder() + File.separator + "user-data", uuid + ".yml");

            YamlConfiguration yc = YamlConfiguration.loadConfiguration(userFile);
            if(yc.getString("generator."+strToStrLocation(loc)+".tier") != null){
                return yc.getString("generator."+strToStrLocation(loc)+".tier");

        }
        return null;
    }

    public static boolean isHopperNear(Location loc){
        Material bpos1 = loc.add(-1, 0, 0).getBlock().getType();
        Material bpos2 = loc.add(+1, 0, 0).getBlock().getType();
        Material bpos3 = loc.add(0, 0, -1).getBlock().getType();
        Material bpos4 = loc.add(0, 0, +1).getBlock().getType();
        Material bpos5 = loc.add(0, 1, 0).getBlock().getType();
        Material bpos6 = loc.add(0, -1, 0).getBlock().getType();

        if(bpos1.equals(Material.HOPPER)) return true;
        if(bpos2.equals(Material.HOPPER)) return true;
        if(bpos3.equals(Material.HOPPER)) return true;
        if(bpos4.equals(Material.HOPPER)) return true;
        if(bpos5.equals(Material.HOPPER)) return true;
        if(bpos6.equals(Material.HOPPER)) return true;
        return false;
    }

    public static boolean isGenNearby(Location loc){
        Block bpos1 = loc.add(-1, 0, 0).getBlock();
        Block bpos2 = loc.add(+1, 0, 0).getBlock();
        Block bpos3 = loc.add(0, 0, -1).getBlock();
        Block bpos4 = loc.add(0, 0, +1).getBlock();
        Block bpos5 = loc.add(0, 1, 0).getBlock();
        Block bpos6 = loc.add(0, -1, 0).getBlock();

        if(GenUtil.isLocAGen(bpos1.getLocation())) return true;
        if(GenUtil.isLocAGen(bpos2.getLocation())) return true;
        if(GenUtil.isLocAGen(bpos3.getLocation())) return true;
        if(GenUtil.isLocAGen(bpos4.getLocation())) return true;
        if(GenUtil.isLocAGen(bpos5.getLocation())) return true;
        if(GenUtil.isLocAGen(bpos6.getLocation())) return true;
        return false;
    }
}
