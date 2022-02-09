package me.chrommob.minestore.commands;

import me.chrommob.minestore.MineStore;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import static me.chrommob.minestore.commandexecution.Command.runLater;

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
            yaml.dump(runLater, writer);

        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void get(){
        try {
            InputStream inputStream = new FileInputStream(later);
            runLater = (HashMap<String, ArrayList<String>>) yaml.load(inputStream);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
