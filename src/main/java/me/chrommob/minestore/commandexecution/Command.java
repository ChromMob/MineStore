package me.chrommob.minestore.commandexecution;

import me.chrommob.minestore.MineStore;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;

public class Command {
    public static HashMap<String, ArrayList<String>> runLater;

    public static void online(String command) {
        Bukkit.getScheduler().runTask(MineStore.instance, () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        });
    }

    public static synchronized void offline(String username, String command) {
        Manager.add(username, command);
    }
}
