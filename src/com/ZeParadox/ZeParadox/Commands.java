package com.ZeParadox.ZeParadox;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TropicalFish;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Commands implements CommandExecutor {
    private Main main;
    private Utility utility;
    private Guns guns;
    private Cars cars;
    private Eco eco;

    Commands(Main main) {
        this.main = main;
        utility = new Utility(this.main);
        guns = new Guns(this.main);
        cars = new Cars(this.main);
        eco = new Eco(this.main);
    }

    Random r = new Random();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(command.getName().equalsIgnoreCase("CommandSpy")){
            if(commandSender.hasPermission("ZeParadox.CommandSpy")){
                if(commandSender instanceof Player){
                    Player p = (Player) commandSender;
                    if(main.getConfig().getBoolean("players." + p.getUniqueId().toString() + ".CommandSpy")){
                        main.getConfig().set("players." + p.getUniqueId().toString() + ".CommandSpy", false);
                        p.sendMessage(utility.colorString(utility.tag+"Command spy deactivate!"));
                    }else{
                        main.getConfig().set("players." + p.getUniqueId().toString() + ".CommandSpy", true);
                        p.sendMessage(utility.colorString(utility.tag+"Command spy active!"));
                    }
                    main.saveConfig();
                }
            }
            return true;
        }

        if(command.getName().equalsIgnoreCase("fish")){
            if(commandSender instanceof Player){
                Player p = (Player) commandSender;
                if(p.hasPermission("ZeParadox.fish")){
                    TropicalFish tf = (TropicalFish) p.getWorld().spawnEntity(p.getLocation(), EntityType.TROPICAL_FISH);
                    TropicalFish.Pattern tfp = TropicalFish.Pattern.values()[r.nextInt(TropicalFish.Pattern.values().length)];
                    tf.setPattern(tfp);
                    tf.setCustomNameVisible(true);
                    tf.setCustomName(utility.colorString("&6Pam"));
                    Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> {
                        DyeColor dc = DyeColor.values()[r.nextInt(DyeColor.values().length)];
                        tf.setBodyColor(dc);
                        dc = DyeColor.values()[r.nextInt(DyeColor.values().length)];
                        tf.setPatternColor(dc);
                    }, 10L, 10L);
                }
            }
            return true;
        }

        if(command.getName().equalsIgnoreCase("card")){
            if(commandSender instanceof Player){
                Player p = (Player) commandSender;
                if(p.hasPermission("ZeParadox.card")){
                    eco.giveCard(p);
                }
            }
            return true;
        }

        if(command.getName().equalsIgnoreCase("atm")){
            if(commandSender instanceof Player){
                Player p = (Player) commandSender;
                if(p.hasPermission("ZeParadox.atm")){
                    main.getConfig().set("players." + p.getUniqueId().toString() + ".atm", true);
                    main.saveConfig();
                    p.sendMessage(utility.colorString(utility.tag + "Left click to remove."));
                    p.sendMessage(utility.colorString(utility.tag + "Right click to create."));
                }
            }
            return true;
        }

        if(command.getName().equalsIgnoreCase("car")){
            if(commandSender instanceof Player){
                Player p = (Player) commandSender;
                if(p.hasPermission("ZeParadox.cars.normal")){
                    cars.carNormalSpawn(p);
                }
            }
            return true;
        }

        if(command.getName().equalsIgnoreCase("addElevator")){
            if(commandSender instanceof Player){
                Player p = (Player) commandSender;
                if(p.hasPermission("ZeParadox.addElevator")){
                    Block b = p.getLocation().getBlock();
                    utility.setLocation(b.getLocation(), "server.elevators." + b.getX() + b.getY() + b.getZ());
                    p.sendMessage(utility.colorString(utility.tag + "Location: x" + b.getX() + " y" + b.getY() + " z" + b.getZ() + " is now an elevator."));
                }
            }
            return true;
        }
        if(command.getName().equalsIgnoreCase("removeElevator")){
            if(commandSender instanceof Player){
                Player p = (Player) commandSender;
                if(p.hasPermission("ZeParadox.removeElevator")){
                    Block b = p.getLocation().getBlock();
                    utility.removeLocation("server.elevators." + b.getX() + b.getY() + b.getZ());
                    p.sendMessage(utility.colorString(utility.tag + "Location: x" + b.getX() + " y" + b.getY() + " z" + b.getZ() + " is now not a elevator."));
                }
            }
            return true;
        }

        if(command.getName().equalsIgnoreCase("showElevators")) {
            if (commandSender instanceof Player) {
                Player p = (Player) commandSender;
                if(p.hasPermission("ZeParadox.showElevators")){
                    if (main.getConfig().get("server.show.elevators") == null) {
                        main.getConfig().set("server.show.elevators", true);
                        p.sendMessage(utility.colorString(utility.tag + "Elevators are now highlighted."));
                    } else {
                        if (main.getConfig().getBoolean("server.show.elevators")) {
                            main.getConfig().set("server.show.elevators", false);
                            p.sendMessage(utility.colorString(utility.tag + "Elevators are now un-highlighted."));
                        } else {
                            main.getConfig().set("server.show.elevators", true);
                            p.sendMessage(utility.colorString(utility.tag + "Elevators are now highlighted."));
                        }
                    }
                    main.saveConfig();
                }
            }
            return true;
        }

        if(command.getName().equalsIgnoreCase("powerTool")){
            if(commandSender instanceof Player){
                Player p = (Player) commandSender;
                if(p.hasPermission("ZeParadox.powerTool")){
                    if(strings.length > 0 ){
                        if(p.getInventory().getItemInMainHand() != null && !p.getInventory().getItemInMainHand().getType().equals(Material.AIR)){
                            ItemStack selectedItem = p.getInventory().getItemInMainHand();

                            StringBuilder selectedCommand = new StringBuilder();
                            for (String ss : strings) {
                                selectedCommand.append(" ").append(ss);
                            }

                            String pid = p.getUniqueId().toString();
                            main.getConfig().set("players." + pid + ".powerTools." + selectedItem.getType().name(), selectedCommand.toString().trim());
                            p.sendMessage(utility.colorString(utility.tag + selectedItem.getType().name() + " = /" + selectedCommand.toString().trim()));
                        }else{
                            p.sendMessage(utility.colorString(utility.tag + "Make sure you have something in your hand."));
                        }
                    }else{
                        p.sendMessage(utility.colorString(utility.tag + "Please enter a command to use."));
                    }
                }
            }
            return true;
        }

        if(command.getName().equalsIgnoreCase("list")){
            if(commandSender.hasPermission("ZeParadox.list")){
                String players = "";
                for (Player selectedPlayer : Bukkit.getServer().getOnlinePlayers()){
                    players = selectedPlayer.getName() + ", " + players;
                }
                commandSender.sendMessage(utility.colorString(utility.tag + "Players online: " + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers()));
                commandSender.sendMessage(utility.colorString(utility.tag + players));
            }
            return true;
        }

        if(command.getName().equalsIgnoreCase("lore")){
            if(commandSender instanceof Player){
                Player p = (Player) commandSender;
                if(p.hasPermission("ZeParadox.lore")){
                    if(p.getInventory().getItemInMainHand() != null && !p.getInventory().getItemInMainHand().getType().equals(Material.AIR)){
                        if(strings.length > 0){
                            try{
                                int foundNumber = Integer.parseInt(strings[0]);
                                ItemStack is = p.getInventory().getItemInMainHand();
                                ItemMeta im = is.getItemMeta();
                                List<String> newLore = new ArrayList<>();
                                if(im.hasLore()){
                                    newLore = new ArrayList<>(im.getLore());
                                }
                                if(strings.length > 1){
                                    String selectedLore = "";
                                    for (int i = 1; i < strings.length; i++) {
                                        selectedLore = selectedLore + " " + strings[i];
                                    }
                                    selectedLore = utility.colorString(selectedLore.trim());
                                    try{
                                        newLore.set(foundNumber, selectedLore);
                                    }catch (Exception ignore){
                                        try{
                                            newLore.add(selectedLore);
                                        }catch (Exception err){
                                            p.sendMessage(utility.colorString(utility.tag+err.getMessage()));
                                        }
                                    }
                                    im.setLore(newLore);
                                    is.setItemMeta(im);
                                    p.getInventory().setItemInMainHand(is);
                                }else{
                                    p.sendMessage(utility.colorString(utility.tag+"No lore detected!"));
                                }
                            }catch (Exception err){
                                p.sendMessage(utility.colorString(utility.tag+err.getMessage()));
                            }
                        }else{
                            p.sendMessage(utility.colorString(utility.tag+"Please select a line number!"));
                        }
                    }else{
                        p.sendMessage(utility.colorString(utility.tag+"You don't have a item!"));
                    }
                }else{
                    p.sendMessage(utility.colorString(utility.tag+"You don't have permission!"));
                }
            }else{
                commandSender.sendMessage(utility.colorString(utility.tag+"Your not a player!"));
            }
            return true;
        }

        if(command.getName().equalsIgnoreCase("name")){
            if(commandSender instanceof Player){
                Player p = (Player) commandSender;
                if(p.hasPermission("ZeParadox.name")){
                    if(p.getInventory().getItemInMainHand() != null && !p.getInventory().getItemInMainHand().getType().equals(Material.AIR)){
                        if(strings.length > 0){
                            StringBuilder newName = new StringBuilder();
                            for (String string : strings) {
                                newName.append(" ").append(string);
                            }
                            newName = new StringBuilder(newName.toString().trim());
                            ItemStack is = p.getInventory().getItemInMainHand();
                            ItemMeta im = is.getItemMeta();
                            im.setDisplayName(utility.colorString(newName.toString()));
                            is.setItemMeta(im);
                            p.getInventory().setItemInMainHand(is);
                            p.sendMessage(utility.colorString(utility.tag + "This item's name is now: " + newName.toString()));
                        }
                    }else{
                        p.sendMessage(utility.colorString(utility.tag + "You must have a item in your hand!"));
                    }
                }else{
                    p.sendMessage(utility.colorString(utility.tag + "You don't have permission!"));
                }
            }else{
                commandSender.sendMessage(utility.colorString(utility.tag + "Only a player can do this!"));
            }
            return true;
        }

        if(command.getName().equalsIgnoreCase("ZeParadox")){
            commandSender.sendMessage(utility.colorString(utility.tag + "Version: " + main.getDescription().getVersion()));
            commandSender.sendMessage(utility.colorString(utility.tag + "Created By: " + main.getDescription().getAuthors()));
            commandSender.sendMessage(utility.colorString(utility.tag + "Website: " + main.getDescription().getWebsite()));
            return true;
        }

        if(command.getName().equalsIgnoreCase("Enderchest")){
            if(commandSender.hasPermission("ZeParadox.Enderchest")){
                if(commandSender instanceof Player){
                    Player p = (Player) commandSender;
                    p.openInventory(p.getEnderChest());
                }
            }
            return true;
        }

        if(command.getName().equalsIgnoreCase("Guns")){
            if(commandSender instanceof Player){
                Player p = (Player) commandSender;
                p.openInventory(guns.gunInventory());
            }
            return true;
        }

        if(command.getName().equalsIgnoreCase("setHome")){
            if(commandSender instanceof Player){
                Player p = (Player) commandSender;
                if(p.hasPermission("ZeParadox.setHome")){
                    Location l = p.getLocation();
                    main.getConfig().set("players." + p.getUniqueId().toString() + ".home.x", l.getX());
                    main.getConfig().set("players." + p.getUniqueId().toString() + ".home.y", l.getY());
                    main.getConfig().set("players." + p.getUniqueId().toString() + ".home.z", l.getZ());

                    main.getConfig().set("players." + p.getUniqueId().toString() + ".home.yaw", l.getYaw());
                    main.getConfig().set("players." + p.getUniqueId().toString() + ".home.pitch", l.getPitch());

                    main.getConfig().set("players." + p.getUniqueId().toString() + ".home.world", l.getWorld().getName());

                    main.saveConfig();

                    p.sendMessage(utility.colorString(utility.tag + "Home saved, use /Home to get back."));
                }
            }
            return true;
        }
        if(command.getName().equalsIgnoreCase("setSpawn")){
            if(commandSender instanceof Player){
                Player p = (Player) commandSender;
                if(p.hasPermission("ZeParadox.setSpawn")){
                    Location l = p.getLocation();
                    main.getConfig().set("server.spawn.x", l.getX());
                    main.getConfig().set("server.spawn.y", l.getY());
                    main.getConfig().set("server.spawn.z", l.getZ());

                    main.getConfig().set("server.spawn.yaw", l.getYaw());
                    main.getConfig().set("server.spawn.pitch", l.getPitch());

                    main.getConfig().set("server.spawn.world", l.getWorld().getName());

                    main.saveConfig();

                    p.sendMessage(utility.colorString(utility.tag + "Spawn saved, use /spawn to get back."));
                }
            }
            return true;
        }
        if(command.getName().equalsIgnoreCase("forceSpawn")){
            if(commandSender instanceof Player){
                Player p = (Player) commandSender;
                if(p.hasPermission("ZeParadox.forceSpawn")){
                    if(main.getConfig().get("server.spawn") != null){
                        if(main.getConfig().getBoolean("server.forceSpawn")){
                            main.getConfig().set("server.forceSpawn", false);
                            p.sendMessage(utility.colorString(utility.tag + "Players will now spawn at there last position!"));
                        }else{
                            main.getConfig().set("server.forceSpawn", true);
                            p.sendMessage(utility.colorString(utility.tag + "Players will now spawn at spawn point every time!"));
                        }
                    }else{
                        p.sendMessage(utility.colorString(utility.tag + "Please set a spawn first!"));
                    }
                    main.saveConfig();
                }
            }
            return true;
        }

        if(command.getName().equalsIgnoreCase("Invsee")){
            if(commandSender instanceof  Player){
                Player p = (Player) commandSender;
                if(p.hasPermission("ZeParadox.Invsee")){
                    if(strings.length > 0){
                        Player target = null;
                        try{
                            target = Bukkit.getPlayer(strings[0]);
                            if(Bukkit.getOnlinePlayers().contains(target)){
                                //Inventory inv = Bukkit.createInventory(null, 9*4, utility.colorString(utility.tag + target.getDisplayName()));
                                p.openInventory(target.getInventory());
                            }
                        }catch (Exception Ignore){
                            p.sendMessage(utility.colorString(utility.tag + "Player not found!"));
                        }
                    }else{
                        p.sendMessage(utility.colorString(utility.tag + "Please select a player.."));
                    }
                }
            }
            return true;
        }

        if(command.getName().equalsIgnoreCase("Home")){
            if(commandSender instanceof Player){
                Player p = (Player) commandSender;
                if(p.hasPermission("ZeParadox.Home")){
                    if(main.getConfig().get("players." + p.getUniqueId().toString() + ".home") != null){
                        String wName = main.getConfig().getString("players." + p.getUniqueId().toString() + ".home.world");
                        World w = Bukkit.getWorld(wName);
                        Double x = main.getConfig().getDouble("players." + p.getUniqueId().toString() + ".home.x");
                        Double y = main.getConfig().getDouble("players." + p.getUniqueId().toString() + ".home.y");
                        Double z = main.getConfig().getDouble("players." + p.getUniqueId().toString() + ".home.z");
                        Float yaw = (float) main.getConfig().getDouble("players." + p.getUniqueId().toString() + ".home.yaw");
                        Float pitch = (float) main.getConfig().getDouble("players." + p.getUniqueId().toString() + ".home.pitch");

                        Location l = new Location(w, x, y, z, yaw, pitch);
                        p.teleport(l);
                    }else{
                        p.sendMessage(utility.colorString(utility.tag + "Make sure to set your home first /setHome"));
                    }
                }else{
                    p.sendMessage(utility.colorString(utility.tag + "Sorry you can't do that!"));
                }
            }else{
                commandSender.sendMessage(utility.colorString(utility.tag + "Only a player can do that!"));
            }
            return true;
        }

        if(command.getName().equalsIgnoreCase("Spawn")){
            if(commandSender instanceof Player){
                Player p = (Player) commandSender;
                if(p.hasPermission("ZeParadox.Spawn")){
                    if(main.getConfig().get("server.spawn") != null){
                        String wName = main.getConfig().getString("server.spawn.world");
                        World w = Bukkit.getWorld(wName);
                        Double x = main.getConfig().getDouble("server.spawn.x");
                        Double y = main.getConfig().getDouble("server.spawn.y");
                        Double z = main.getConfig().getDouble("server.spawn.z");
                        Float yaw = (float) main.getConfig().getDouble("server.spawn.yaw");
                        Float pitch = (float) main.getConfig().getDouble("server.spawn.pitch");

                        Location l = new Location(w, x, y, z, yaw, pitch);
                        p.teleport(l);
                    }else{
                        p.sendMessage(utility.colorString(utility.tag + "No spawn has been set!"));
                    }
                }else{
                    p.sendMessage(utility.colorString(utility.tag + "Sorry you can't do that!"));
                }
            }else{
                commandSender.sendMessage(utility.colorString(utility.tag + "Only a player can do that!"));
            }
            return true;
        }

        return false;
    }


}
