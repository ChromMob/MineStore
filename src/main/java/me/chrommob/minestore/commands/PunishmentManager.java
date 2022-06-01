package me.chrommob.minestore.commands;

import me.chrommob.minestore.MineStore;
import me.chrommob.minestore.commandexecution.Command;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class PunishmentManager extends JavaPlugin {
    static File later = new File(MineStore.instance.getDataFolder()+"/later.yml");
    static Yaml yaml = new Yaml();
    public static void create() {
        try {
            later.createNewFile();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void update(){
        try {
            PrintWriter writer = new PrintWriter(later);
            yaml.dump(Command.runLater, writer);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void get(){
        try {
            InputStream inputStream = new FileInputStream(later);
            try {
                Command.runLater = (HashMap<String, ArrayList<String>>) yaml.load(inputStream);
            } catch (Exception e){
                e.printStackTrace();
            }
            if (Command.runLater == null){
                Command.runLater = new HashMap<>();
                Bukkit.getLogger().info("[MineStore] Command file empty.");
            } else {
                Bukkit.getLogger().info("[MineStore] Loaded commands for " + Command.runLater.size() + " players.");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
