package com.patatuffolo.report.events;

import com.patatuffolo.report.inventories.ReportInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.sql.SQLException;

public class InventoryEvents implements Listener {

    public static int nr = 0;

    @EventHandler
    public void onClick(InventoryClickEvent e) throws SQLException {
        if (e.getClickedInventory() == null) { return; }
        if (e.getClickedInventory().getHolder() instanceof ReportInventory) {
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            if (e.getCurrentItem() == null) { return; }
            if (e.getCurrentItem().getType() == Material.GREEN_WOOL) {
                if (ReportInventory.reports[e.getSlot()+1].contains("P:")) {
                    String p = ReportInventory.reports[e.getSlot()+1].split("P:")[0];
                    String s = ReportInventory.servers[e.getSlot()+1].split("S:")[0];
                    player.sendMessage("Teleporting to " + p);
                    if (!String.valueOf(player.getServer()).split("=")[1].split(",")[0].equalsIgnoreCase(s)) {
                        player.chat("/server + " + s);
                    }
                    player.chat("/tp " + p);
                }
                //rimuovi il report dal db.
                String p = (ReportInventory.reports[e.getSlot()+1]).split("P:")[0];
                ReportInventory.rmReport(p);
            }
            player.closeInventory();
        }

    }




}
