package me.chrommob.minestore.updater;

import me.chrommob.minestore.MineStore;
import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class UpdateLoader implements Runnable {
    private final File pluginFile = new File(MineStore.instance.getDataFolder().getParentFile() + File.separator + "MineStore" + File.separator + "temp", "MineStore.jar");

    public UpdateLoader() {
        this.run();
    }

    @Override
    public void run() {
        Bukkit.getPluginManager().disablePlugin(MineStore.instance);
        try {
            if (!isWindows() && copyFile()) {
                Bukkit.getLogger().info("Copied new plugin file to plugins directory.");
            } else {
                Bukkit.getPluginManager().loadPlugin(pluginFile);
            }
            Bukkit.getLogger().info("Update loaded successfully.");
            Bukkit.getPluginManager().enablePlugin(Bukkit.getPluginManager().getPlugin("MineStore"));
        } catch (InvalidPluginException | InvalidDescriptionException e) {
            e.printStackTrace();
            Bukkit.getLogger().warning("Failed to load plugin!");
        }
    }

    private boolean copyFile() {
        try {
            if (pluginFile.exists()) {
                Files.delete(pluginFile.toPath());
            }
            Files.copy(pluginFile.toPath(), new File(MineStore.instance.getDataFolder().getParentFile(), "MineStore.jar").toPath());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
}
