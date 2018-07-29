package com.ZeParadox.ZeParadox;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class Main extends JavaPlugin implements Listener {


    private Utility utility = new Utility(this);
    private Guns guns = new Guns(this);
    private Scoreboard scoreboard = new Scoreboard(this);
    private Performance performance = new Performance(this);
    private Cars cars = new Cars(this);
    private Eco eco = new Eco(this);
    private CommandSpy commandSpy = new CommandSpy(this);

    @Override
    public void onEnable(){
        utility.log("> ZeParadox");
        getCommand("ZeParadox").setExecutor(new Commands(this));
        getCommand("Guns").setExecutor(new Commands(this));
        getCommand("setHome").setExecutor(new Commands(this));
        getCommand("Home").setExecutor(new Commands(this));
        getCommand("setSpawn").setExecutor(new Commands(this));
        getCommand("Spawn").setExecutor(new Commands(this));
        getCommand("forceSpawn").setExecutor(new Commands(this));
        getCommand("Invsee").setExecutor(new Commands(this));
        getCommand("Enderchest").setExecutor(new Commands(this));
        getCommand("name").setExecutor(new Commands(this));
        getCommand("lore").setExecutor(new Commands(this));
        getCommand("List").setExecutor(new Commands(this));
        getCommand("powerTool").setExecutor(new Commands(this));
        getCommand("showElevators").setExecutor(new Commands(this));
        getCommand("addElevator").setExecutor(new Commands(this));
        getCommand("removeElevator").setExecutor(new Commands(this));
        getCommand("car").setExecutor(new Commands(this));
        getCommand("atm").setExecutor(new Commands(this));
        getCommand("fish").setExecutor(new Commands(this));
        getCommand("CommandSpy").setExecutor(new Commands(this));
        getCommand("card").setExecutor(new Commands(this));
        utility.log("  &e+ &9Commands");
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new Guns(this), this);
        getServer().getPluginManager().registerEvents(new Cars(this), this);
        getServer().getPluginManager().registerEvents(new Elevators(this), this);
        getServer().getPluginManager().registerEvents(new Afk(this), this);
        getServer().getPluginManager().registerEvents(new Powertools(this), this);
        getServer().getPluginManager().registerEvents(new Eco(this), this);
        getServer().getPluginManager().registerEvents(new CommandSpy(this), this);
        utility.log("  &e+ &3Events");
        guns.gunWatcher();
        scoreboard.boardWatcher();
        performance.performanceWatcher();
        cars.carsWatcher();
        utility.log("  &e+ &2Tasks");
        utility.log("> Finished");
    }

    @Override
    public void onDisable(){
        utility.log("> ZeParadox");
        utility.log("  &c- &9Commands");
        utility.log("  &c- &3Events");
        utility.log("  &c- &2Tasks");
        utility.log("> Finished");
    }



    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e){
        e.setJoinMessage("");
    }
    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e){
        e.setQuitMessage("");
    }
    @EventHandler
    public void PlayerSpawnLocationEvent(PlayerSpawnLocationEvent e){

        if(getConfig().get("server.spawn") != null){
            if(getConfig().getBoolean("server.forceSpawn")){

                String wName = getConfig().getString("server.spawn.world");
                World w = Bukkit.getWorld(wName);
                Double x = getConfig().getDouble("server.spawn.x");
                Double y = getConfig().getDouble("server.spawn.y");
                Double z = getConfig().getDouble("server.spawn.z");
                Float yaw = (float) getConfig().getDouble("server.spawn.yaw");
                Float pitch = (float) getConfig().getDouble("server.spawn.pitch");

                Location l = new Location(w, x, y, z, yaw, pitch);
                e.setSpawnLocation(l);

            }
        }


    }
    @EventHandler
    public void Player(PlayerRespawnEvent e){
        if(getConfig().get("server.spawn") != null){
            if(getConfig().getBoolean("server.forceSpawn")){

                String wName = getConfig().getString("server.spawn.world");
                World w = Bukkit.getWorld(wName);
                Double x = getConfig().getDouble("server.spawn.x");
                Double y = getConfig().getDouble("server.spawn.y");
                Double z = getConfig().getDouble("server.spawn.z");
                Float yaw = (float) getConfig().getDouble("server.spawn.yaw");
                Float pitch = (float) getConfig().getDouble("server.spawn.pitch");

                Location l = new Location(w, x, y, z, yaw, pitch);
                e.setRespawnLocation(l);

            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void SignChangeEvent(SignChangeEvent e) {
        if (e.getPlayer().hasPermission("ZeParadox.sign.color")) {
            for (int i = 0; i < 4; i++) {
                String line = e.getLine(i);
                if (line != null && !line.equals("")) {
                    e.setLine(i, utility.colorString(line));
                }
            }
        }
    }

}
