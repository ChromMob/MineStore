package me.chrommob.minestore.commandexecution;

import me.chrommob.minestore.MineStore;
import me.chrommob.minestore.commands.PunishmentManager;
import me.chrommob.minestore.data.Config;
import org.apache.commons.lang.ObjectUtils;

import java.util.ArrayList;

import static me.chrommob.minestore.commandexecution.Command.runLater;

public class Manager {
    public static void add(String player, String command) {
        if (Config.isDebug()) {
            MineStore.instance.getLogger().info("Manager.java add " + player + " " + command);
        }
        ArrayList<String> commands;
        try {
            commands = runLater.get(player);
            commands.add(command);
            runLater.put(player, commands);
            PunishmentManager.update();
        } catch (Exception e) {
            commands = new ArrayList<String>();
            commands.add(command);
            runLater.put(player, commands);
            PunishmentManager.update();
        }
    }
}
