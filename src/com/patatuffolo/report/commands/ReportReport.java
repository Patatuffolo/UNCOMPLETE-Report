package com.patatuffolo.report.commands;

import com.patatuffolo.report.events.InventoryEvents;
import com.patatuffolo.report.inventories.ReportInventory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.*;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;

import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class ReportReport implements CommandExecutor {

    Inventory inv = Bukkit.createInventory(null, 54, "Report GUI");
    Connection connection;
    Player player;
    String[] staff = {"Patatuffolo", "PandaWurst", "TheRealGiov22"};

    public boolean reportPlayer(String reporter, String playerReported, String reason) {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://sql11.freemysqlhosting.net/sql11423347?user=sql11423347&password=YD8XngrtfL");
            String statement = "INSERT INTO plugin_reports (Reporter, PlayerReported, Reason, Server) VALUES ('" + reporter + "', '" + playerReported + "', '" + reason + "', 'Vanilla')";
            writeSt(statement);
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public boolean writeSt(String statement)
    {
        try {
            Statement st = connection.createStatement();
            st.executeUpdate(statement);

            return true;
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public boolean readSt(String statement) {
        int i = 0;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://sql11.freemysqlhosting.net/sql11423347?user=sql11423347&password=YD8XngrtfL");
            PreparedStatement st = connection.prepareStatement("SELECT * FROM plugin_reports");
            ResultSet results = st.executeQuery();
            while (results.next()) {
                i++;
                player.sendMessage(ChatColor.DARK_RED + "\nNuovo Report:"
                        + ChatColor.YELLOW + "\nID: " + i + "\n"
                        + ChatColor.RED + "Player -> " + results.getString("PlayerReported") + ChatColor.WHITE + "\n"
                        + ChatColor.LIGHT_PURPLE + "Reason -> " + results.getString("Reason") + ChatColor.WHITE + "\n"
                        + ChatColor.GREEN + "Server: " + results.getString("Server")
                        + "\n\n");

            }
            return true;
        } catch (SQLException throwables) {
            player.sendMessage(ChatColor.RED + throwables.toString());
            return false;
        }
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) { return true; }

        player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("report")) {
            //Check player permissions
            //Default permissions:
            boolean isStaff = false;

            for (int i = 0; i < staff.length; i++) {
                if (staff[i].equals(player.getName().toString()))
                {
                    isStaff = true;
                    break;
                }
            }
            if (!isStaff) {
                if (args.length != 2) {
                    player.sendMessage(ChatColor.RED + "[Report] Hai sbagliato qualcosa. Prova /report <Player> <Motivo (una parola)>");
                }
                else {
                    Player player1 = Bukkit.getServer().getPlayer(args[0]);
                    if (player1 == null) {
                        player.sendMessage(ChatColor.RED + "[Report] Il player deve essere online...");
                    }
                    else {
                        if (!player.getName().equals(args[0])) {
                            if (InventoryEvents.nr < 54) {
                                if (reportPlayer(player.getName().toString(), args[0] , args[1])) {
                                    InventoryEvents.nr++;
                                    player.sendMessage(ChatColor.GREEN + "Hai reportato il player " + player1.getName().toString() + "\nMotivazione: " + args[1]);
                                    for (Player player2 : Bukkit.getServer().getOnlinePlayers()) {
                                        for (int i = 0; i < staff.length; i++) {
                                            if (player2.getName().equals(staff[i])) {
                                                player2.sendMessage(player.getName() + " ha reportato " + player1.getName() + " per " + args[1]);
                                            }
                                        }
                                    }
                                }
                                else
                                {
                                    player.sendMessage(ChatColor.RED + "Errore");
                                }
                            }
                            else {
                                player.sendMessage("Lo staff sta già provvedendo a verificare i report di altri player, per favore riprova tra poco");
                            }
                        }
                        else {
                            player.sendMessage(ChatColor.RED + "Non puoi reportare te stesso!");
                        }
                    }
                }
            }
            else {
                //readSt("SELECT * FROM plugin_reports");
                //player.sendMessage(String.valueOf(player.getServer()).split("=")[1].split(",")[0]);
                ReportInventory gui = new ReportInventory();
                player.openInventory(gui.getInventory());
                player.sendMessage(ChatColor.BLUE + "Reports to solve: " + InventoryEvents.nr);
            }
        }
        return true;
    }
}
