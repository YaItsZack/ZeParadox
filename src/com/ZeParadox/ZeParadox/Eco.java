package com.ZeParadox.ZeParadox;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

class Eco implements Listener {

    private Main main;
    Eco(Main main) {
        this.main = main;
        utility = new Utility(this.main);
    }
    private Utility utility;

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        Player p = e.getPlayer();
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                Block b = e.getClickedBlock();
                if (b.getType() == Material.SIGN || b.getType() == Material.WALL_SIGN) {
                    Sign s = (Sign) b.getState();
                    int sx = s.getX();
                    int sy = s.getY();
                    int sz = s.getZ();
                    if (main.getConfig().getBoolean("players." + p.getUniqueId().toString() + ".atm")) {
                        if (main.getConfig().get("server.atms." + sx + sy + sz) != null) {
                            p.sendMessage(utility.colorString(utility.tag + "This is a Atm.."));
                            main.getConfig().set("players." + p.getUniqueId().toString() + ".atm", false);
                        } else {
                            s.setLine(0, utility.colorString("&b❖ &9&lATM &b❖"));
                            s.setLine(1, utility.colorString("&b&m&l--------------"));
                            s.setLine(2, utility.colorString("&9Please Use Your"));
                            s.setLine(3, utility.colorString("&9Credit Card."));
                            s.update(true);
                            main.getConfig().set("players." + p.getUniqueId().toString() + ".atm", false);
                            main.getConfig().set("server.atms." + sx + sy + sz + ".x", sx);
                            main.getConfig().set("server.atms." + sx + sy + sz + ".y", sy);
                            main.getConfig().set("server.atms." + sx + sy + sz + ".z", sz);
                            p.sendMessage(utility.colorString(utility.tag + "Sign: x" + sx + " y" + sy + " z" + sz + " is now a ATM."));
                        }
                    } else {
                        if (main.getConfig().get("server.atms." + sx + sy + sz) != null) {
                            if (main.getConfig().get("players." + p.getUniqueId().toString() + ".account") != null) {
                                if (p.getInventory().getItemInMainHand().equals(creditCard(p))) {
                                    p.openInventory(bankInventory());
                                } else {
                                    if (p.getInventory().getItemInMainHand().getType().name().equals("AIR")) {
                                        p.sendMessage(utility.colorString(utility.tag + "We could not read your HAND."));
                                    } else {
                                        p.sendMessage(utility.colorString(utility.tag + "We could not read your " + p.getInventory().getItemInMainHand().getType().name() + "."));
                                    }
                                }
                            } else {
                                newAccount(p);
                            }
                        } else {
                            main.getConfig().set("players." + p.getUniqueId().toString() + ".atm", false);
                        }
                    }
                } else {
                    main.getConfig().set("players." + p.getUniqueId().toString() + ".atm", false);
                }
                main.saveConfig();
            }
            if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                Block b = e.getClickedBlock();
                if (b.getType() == Material.SIGN || b.getType() == Material.WALL_SIGN) {
                    Sign s = (Sign) b.getState();
                    int sx = s.getX();
                    int sy = s.getY();
                    int sz = s.getZ();
                    if (main.getConfig().get("server.atms." + sx + sy + sz) != null) {
                        if (main.getConfig().getBoolean("players." + p.getUniqueId().toString() + ".atm")) {
                            main.getConfig().set("server.atms." + sx + sy + sz, null);
                            p.sendMessage(utility.colorString(utility.tag + "ATM Removed."));
                            main.getConfig().set("players." + p.getUniqueId().toString() + ".atm", false);
                        } else {
                            main.getConfig().set("players." + p.getUniqueId().toString() + ".atm", false);
                            e.setCancelled(true);
                        }
                    }
                } else {
                    main.getConfig().set("players." + p.getUniqueId().toString() + ".atm", false);
                }
                main.saveConfig();
            }
    }

    private Inventory bankInventory(){
        Inventory gi = Bukkit.createInventory(null, InventoryType.HOPPER, utility.colorString("&b❖ &9&lBank &b❖"));
        for (int i = 0; i < gi.getSize(); i++) {
            gi.setItem(i, utility.createItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, (short) 0, "&9&lInfo", Arrays.asList("&7Welcome to the bank!","&8With this inventory you can","&7Deposit money into your account.", "&8Withdraw money out of your account", "&7And pay other players."), null));
        }
        gi.setItem(0, bankBalance());
        gi.setItem(2, bankPay());
        gi.setItem(3, bankWithdraw());
        gi.setItem(4, bankDeposit());
        return gi;
    }
    private Inventory playersInventory(){
        Inventory gi = Bukkit.createInventory(null, 9*5, utility.colorString("&b❖ &9&lPlayers &b❖"));
        for(Player p : Bukkit.getServer().getOnlinePlayers()){
            gi.addItem(playerHead(p));
        }
        return gi;
    }
    private ItemStack playerHead(Player p){
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwningPlayer(p);
        skull.setItemMeta(meta);
        return skull;
    }
    private Inventory depositInventory(){
        Inventory gi = Bukkit.createInventory(null, InventoryType.HOPPER, utility.colorString("&b❖ &9&lDeposit &b❖"));
        gi.setItem(0, amount1());
        gi.setItem(1, amount5());
        gi.setItem(2, amount10());
        gi.setItem(3, amount100());
        gi.setItem(4, amount1000());
        return gi;
    }
    private Inventory withdrawInventory(){
        Inventory gi = Bukkit.createInventory(null, InventoryType.HOPPER, utility.colorString("&b❖ &9&lWithdraw &b❖"));
        gi.setItem(0, amount1());
        gi.setItem(1, amount5());
        gi.setItem(2, amount10());
        gi.setItem(3, amount100());
        gi.setItem(4, amount1000());
        return gi;
    }
    private Inventory sendInventory(){
        Inventory gi = Bukkit.createInventory(null, InventoryType.HOPPER, utility.colorString("&b❖ &9&lPay &b❖"));
        gi.setItem(0, amount1());
        gi.setItem(1, amount5());
        gi.setItem(2, amount10());
        gi.setItem(3, amount100());
        gi.setItem(4, amount1000());
        return gi;
    }

    private ItemStack creditCard(Player p){
        String pid = p.getUniqueId().toString();
        return utility.createItem(Material.KNOWLEDGE_BOOK, 1, (short) 0, "&b❖ &9&lCredit Card &b❖", Arrays.asList("&b" + pid), null);
    }

    private ItemStack amount1() {
        return utility.createItem(Material.BLUE_WOOL, 1, (short) 0, "&b❖1", Arrays.asList("&7❖ Is the", "&7servers currency!"), null);
    }
    private ItemStack amount5() {
        return utility.createItem(Material.LIME_WOOL, 1, (short) 0, "&b❖5", Arrays.asList("&7❖ Is the", "&7servers currency!"), null);
    }
    private ItemStack amount10() {
        return utility.createItem(Material.YELLOW_WOOL, 1, (short) 0, "&b❖10", Arrays.asList("&7❖ Is the", "&7servers currency!"), null);
    }
    private ItemStack amount100() {
        return utility.createItem(Material.ORANGE_WOOL, 1, (short) 0, "&b❖100", Arrays.asList("&7❖ Is the", "&7servers currency!"), null);
    }
    private ItemStack amount1000() {
        return utility.createItem(Material.RED_WOOL, 1, (short) 0, "&b❖1000", Arrays.asList("&7❖ Is the", "&7servers currency!"), null);
    }

    private ItemStack bankBalance() {
        return utility.createItem(Material.PURPLE_WOOL, 1, (short) 0, "&9&lBalance", Arrays.asList("&7Click to view your balance."), null);
    }
    private ItemStack bankDeposit() {
        return utility.createItem(Material.BLUE_WOOL, 1, (short) 0, "&9&lDeposit", Arrays.asList("&7Deposit into the bank!"), null);
    }
    private ItemStack bankWithdraw() {
        return utility.createItem(Material.YELLOW_WOOL, 1, (short) 0, "&9&lWithdraw", Arrays.asList("&7Withdraw out of the bank!"), null);
    }
    private ItemStack bankPay() {
        return utility.createItem(Material.RED_WOOL, 1, (short) 0, "&9&lPay", Arrays.asList("&7Pay a player!"), null);
    }

    void newAccount(Player p){
        main.getConfig().set("players." + p.getUniqueId().toString() + ".account.debit", 1000);
        main.getConfig().set("players." + p.getUniqueId().toString() + ".account.cash", 0);
        main.saveConfig();
        p.sendMessage(utility.colorString(utility.tag + "Welcome to the bank!"));
        giveCard(p);
    }
    void giveCard(Player p){
        p.getInventory().addItem(creditCard(p));
        p.sendMessage(utility.colorString(utility.tag + "Here is your new card!"));
    }

    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent e) {
        if(e.getClickedInventory().getTitle().equalsIgnoreCase(playersInventory().getTitle())){
            e.setCancelled(true);
            ItemStack skull = e.getCurrentItem();
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            main.getConfig().set("players."+e.getWhoClicked().getUniqueId().toString()+".account.selected", meta.getOwningPlayer().getUniqueId().toString());
            main.saveConfig();
            e.getWhoClicked().closeInventory();
            e.getWhoClicked().openInventory(sendInventory());
        }
        if(e.getClickedInventory().getTitle().equalsIgnoreCase(sendInventory().getTitle())) {
            Player p = null;
            for (Player pp : Bukkit.getServer().getOnlinePlayers()) {
                if (pp.getUniqueId().toString().equals(main.getConfig().getString("players." + e.getWhoClicked().getUniqueId().toString() + ".account.selected"))) {
                    p = pp;
                }
            }
            if (p != null) {
                if (e.getCurrentItem().equals(amount1())) {
                    payPlayer((Player) e.getWhoClicked(), 1, p);
                    e.getWhoClicked().closeInventory();
                }
                if (e.getCurrentItem().equals(amount5())) {
                    payPlayer((Player) e.getWhoClicked(), 5, p);
                    e.getWhoClicked().closeInventory();
                }
                if (e.getCurrentItem().equals(amount10())) {
                    payPlayer((Player) e.getWhoClicked(), 10, p);
                    e.getWhoClicked().closeInventory();
                }
                if (e.getCurrentItem().equals(amount100())) {
                    payPlayer((Player) e.getWhoClicked(), 100, p);
                    e.getWhoClicked().closeInventory();
                }
                if (e.getCurrentItem().equals(amount1000())) {
                    payPlayer((Player) e.getWhoClicked(), 1000, p);
                    e.getWhoClicked().closeInventory();
                }
                main.getConfig().set("players." + e.getWhoClicked().getUniqueId().toString() + ".account.selected", null);
                main.saveConfig();
            }
        }
        if(e.getClickedInventory().getTitle().equalsIgnoreCase(depositInventory().getTitle())){
            e.setCancelled(true);
            if(e.getCurrentItem().equals(amount1())){
                depositMoney((Player) e.getWhoClicked(), 1);
                e.getWhoClicked().closeInventory();
            }
            if(e.getCurrentItem().equals(amount5())){
                depositMoney((Player) e.getWhoClicked(), 5);
                e.getWhoClicked().closeInventory();
            }
            if(e.getCurrentItem().equals(amount10())){
                depositMoney((Player) e.getWhoClicked(), 10);
                e.getWhoClicked().closeInventory();
            }
            if(e.getCurrentItem().equals(amount100())){
                depositMoney((Player) e.getWhoClicked(), 100);
                e.getWhoClicked().closeInventory();
            }
            if(e.getCurrentItem().equals(amount1000())){
                depositMoney((Player) e.getWhoClicked(), 1000);
                e.getWhoClicked().closeInventory();
            }
        }
        if(e.getClickedInventory().getTitle().equalsIgnoreCase(withdrawInventory().getTitle())){
            e.setCancelled(true);
            if(e.getCurrentItem().equals(amount1())){
                withdrawMoney((Player) e.getWhoClicked(), 1);
                e.getWhoClicked().closeInventory();
            }
            if(e.getCurrentItem().equals(amount5())){
                withdrawMoney((Player) e.getWhoClicked(), 5);
                e.getWhoClicked().closeInventory();
            }
            if(e.getCurrentItem().equals(amount10())){
                withdrawMoney((Player) e.getWhoClicked(), 10);
                e.getWhoClicked().closeInventory();
            }
            if(e.getCurrentItem().equals(amount100())){
                withdrawMoney((Player) e.getWhoClicked(), 100);
                e.getWhoClicked().closeInventory();
            }
            if(e.getCurrentItem().equals(amount1000())){
                withdrawMoney((Player) e.getWhoClicked(), 1000);
                e.getWhoClicked().closeInventory();
            }
        }

        if(e.getClickedInventory().getTitle().equalsIgnoreCase(bankInventory().getTitle())){
            e.setCancelled(true);
            if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(bankBalance().getItemMeta().getDisplayName())){
                int c = main.getConfig().getInt("players." + e.getWhoClicked().getUniqueId().toString() + ".account.cash");
                int d = main.getConfig().getInt("players." + e.getWhoClicked().getUniqueId().toString() + ".account.debit");
                e.getWhoClicked().sendMessage(utility.colorString(utility.tag + "Debit: " + d));
                e.getWhoClicked().sendMessage(utility.colorString(utility.tag + "Cash: " + c));
                e.getWhoClicked().closeInventory();
            }
            if(e.getCurrentItem().equals(bankDeposit())){
                e.getWhoClicked().closeInventory();
                e.getWhoClicked().openInventory(depositInventory());
            }
            if(e.getCurrentItem().equals(bankWithdraw())){
                e.getWhoClicked().closeInventory();
                e.getWhoClicked().openInventory(withdrawInventory());
            }
            if(e.getCurrentItem().equals(bankPay())){
                e.getWhoClicked().closeInventory();
                e.getWhoClicked().openInventory(playersInventory());
            }
        }

    }
    private void payPlayer(Player p, int a, Player t){
        int c = main.getConfig().getInt("players." + p.getUniqueId().toString() + ".account.cash");
        if(c >= a){
            main.getConfig().set("players." + p.getUniqueId().toString() + ".account.cash", (c-a));
            main.saveConfig();
            p.sendMessage(utility.colorString(utility.tag + "You payed &r" + t.getDisplayName() + ": &b❖" + a));
            int tc = main.getConfig().getInt("players." + t.getUniqueId().toString() + ".account.cash");
            main.getConfig().set("players." + t.getUniqueId().toString() + ".account.cash", (tc+a));
            main.saveConfig();
            t.sendMessage(utility.colorString(utility.tag + " &r" + p.getDisplayName() + "has payed you: &b❖" + a));
        }else{
            p.sendMessage(utility.colorString(utility.tag + " &cSorry you don't have that much."));
        }
    }
    private void depositMoney(Player p, int a){
        int d = main.getConfig().getInt("players." + p.getUniqueId().toString() + ".account.debit");
        int c = main.getConfig().getInt("players." + p.getUniqueId().toString() + ".account.cash");
        if(c >= a){
            main.getConfig().set("players." + p.getUniqueId().toString() + ".account.debit", (d+a));
            main.saveConfig();
            main.getConfig().set("players." + p.getUniqueId().toString() + ".account.cash", (c-a));
            main.saveConfig();
            p.sendMessage(utility.colorString(utility.tag + "You deposited: &b❖" + a));
        }else{
            p.sendMessage(utility.colorString(utility.tag + " &cSorry you don't have that much."));
        }
    }
    private void withdrawMoney(Player p, int a){
        int d = main.getConfig().getInt("players." + p.getUniqueId().toString() + ".account.debit");
        int c = main.getConfig().getInt("players." + p.getUniqueId().toString() + ".account.cash");
        if(d >= a){
            main.getConfig().set("players." + p.getUniqueId().toString() + ".account.debit", (d-a));
            main.saveConfig();
            main.getConfig().set("players." + p.getUniqueId().toString() + ".account.cash", (c+a));
            main.saveConfig();
            p.sendMessage(utility.colorString(utility.tag + "You withdrew: &b❖" + a));
        }else{
            p.sendMessage(utility.colorString(utility.tag + " &cSorry you don't have that much."));
        }
    }

}
