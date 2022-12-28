package me.chrommob.minestore.updater;

import me.chrommob.minestore.MineStore;
import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class UpdateLoader implements Runnable {
    private final File newPluginFile = new File(MineStore.instance.getDataFolder().getParentFile() + File.separator + "MineStore" + File.separator + "temp", "MineStore.jar");
    private final File oldPluginFile = new File(MineStore.instance.getDataFolder().getParentFile() + File.separator + "MineStore.jar");

    public UpdateLoader() {
        this.run();
    }

    @Override
    public void run() {
        Bukkit.getPluginManager().disablePlugin(MineStore.instance);
        try {
            if (!isWindows()) {
                copyFile();
            }
            Bukkit.getPluginManager().loadPlugin(newPluginFile);
            Bukkit.getLogger().info("Update loaded successfully.");
            Bukkit.getPluginManager().enablePlugin(Bukkit.getPluginManager().getPlugin("MineStore"));
        } catch (InvalidPluginException | InvalidDescriptionException e) {
            e.printStackTrace();
            Bukkit.getLogger().warning("Failed to load plugin!");
        }
    }

    private void copyFile() {
        try {
            Files.delete(oldPluginFile.toPath());
            Files.copy(newPluginFile.toPath(), oldPluginFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.getLogger().warning("Failed to copy file!");
        }
    }

    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
}
