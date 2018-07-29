package com.ZeParadox.ZeParadox;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

class Afk implements Listener {


    private Main main;
    Afk(Main main) {
        this.main = main;
        utility = new Utility(this.main);
    }
    private Utility utility;


    private Map<Player, Integer> Afk = new HashMap<>();

    private int afkMax = 300;
    private void notAfk(Player p){
        if(Afk.containsKey(p)){
            int time = Afk.get(p);
            if(time >= afkMax){
                Bukkit.broadcastMessage(utility.colorString(utility.tag + "&7&o" + p.getName() + " is not afk anymore!"));
            }
            Afk.remove(p);
        }
    }

    private void afkWatcher(){
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, () -> {
            for(Player player : Bukkit.getOnlinePlayers()){
                int current;
                Afk.putIfAbsent(player, 0);
                current = Afk.get(player);
                current ++;
                Afk.remove(player);
                Afk.put(player, current);
                if(Afk.get(player) == afkMax){
                    Bukkit.broadcastMessage(utility.colorString(utility.tag + "&7&o" + player.getName() + " is now afk!"));
                }
            }
        },0, 20);
    }

}
