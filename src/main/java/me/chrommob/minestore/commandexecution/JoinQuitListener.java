package me.chrommob.minestore.commandexecution;

import me.chrommob.minestore.MineStore;
import me.chrommob.minestore.commands.PunishmentManager;
import me.chrommob.minestore.data.Config;
import me.chrommob.minestore.mysql.MySQLData;
import me.chrommob.minestore.mysql.data.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

import static me.chrommob.minestore.commandexecution.Command.runLater;


public class JoinQuitListener implements Listener {
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        try {
            String name = event.getPlayer().getName();
            if (runLater.get(name).isEmpty()) {
                runLater.remove(name);
            } else {
                Bukkit.getScheduler().runTask(Bukkit.getPluginManager().getPlugin("MineStore"), () -> {
                    runLater.get(name).forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
                    runLater.remove(name);
                    PunishmentManager.update();
                });
            }
        } catch (Exception ignored) {
        }
        if (Config.isVaultPresent() && MySQLData.isEnabled()) {
            String name = event.getPlayer().getName();
            UUID uuid = event.getPlayer().getUniqueId();
            MineStore.instance.getUserManager().createProfile(uuid, name);
        }
    }
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        if (Config.isVaultPresent() && MySQLData.isEnabled()) {
            MineStore.instance.getUserManager().removeProfile(event.getPlayer().getUniqueId());
        }
    }
}
