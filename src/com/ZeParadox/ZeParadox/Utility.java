package com.ZeParadox.ZeParadox;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

class Utility {

    String tag = "&8[&bZeParadox&8] &b";

    private final Main main;
    Utility(Main main) {
        this.main = main;
    }

    String colorString(String Input){
        String s = ChatColor.translateAlternateColorCodes('&', Input);
        return s;
    }

    void log(String info){
        main.getServer().getConsoleSender().sendMessage(colorString(tag + info));
    }

    ItemStack createItem(Material m, Integer i, Short d, String n, List<String> a, Enchantment e) {
        ItemStack is = new ItemStack(m, i, d);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(colorString(n));
        im.setLore(colorList(a));
        if (e != null) {
            im.addEnchant(e, 1, true);
        }
        is.setItemMeta(im);
        return is;
    }

    private List<String> colorList(List<String> a) {
        List<String> l = new ArrayList<>();
        for (String s : a) {
            l.add(colorString(s));
        }
        return l;
    }

    void say(String input){
        Bukkit.broadcastMessage(colorString(tag + input));
    }

    void customParticle(World w, double x, double y, double z, double r, double g, double b, double a){
        w.spawnParticle(Particle.REDSTONE, x, y, z, 0, r, g, b, a, Particle.REDSTONE.getDataType());
    }


    void setLocation(Location theLocation, String thePath){
        double x = theLocation.getX();
        double y = theLocation.getY();
        double z = theLocation.getZ();
        float yaw = theLocation.getYaw();
        float pitch = theLocation.getPitch();
        World world = theLocation.getWorld();
        String w = world.getName();
        main.getConfig().set(thePath + ".x", x);
        main.getConfig().set(thePath + ".y", y);
        main.getConfig().set(thePath + ".z", z);
        main.getConfig().set(thePath + ".world", w);
        main.getConfig().set(thePath + ".yaw", yaw);
        main.getConfig().set(thePath + ".pitch", pitch);
        main.saveConfig();
    }


    void removeLocation(String thePath){
        main.getConfig().set(thePath, null);
        main.saveConfig();
    }


    Location getLocation(String thePath){
        Location l = null;
        try{
            if(main.getConfig().get(thePath) != null){
                double x = main.getConfig().getDouble(thePath + ".x");
                double y = main.getConfig().getDouble(thePath + ".y");
                double z = main.getConfig().getDouble(thePath + ".z");
                World w = Bukkit.getWorld(main.getConfig().getString(thePath + ".world"));
                l = new Location(w, x, y, z);
            }
        }catch (Exception err){
            log("get Location err: " + err.getMessage());
        }
        return l;
    }

}
