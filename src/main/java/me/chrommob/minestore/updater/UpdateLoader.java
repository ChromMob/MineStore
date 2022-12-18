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
            if (!isWindows() && replaceFile()) {
                Bukkit.getLogger().info("File replaced successfully. So it will not be needed to be redownloaded on restart.");
            } else {
                Bukkit.getLogger().info("File not replaced. You are on Windows or the file is not found.");
            }
            Bukkit.getLogger().info("Update loaded successfully.");
            Bukkit.getPluginManager().enablePlugin(Bukkit.getPluginManager().getPlugin("MineStore"));
        } catch (InvalidPluginException | InvalidDescriptionException e) {
            Bukkit.getLogger().warning("Failed to load plugin!");
        }
    }

    private boolean replaceFile() {
        return pluginFile.renameTo(new File(MineStore.instance.getDataFolder().getParentFile(), "MineStore.jar"));
    }

    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
}
