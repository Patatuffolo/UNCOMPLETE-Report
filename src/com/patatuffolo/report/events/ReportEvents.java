package com.patatuffolo.report.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ReportEvents implements Listener {

    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent evt) {
        Player p = evt.getPlayer();
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            player.sendMessage(ChatColor.GREEN + "[+] " + p.getName());
        }
    }
}
