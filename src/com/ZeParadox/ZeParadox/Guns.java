package com.ZeParadox.ZeParadox;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Guns implements Listener {

    private List<Player> cooldownRevolver = new ArrayList<>();
    private List<Player> cooldownColt = new ArrayList<>();

    private Main main;
    Guns(Main main) {
        this.main = main;
        utility = new Utility(this.main);
    }
    private Utility utility;


    private ItemStack gunRevolver() {
        return utility.createItem(Material.BLAZE_ROD, 1, (short) 0, "&dRevolver", Arrays.asList("&fCategory: &7Pistol","&fDamage: &73 Hearts", "&fAmmo: &79mm"), null);
    }
    private ItemStack gunColt() {
        return utility.createItem(Material.STICK, 1, (short) 0, "&9Colt 1911", Arrays.asList("&fCategory: &7Pistol","&fDamage: &74 Hearts", "&fAmmo: &7.45"), null);
    }
    private ItemStack meleeBaton() {
        return utility.createItem(Material.STICK, 1, (short) 0, "&6Baton", Arrays.asList("&fCategory: &7Melee","&fDamage: &70 Hearts", "&fDescription: &7Knocks people out."), null);
    }
    private ItemStack ammo9mm() {
        return utility.createItem(Material.BEETROOT_SEEDS, 1, (short) 0, "&d9mm", Arrays.asList("&7This pistol round is officially known as the “9x19mm Parabellum”.", "&7Or “9mm Luger” to distinguish it from other 9mm rounds.", "&7But you will be fine just saying “nine millimeter” or “nine mil”."), null);
    }
    private ItemStack ammo45() {
        return utility.createItem(Material.PUMPKIN_SEEDS, 1, (short) 0, "&9.45", Arrays.asList("&7The .45 Designed in 1904 by Mr. John Browning.", "&7Designed for the famous 1911 pistol.", "&7This round has one heck of a history."), null);
    }
    Inventory gunInventory(){
        Inventory gi = Bukkit.createInventory(null, 9 * 3, utility.colorString("&8[ &cGuns and Ammo &8]"));

        gi.setItem(0, gunRevolver()); gi.setItem(1, gunColt());
        gi.setItem(9, ammo9mm()); gi.setItem(10, ammo45());
        gi.setItem(25, meleeBaton());

        return gi;
    }
    private ItemStack bulletBaton() {
        return utility.createItem(Material.ANVIL, 1, (short) 0, "Baton Bullet", Arrays.asList("The Baton Bullet"), null);
    }
    private ItemStack bulletRevolver() {
        return utility.createItem(Material.STONE_BUTTON, 1, (short) 0, "Revolver Bullet", Arrays.asList("The Revolver Bullet"), null);
    }
    private ItemStack bulletColt() {
        return utility.createItem(Material.STONE_BUTTON, 1, (short) 0, "Colt Bullet", Arrays.asList("The Colt Bullet"), null);
    }


    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent e) {
        if (e.getInventory().getTitle().equals(gunInventory().getTitle())) {

            if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(gunColt().getItemMeta().getDisplayName())) {
                e.getWhoClicked().getInventory().addItem(gunColt());
                e.getWhoClicked().sendMessage(utility.colorString(utility.tag + "Spawned " + gunColt().getItemMeta().getDisplayName() + "&b!"));
            }

            if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(gunRevolver().getItemMeta().getDisplayName())) {
                e.getWhoClicked().getInventory().addItem(gunRevolver());
                e.getWhoClicked().sendMessage(utility.colorString(utility.tag + "Spawned " + gunRevolver().getItemMeta().getDisplayName() + "&b!"));

            }

            if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(meleeBaton().getItemMeta().getDisplayName())){
                e.getWhoClicked().getInventory().addItem(meleeBaton());
                e.getWhoClicked().sendMessage(utility.colorString(utility.tag + "Spawned " + meleeBaton().getItemMeta().getDisplayName() + "&b!"));

            }

            e.getWhoClicked().closeInventory();
            e.setCancelled(true);
        }
    }


    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Location l = p.getEyeLocation();
        if (e.hasItem()) {
            if (e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(meleeBaton().getItemMeta().getDisplayName())) {
                e.setCancelled(true);
                if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                    Location sl = l.subtract(0, 0.2, 0);
                    Item i = e.getPlayer().getWorld().dropItem(sl, bulletBaton());
                    i.setPickupDelay(32767);
                    i.setGravity(false);
                    i.setCustomName(p.getUniqueId().toString());
                    i.setVelocity(e.getPlayer().getEyeLocation().getDirection().multiply(4));
                    Bukkit.getScheduler().scheduleSyncDelayedTask(main, i::remove, 1L);
                }
            }
            if (e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(gunColt().getItemMeta().getDisplayName())) {
                e.setCancelled(true);
                if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                    if (!cooldownColt.contains(p)) {
                        Location sl = l.subtract(0, 0.2, 0);
                        Item i = e.getPlayer().getWorld().dropItem(sl, bulletColt());
                        sl.getWorld().playSound(sl, Sound.ENTITY_PLAYER_ATTACK_CRIT, 1f, 1f);
                        sl.getWorld().playSound(sl, Sound.ENTITY_GENERIC_EXPLODE, 0.75f, 2.5f);
                        i.setPickupDelay(32767);
                        i.setGravity(false);
                        i.setCustomName(p.getUniqueId().toString());
                        i.setVelocity(e.getPlayer().getEyeLocation().getDirection().multiply(2));
                        Bukkit.getScheduler().scheduleSyncDelayedTask(main, i::remove, 15);
                        cooldownColt.add(p);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> cooldownColt.remove(p), 8);
                    }
                }
            }
            if (e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(gunRevolver().getItemMeta().getDisplayName())) {
                e.setCancelled(true);
                if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                    if (!cooldownRevolver.contains(p)) {
                        Location sl = l.subtract(0, 0.2, 0);
                        Item i = e.getPlayer().getWorld().dropItem(sl, bulletRevolver());
                        sl.getWorld().playSound(sl, Sound.ENTITY_PLAYER_ATTACK_CRIT, 1f, 1f);
                        sl.getWorld().playSound(sl, Sound.ENTITY_GENERIC_EXPLODE, 0.65f, 2.0f);
                        i.setPickupDelay(32767);
                        i.setGravity(false);
                        i.setCustomName(p.getUniqueId().toString());
                        i.setVelocity(e.getPlayer().getEyeLocation().getDirection().multiply(2));
                        Bukkit.getScheduler().scheduleSyncDelayedTask(main, i::remove, 15);
                        cooldownRevolver.add(p);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> cooldownRevolver.remove(p), 8);
                    }
                }
            }

        }

    }

    void gunWatcher(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> {
            for (World w : Bukkit.getWorlds()) {
                for (Entity e : w.getEntities()) {
                    if (e instanceof Item) {
                        Item i = (Item) e;
                        if (i.getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase(bulletRevolver().getItemMeta().getDisplayName())) {
                            w.spawnParticle(Particle.SMOKE_NORMAL, i.getLocation().add(0, 0.5, 0), 1, 0, 0, 0, 0);
                            for (Entity ee : i.getNearbyEntities(0.25, 0.25, 0.25)) {
                                for(Player p : i.getWorld().getPlayers()){
                                    if(i.getCustomName().equals(p.getUniqueId().toString())){
                                        if (ee instanceof LivingEntity) {
                                            LivingEntity le = (LivingEntity) ee;
                                            Boolean isPlayer = false;
                                            if (ee instanceof Player){
                                                Player pp = (Player) le;
                                                if(i.getCustomName().equals(pp.getUniqueId().toString())){
                                                    isPlayer = true;
                                                }
                                            }
                                            if(!isPlayer){
                                                le.damage(6);
                                                i.getLocation().getWorld().playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1f, 1f);
                                                i.remove();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (i.getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase(gunColt().getItemMeta().getDisplayName())) {
                            w.spawnParticle(Particle.SMOKE_NORMAL, i.getLocation().add(0, 0.5, 0), 1, 0, 0, 0, 0);
                            for (Entity ee : i.getNearbyEntities(0.25, 0.25, 0.25)) {
                                for(Player p : i.getWorld().getPlayers()){
                                    if(i.getCustomName().equals(p.getUniqueId().toString())){
                                        if (ee instanceof LivingEntity) {
                                            LivingEntity le = (LivingEntity) ee;
                                            Boolean isPlayer = false;
                                            if (ee instanceof Player){
                                                Player pp = (Player) le;
                                                if(i.getCustomName().equals(pp.getUniqueId().toString())){
                                                    isPlayer = true;
                                                }
                                            }
                                            if(!isPlayer){
                                                le.damage(8);
                                                i.getLocation().getWorld().playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1f, 1f);
                                                i.remove();
                                            }
                                        }
                                    }
                                }

                            }
                        }
                        if(i.getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase(bulletBaton().getItemMeta().getDisplayName())){
                            for (Entity ee : i.getNearbyEntities(1, 1, 1)) {
                                for(Player p : i.getWorld().getPlayers()){
                                    if(i.getCustomName().equals(p.getUniqueId().toString())){
                                        if (ee instanceof LivingEntity) {
                                            LivingEntity le = (LivingEntity) ee;
                                            Boolean isPlayer = false;
                                            if (ee instanceof Player){
                                                Player pp = (Player) le;
                                                if(i.getCustomName().equals(pp.getUniqueId().toString())){
                                                    isPlayer = true;
                                                }
                                            }
                                            if(!isPlayer){
                                                le.setVelocity(i.getVelocity());
                                                i.remove();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }, 0L, 1L);
    }

}
