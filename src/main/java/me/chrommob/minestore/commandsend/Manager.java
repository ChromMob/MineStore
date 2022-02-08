package me.chrommob.minestore.commandsend;

import me.chrommob.minestore.storage.PunishmentManager;

import java.util.ArrayList;

import static me.chrommob.minestore.commandsend.Command.runLater;

public class Manager {
    public static void add(String player, String command) {
        ArrayList commands;
        try {
            commands = runLater.get(player);
            commands.add(command);
            runLater.put(player, commands);
            PunishmentManager.update();
        } catch (Exception e) {
            commands = new ArrayList();
            commands.add(command);
            runLater.put(player, commands);
            PunishmentManager.update();
        }
    }
}
