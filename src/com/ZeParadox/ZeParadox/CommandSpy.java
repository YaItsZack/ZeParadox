package com.ZeParadox.ZeParadox;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandSpy implements Listener {

    private Main main;
    CommandSpy(Main main) {
        this.main = main;
        utility = new Utility(this.main);
    }
    private Utility utility;

    @EventHandler
    public void lol(PlayerCommandPreprocessEvent e){
        for(Player p : Bukkit.getOnlinePlayers()){
            if(p.hasPermission("ZeParadox.CommandSpy")){
                if(main.getConfig().getBoolean("players." + p.getUniqueId().toString() + ".CommandSpy")){
                    p.sendMessage(utility.colorString("&8[&bSpy&8] &7" + e.getPlayer().getDisplayName() + ": " + e.getMessage()));
                }
            }
        }
    }

}
