package com.ZeParadox.ZeParadox;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.ArrayList;
import java.util.List;

class Elevators implements Listener {

    private Main main;
    Elevators(Main main) {
        this.main = main;
        utility = new Utility(this.main);
    }
    private Utility utility;

    private List<Player> cooldownElevator = new ArrayList<>();

    private void startElevatorTask(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> {
            if(main.getConfig().getBoolean("server.show.elevators")){
                for(String selectedElevator : main.getConfig().getConfigurationSection("server.elevators").getKeys(false)) {
                    Double x = main.getConfig().getDouble("server.elevators." + selectedElevator + ".x");
                    Double y = main.getConfig().getDouble("server.elevators." + selectedElevator + ".y");
                    Double z = main.getConfig().getDouble("server.elevators." + selectedElevator + ".z");
                    World w = Bukkit.getWorld(main.getConfig().getString("server.elevators." + selectedElevator + ".world"));
                    utility.customParticle(w, x + 0.5, y - 0.5 + 0.5, z + 0.5, 1, 0, 0, 1);
                    utility.customParticle(w, x + 0.5, y - 0.5 - 0.5, z + 0.5, 1, 0, 0, 1);
                    utility.customParticle(w, x + 0.5 + 0.5, y - 0.5, z + 0.5, 1, 0, 0, 1);
                    utility.customParticle(w, x + 0.5 - 0.5, y - 0.5, z + 0.5, 1, 0, 0, 1);
                    utility.customParticle(w, x + 0.5, y - 0.5, z + 0.5 + 0.5, 1, 0, 0, 1);
                    utility.customParticle(w, x + 0.5, y - 0.5, z + 0.5 - 0.5, 1, 0, 0, 1);
                }
            }
        }, 5L, 5L);
    }

    @EventHandler
    private void onSneak(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        try{
            if (!cooldownElevator.contains(p)) {
                double y = p.getLocation().getBlock().getY();
                boolean onBlock = false;
                for (String selectedElevator : main.getConfig().getConfigurationSection("server.elevators").getKeys(false)) {
                    if (p.getLocation().getBlock().getLocation().equals(utility.getLocation("server.elevators." + selectedElevator))) {
                        onBlock = true;
                    }
                }
                if (onBlock) {
                    if(p.isSneaking()){
                        double nextFloor = 0;
                        boolean nextFloorFound = false;
                        for (int i = (int) y; i > y - 7; --i) {
                            for(String selectedElevator : main.getConfig().getConfigurationSection("server.elevators").getKeys(false)){
                                if(main.getConfig().getDouble("server.elevators." + selectedElevator + ".x") == p.getLocation().getBlock().getX()){
                                    if(main.getConfig().getDouble("server.elevators." + selectedElevator + ".y") == i){
                                        if(main.getConfig().getDouble("server.elevators." + selectedElevator + ".z") == p.getLocation().getBlock().getZ()){
                                            nextFloor = i;
                                            nextFloorFound = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        if(nextFloorFound){
                            Location ll = p.getLocation();
                            ll.setY(nextFloor);
                            p.teleport(ll);
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 1f, 1f);

                            cooldownElevator.add(p);
                            Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> cooldownElevator.remove(p), 10L);
                        }
                    }
                }
            }
        }catch (Exception ignore){

        }
    }

    @EventHandler
    private void PlayerMoveEvent(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        try{
            if (!cooldownElevator.contains(p)) {
                double y = p.getLocation().getBlock().getY();
                boolean onBlock = false;
                for (String selectedElevator : main.getConfig().getConfigurationSection("server.elevators").getKeys(false)) {
                    if (p.getLocation().getBlock().getLocation().equals(utility.getLocation("server.elevators." + selectedElevator))) {
                        onBlock = true;
                    }
                }
                if (onBlock) {
                    if(!p.isOnGround()){
                        double nextFloor = 0;
                        boolean nextFloorFound = false;
                        for(int i = (int) y; i < y + 7; i++){
                            for(String selectedElevator : main.getConfig().getConfigurationSection("server.elevators").getKeys(false)){
                                if(main.getConfig().getDouble("server.elevators." + selectedElevator + ".x") == p.getLocation().getBlock().getX()){
                                    if(main.getConfig().getDouble("server.elevators." + selectedElevator + ".y") == i){
                                        if(main.getConfig().getDouble("server.elevators." + selectedElevator + ".z") == p.getLocation().getBlock().getZ()){
                                            nextFloor = i;
                                            nextFloorFound = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        if(nextFloorFound){
                            Location ll = p.getLocation();
                            ll.setY(nextFloor);
                            p.teleport(ll);
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f);

                            cooldownElevator.add(p);
                            Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> cooldownElevator.remove(p), 10L);
                        }
                    }
                }
            }
        }catch (Exception ignore){

        }
    }

}
