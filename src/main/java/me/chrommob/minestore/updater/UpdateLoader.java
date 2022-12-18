package me.chrommob.minestore.updater;

import me.chrommob.minestore.MineStore;
import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;

import java.io.File;

public class UpdateLoader implements Runnable {
    private final File pluginFile = new File(MineStore.instance.getDataFolder().getParentFile() + File.separator + "MineStore" + File.separator + "temp", "MineStore.jar");

    public UpdateLoader() {
        this.run();
    }

    @Override
    public void run() {
        Bukkit.getPluginManager().disablePlugin(MineStore.instance);
        try {
            Bukkit.getPluginManager().loadPlugin(pluginFile);
            Bukkit.getLogger().info("Update loaded successfully.");
            Bukkit.getPluginManager().enablePlugin(Bukkit.getPluginManager().getPlugin("MineStore"));
        } catch (InvalidPluginException | InvalidDescriptionException e) {
            e.printStackTrace();
            Bukkit.getLogger().warning("Failed to load plugin!");
        }
    }
}
