package com.patatuffolo.report.inventories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ReportInventory implements InventoryHolder {

    private Inventory inv;
    private ItemStack item;
    public static String reports[] = new String[54];
    public static String servers[] = new String[54];

    private List<String> lore = new ArrayList<>();

    public ReportInventory() {
        inv = Bukkit.createInventory(this, 54, "Report GUI");
        init();
    }

    private void init() {
        //Riempri l'inventario di report.
        try {
            getReports();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        /**
         * comandi utili:
         * item = createItem("Accept", Material.mat, Collection.singletonList("Accepts the request"));
         * inv.setItem(slot, item);
         * List<String> lore = new ArrayList<>();
         * lore.add("String");
         * item = createItem("Accept", Material.mat, lore);
         */
    }

    private void getReports() throws SQLException {
        int i = 0;
        if (dbConnects("SELECT * FROM plugin_reports")) {
            PreparedStatement st = con.prepareStatement(statement);
            ResultSet results = st.executeQuery();
            while (results.next()) {
                if (i < 52) {
                    i++;

                    reports[i] = "P:" + results.getString("PlayerReported");
                    servers[i] = "S:" + results.getString("Server");

                    lore.add("§l§cPlayer: §r§c'" + reports[i].split("P:")[0] + "' - " + online(reports[i].split("P:")[0]));
                    lore.add("§l§2Reason: §r§2" + results.getString("Reason"));
                    lore.add("§l§9Server: §r§9" + servers[i].split("S:")[0]);

                    item = createItem(("§6§lReport: " + i), Material.GREEN_WOOL, lore);

                    inv.setItem(inv.firstEmpty(), item);
                }
                else {
                    lore.add("Prossima Pagina");
                    inv.setItem(inv.firstEmpty(), createItem("Prossima Pagina", Material.BOOK, lore));
                }
                //if (i > 54) {inv.setItem(54, createItem(">54", Material.ACACIA_FENCE, lore));}
            }
        }
    }

    static Connection con;
    static String statement;

    private static boolean dbConnects(String st) {
        try {
            con = DriverManager.getConnection("jdbc:mysql://sql11.freemysqlhosting.net/sql11423347?user=sql11423347&password=YD8XngrtfL");
            statement = st;
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    private ItemStack createItem(String name, Material mat, List<String> lore) {
        ItemStack item = new ItemStack(mat, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

    public String online(String name) {
        for (Player player1 : Bukkit.getServer().getOnlinePlayers()) {
            if (player1.getName().equals(name)) {
                return "§l§2ONLINE";
            }
        }
        return "§l§4OFFLINE";
    }

    public static boolean rmReport(String name) throws SQLException {
        if (dbConnects("")) {
            int j = 0;
            PreparedStatement st = con.prepareStatement(statement);
            ResultSet results = st.executeQuery();
            while (results.next()) {
                j++;
            }
        }
        else {
            return false;
        }

        if (dbConnects("DELETE FROM plugin_reports WHERE PlayerReported = '" + name + "'")) {
            return true;
        }
        else {
            return false;
        }
    }
}
