package com.patatuffolo.report;

import com.patatuffolo.report.commands.ReportReport;
import com.patatuffolo.report.events.InventoryEvents;
import com.patatuffolo.report.events.ReportEvents;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Report extends JavaPlugin {

    public static LuckPerms api;

    @Override
    public void onEnable() {

        getServer().getPluginManager().registerEvents(new ReportEvents(), this);
        getServer().getPluginManager().registerEvents(new InventoryEvents(), this);
        getCommand("report").setExecutor(new ReportReport());
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[Report] Plugin Enabled");

    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[Report] Plugin Disabled");
    }

}
