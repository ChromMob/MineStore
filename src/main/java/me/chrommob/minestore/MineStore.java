package me.chrommob.minestore;

import co.aikar.commands.PaperCommandManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import me.chrommob.minestore.command.Buy;
import me.chrommob.minestore.command.Reload;
import me.chrommob.minestore.command.Store;
import me.chrommob.minestore.data.Config;
import me.chrommob.minestore.gui.Event;
import me.chrommob.minestore.placeholders.PlaceholderHook;
import me.chrommob.minestore.storage.PunishmentManager;
import me.chrommob.minestore.listeners.JoinQuitListener;
import me.chrommob.minestore.util.Mode;
import me.chrommob.minestore.util.Runnable;
import me.chrommob.minestore.websocket.Socket;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class MineStore extends JavaPlugin {
    public static MineStore instance;
    Mode mode = Mode.WEBSOCKET;

    @Override
    public void onLoad() {
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        Metrics metrics = new Metrics(this, 14043);
        dependencyCheck();
        PluginManager plManager = Bukkit.getPluginManager();
        instance = this;
        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new Reload());
        plManager.registerEvents(new JoinQuitListener(), this);
        plManager.registerEvents(new Event(this), this);
        PunishmentManager.create();
        PunishmentManager.get();
        loadConfig();
        if (getConfig().getString("mode").equalsIgnoreCase("WEBSOCKET")) {
            mode = Mode.WEBSOCKET;
        } else {
            mode = Mode.WEBLISTENER;
        }
        Config.setStoreEnabled(getConfig().getBoolean("store-enabled"));
        Config.setPort(getConfig().getInt("port"));
        Config.setGuiEnabled(getConfig().getBoolean("gui-enabled"));
        if (mode == Mode.WEBSOCKET) {
            Runnable.runListener("NO");
            try {
                NettyServer(Config.getPort());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Runnable.runListener("web");
            Bukkit.getLogger().info("[MineStore] Starting web listener...");
        }
        if (Config.isGuiEnabled()) {
            manager.registerCommand(new Buy(this));
            Bukkit.getLogger().info("[MineStore] Starting gui listener...");
        }
        if (Config.isStoreEnabled()) {
            manager.registerCommand(new Store());
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void dependencyCheck() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            Bukkit.getLogger().info("[MineStore] PlaceholderAPI found!");
            Config.setPlaceholderPresent(false);
        } else {
            Config.setPlaceholderPresent(true);
            new PlaceholderHook(this).register();
        }
    }

    private void NettyServer(int port) throws Exception
    {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
            getLogger().info("Starting Server at " + port);

            serverBootstrap.group(eventLoopGroup, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new Socket())
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            serverBootstrap.bind(port).sync();
    }

    public void loadConfig() {
        reloadConfig();
        Config.setPassword(getConfig().getString("password"));
        Config.setApiUrl(getConfig().getString("store-api"));
        Config.setStoreMessage(getConfig().getString("store-message"));
        Config.setSecretKey(getConfig().getString("secret-key"));
        Config.setGuiName(getConfig().getString("settings.name"));
        Config.setPackageMessage(getConfig().getString("settings.message"));
        Config.setBuyUrl(getConfig().getString("settings.api-url"));
        Config.setItemName(getConfig().getString("format.item-name"));
        Config.setItemDescription(getConfig().getString("format.item-description"));
        Config.setItemPrice(getConfig().getString("format.item-price"));
        Config.setApiKey(getConfig().getInt("api-key"));
    }
}