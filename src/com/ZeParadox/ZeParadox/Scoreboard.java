package com.ZeParadox.ZeParadox;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.ScoreboardManager;

class Scoreboard {

    private Main main;
    Scoreboard(Main main) {
        this.main = main;
        utility = new Utility(this.main);
    }
    private Utility utility;

    private ScoreboardManager manager = null;
    private org.bukkit.scoreboard.Scoreboard board = null;
    private Objective objective = null;
    void boardWatcher(){
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, () -> {
            for(Player player : Bukkit.getOnlinePlayers()){
                Player p = player.getPlayer();
                manager = Bukkit.getScoreboardManager();
                board = manager.getNewScoreboard();
                objective = board.registerNewObjective("ZeParadox", "dummy", "ZeParadox");
                objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                objective.setDisplayName(utility.colorString("&8> &bZeParadox &8<"));
                Score score = objective.getScore(utility.colorString("&8> &bOnline&8: &b" + Bukkit.getOnlinePlayers().size() + "&8/&b" + Bukkit.getMaxPlayers()));
                score.setScore(1);
                double health = p.getHealth();
                double maxHealth = p.getHealthScale();
                double percentHealth = (health / maxHealth) * 100;
                score = objective.getScore(utility.colorString("&8> &bHealth&8: &b" + Math.floor(percentHealth) + "&8%"));
                score.setScore(2);
                score = objective.getScore(utility.colorString("&8> &b" + (int) p.getLocation().getX() + "&8, &b" + (int) p.getLocation().getY() + "&8, &b" + (int) p.getLocation().getZ()));
                score.setScore(3);
                score = objective.getScore(utility.colorString("&8> &bFacing&8: &b" + method2(p)));
                score.setScore(4);
                p.setScoreboard(board);
            }
        },50L, 50L);
    }

    private String method2(Player player) {
        double rot = (player.getLocation().getYaw() - 180) % 360;
        if (rot < 0) {
            rot += 360.0;
        }
        return getDirection(rot);
    }

    private static String getDirection(double rot) {
        if (0 <= rot && rot < 22.5) {
            return "North";
        } else if (22.5 <= rot && rot < 67.5) {
            return "Northeast";
        } else if (67.5 <= rot && rot < 112.5) {
            return "East";
        } else if (112.5 <= rot && rot < 157.5) {
            return "Southeast";
        } else if (157.5 <= rot && rot < 202.5) {
            return "South";
        } else if (202.5 <= rot && rot < 247.5) {
            return "Southwest";
        } else if (247.5 <= rot && rot < 292.5) {
            return "West";
        } else if (292.5 <= rot && rot < 337.5) {
            return "Northwest";
        } else if (337.5 <= rot && rot < 360.0) {
            return "North";
        } else {
            return null;
        }
    }
}
