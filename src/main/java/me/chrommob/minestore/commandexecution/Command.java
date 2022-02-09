package me.chrommob.minestore.commandexecution;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;

public class Command {
    public static HashMap<String, ArrayList<String>> runLater = new HashMap<>();

    public static void online(String command) {
        Bukkit.getScheduler().runTask(Bukkit.getPluginManager().getPlugin("MineStore"), () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        });
    }

    public static void offline(String username, String command) {
        Manager.add(username, command);
    }
}
