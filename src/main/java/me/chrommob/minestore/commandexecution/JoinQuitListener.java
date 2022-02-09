package me.chrommob.minestore.commandexecution;

import me.chrommob.minestore.commands.PunishmentManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static me.chrommob.minestore.commandexecution.Command.runLater;


public class JoinQuitListener implements Listener {
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        String name = event.getPlayer().getName();
        try {
            if (runLater.get(name).isEmpty()) {
                runLater.remove(name);
            } else {
                Bukkit.getScheduler().runTask(Bukkit.getPluginManager().getPlugin("MineStore"), () -> {
                    runLater.get(name).forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
                    runLater.remove(name);
                    System.out.print(runLater);
                    PunishmentManager.update();
                });
            }
        } catch (Exception ignored) {
        }
    }

}
