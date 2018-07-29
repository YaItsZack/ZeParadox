package com.ZeParadox.ZeParadox;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.util.Vector;

class Cars implements Listener {

    private Main main;
    Cars(Main main) {
        this.main = main;
        utility = new Utility(this.main);
    }
    private Utility utility;

    void carsWatcher(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> {
            for(World w : Bukkit.getWorlds()){
                for(Player p  : w.getPlayers()){
                    if(p.isInsideVehicle()){
                        if(p.getVehicle() instanceof RideableMinecart){
                            RideableMinecart rm = (RideableMinecart) p.getVehicle();
                            if(main.getConfig().get("players." + p.getUniqueId().toString() + ".car.id") != null){
                                if(rm.getUniqueId().toString().equals(main.getConfig().getString("players." + p.getUniqueId().toString() + ".car.id"))){
                                    rm.setGravity(false);

                                    int speed = 0;
                                    if(main.getConfig().get("players." + p.getUniqueId().toString() + ".car.speed") != null){
                                        speed = main.getConfig().getInt("players." + p.getUniqueId().toString() + ".car.speed");
                                    }

                                    Vector v = p.getLocation().getDirection().multiply(speed);
                                    Vector vv = new Vector(0, 5, 0);
                                    rm.setVelocity(v.subtract(vv));
                                    utility.customParticle(rm.getWorld(), rm.getLocation().getX(), rm.getLocation().getY() + 0.5, rm.getLocation().getZ(), 0.5,0.5,0.5,0.5);
                                }
                            }
                        }
                    }
                }
            }
        }, 0L, 2L);
    }

    @EventHandler
    public void VehicleEnterEvent(VehicleEnterEvent e){
        if(e.getEntered() instanceof Player){
            Player p = (Player) e.getEntered();
            if(e.getVehicle() instanceof RideableMinecart){
                RideableMinecart rm = (RideableMinecart) e.getVehicle();
                if(rm.isCustomNameVisible()){
                    boolean kick = true;
                    String uuidrm = rm.getUniqueId().toString();
                    String uuidp = p.getUniqueId().toString();
                    if(main.getConfig().get("players." + uuidp + ".car.id") != null){
                        String uuidcar = main.getConfig().getString("players." + uuidp + ".car.id");
                        if(uuidcar.equals(uuidrm)){
                            kick = false;
                        }
                    }
                    if(kick){
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void VehicleExitEvent(VehicleExitEvent e){
        if(e.getExited() instanceof Player){
            Player p = (Player) e.getExited();
            if(e.getVehicle() instanceof RideableMinecart){
                RideableMinecart rm = (RideableMinecart) e.getVehicle();
                String rmid = rm.getUniqueId().toString();
                String pid = p.getUniqueId().toString();
                String carid = "";
                if(rm.isCustomNameVisible()){
                    if(main.getConfig().get("players." + pid + ".car.id") != null){
                        carid = main.getConfig().getString("players." + pid + ".car.id");
                    }
                    if(rmid.equals(carid)) {
                        main.getConfig().set("players." + pid + ".car.speed", null);
                    }
                }
            }
        }
    }

    @EventHandler
    public void VehicleDestroyEvent(VehicleDestroyEvent e){
        if(e.getAttacker() instanceof Player){
            Player p = (Player) e.getAttacker();
            if(e.getVehicle() instanceof RideableMinecart){
                RideableMinecart rm = (RideableMinecart) e.getVehicle();
                String rmid = rm.getUniqueId().toString();
                String pid = p.getUniqueId().toString();
                String carid = "";
                if(rm.isCustomNameVisible()){
                    if(main.getConfig().get("players." + pid + ".car.id") != null){
                        carid = main.getConfig().getString("players." + pid + ".car.id");
                    }
                    if(rmid.equals(carid)) {
                        e.setCancelled(true);
                        rm.remove();
                        main.getConfig().set("players." + pid + ".car", null);
                        p.sendMessage(utility.colorString(utility.tag + "You have removed your car.."));
                    }else{
                        e.setCancelled(true);
                        p.sendMessage(utility.colorString(utility.tag + "That's not your car!"));
                    }
                }
            }
        }
    }

    private boolean isInsideOfThereCar(Player p){
        boolean is = false;
        if(p.isInsideVehicle()){
            if(p.getVehicle() instanceof RideableMinecart){
                RideableMinecart rm = (RideableMinecart) p.getVehicle();
                if(main.getConfig().get("players." + p.getUniqueId().toString() + ".car.id") != null){
                    if(rm.getUniqueId().toString().equals(main.getConfig().getString("players." + p.getUniqueId().toString() + ".car.id"))){
                        is = true;
                    }
                }
            }
        }
        return is;
    }

    void carNormalSpawn(Player player){
        if(main.getConfig().get("players." + player.getUniqueId().toString() + ".car.id") == null){

            Entity entity = player.getWorld().spawnEntity(player.getLocation(), EntityType.MINECART);
            entity.setCustomNameVisible(true);
            entity.setCustomName(utility.colorString("&8[&b" + player.getDisplayName() + "&8] &bCar"));
            main.getConfig().set("players." + player.getUniqueId().toString() + ".car.id", entity.getUniqueId().toString());
            main.saveConfig();
            player.sendMessage(utility.colorString(utility.tag + "Spawned: car!"));

        }else{

            String boatID = main.getConfig().getString("players." + player.getUniqueId().toString() + ".car.id");
            boolean foundCar = false;
            Entity carFound = null;
            for(Entity ent : player.getWorld().getEntities()){
                if(ent.getUniqueId().toString().equals(boatID)){
                    foundCar = true;
                    carFound = ent;
                }
            }

            if(foundCar){
                carFound.teleport(player.getLocation());
                player.sendMessage(utility.colorString(utility.tag + "Spawned: car!"));
            }else{
                main.getConfig().get("players." + player.getUniqueId().toString() + ".car", null);
                carNormalSpawn(player);
            }

        }
    }

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        if (isInsideOfThereCar(e.getPlayer())) {
            e.setCancelled(true);
            if (main.getConfig().get("players." + e.getPlayer().getUniqueId().toString() + ".car.speed") != null) {
                if (main.getConfig().getInt("players." + e.getPlayer().getUniqueId().toString() + ".car.speed") == 0) {
                    main.getConfig().set("players." + e.getPlayer().getUniqueId().toString() + ".car.speed", 3);
                    main.saveConfig();
                    e.getPlayer().sendMessage(utility.colorString(utility.tag +"Switched to drive!"));
                } else {
                    main.getConfig().set("players." + e.getPlayer().getUniqueId().toString() + ".car.speed", 0);
                    main.saveConfig();
                    e.getPlayer().sendMessage(utility.colorString(utility.tag +"Switched to park."));
                }
            } else {
                main.getConfig().set("players." + e.getPlayer().getUniqueId().toString() + ".car.speed", 0);
                main.saveConfig();
                e.getPlayer().sendMessage(utility.colorString(utility.tag +"Engine started."));
            }
        }
    }

}
