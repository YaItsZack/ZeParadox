package com.ZeParadox.ZeParadox;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

class Performance {

    private Main main;
    Performance(Main main) {
        this.main = main;
        utility = new Utility(this.main);
    }
    private Utility utility;


    private int	tps = 0;
    private long second = 0;
    private long sec = 0;
    private int ticks = 0;
    void performanceWatcher(){
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, () -> {
            sec = (System.currentTimeMillis() / 1000);
            if(second == sec)
            {
                ticks++;
            }
            else
            {
                second = sec;
                tps = (tps == 0 ? ticks : ((tps + ticks) / 2));
                if(tps < 15){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        if(p.hasPermission("ZeParadox.performance")){
                            p.sendMessage(utility.colorString(utility.tag + "Lag detected, TPS: " + tps));
                        }
                    }
                }
                ticks = 0;
            }
        },20, 1);
    }
}
