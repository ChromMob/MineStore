package me.chrommob.minestore.commandexecution;

import me.chrommob.minestore.commands.PunishmentManager;

import java.util.ArrayList;

import static me.chrommob.minestore.commandexecution.Command.runLater;

public class Manager {
    public static void add(String player, String command) {

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
