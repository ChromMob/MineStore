package me.chrommob.minestore.util;

import me.chrommob.minestore.MineStore;
import me.chrommob.minestore.data.Config;
import me.chrommob.minestore.gui.BuyListener;
import me.chrommob.minestore.gui.Currency;
import me.chrommob.minestore.mysql.MySQLData;
import me.chrommob.minestore.mysql.connection.ConnectionPool;
import me.chrommob.minestore.placeholders.listener.DonationGoalListener;
import me.chrommob.minestore.placeholders.listener.LastDonatorListener;
import me.chrommob.minestore.placeholders.listener.TopDonoListener;
import me.chrommob.minestore.placeholders.objects.DonationGoal;
import me.chrommob.minestore.placeholders.objects.LastDonator;
import me.chrommob.minestore.weblistener.Listener;
import org.bukkit.Bukkit;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//Runnable to execute plugin functions
public class Runnable {
    public static void runListener(String load) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(MineStore.instance, () -> {
            if (Config.isDebug()) {
                MineStore.instance.getLogger().info("Runnable.java runListener " + load);
            }
            if (load.equalsIgnoreCase("web")) {
                for (int i = 0; i < Config.getApiUrl().size(); i++) {
                    if (Config.getEmpty().get(i)) {
                        Listener.run(i);
                    }
                }
            }
            if (Config.isGuiEnabled()) {
                BuyListener.run();
                Currency.run();
            }
            if (Config.isPlaceholderPresent()) {
                TopDonoListener.run();
                DonationGoalListener.run();
                LastDonatorListener.run();
            }
            if (Config.isVaultPresent() && MySQLData.isEnabled()) {
                MineStore.instance.getUserManager().updateAll();
                ConnectionPool.updateTable();
            }
        }, 0, 30 * 20);
        Bukkit.getScheduler().runTaskTimerAsynchronously(MineStore.instance, () -> {
            if (load.equalsIgnoreCase("web")) {
                for (int i = 0; i < Config.getApiUrl().size(); i++) {
                    if (!Config.getEmpty().get(i)) {
                        Listener.run(i);
                    }
                }
            }
        }, 0, 2*20);
    }
}
