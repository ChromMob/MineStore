package me.chrommob.minestore.commandexecution;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;

import java.util.ArrayList;
import java.util.HashMap;

public class Command {
    public static HashMap<String, ArrayList<String>> runLater;

    public static void online(String command) {
        Sponge.getCommandManager().process(Sponge.getServer().getConsole(), command);
    }

    public static void offline(String username, String command) {
        Manager.add(username, command);
    }
}
