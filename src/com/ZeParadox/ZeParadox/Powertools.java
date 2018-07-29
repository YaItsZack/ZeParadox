package com.ZeParadox.ZeParadox;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

class Powertools implements Listener {

    private Main main;
    Powertools(Main main) {
        this.main = main;
        utility = new Utility(this.main);
    }
    private Utility utility;

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e) {

        if(e.hasItem()){
            try {
                if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                    String command = main.getConfig().getString("players." + e.getPlayer().getUniqueId().toString() + ".powerTools." + e.getItem().getType().name());
                    Bukkit.dispatchCommand(e.getPlayer(), command);
                }
            } catch (Exception ignore) {

            }
        }

    }

}
