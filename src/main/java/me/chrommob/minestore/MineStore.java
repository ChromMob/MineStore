package me.chrommob.minestore;

import co.aikar.commands.PaperCommandManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;
import me.chrommob.minestore.authorization.AuthManager;
import me.chrommob.minestore.commands.*;
import me.chrommob.minestore.data.Config;
import me.chrommob.minestore.gui.Event;
import me.chrommob.minestore.mysql.MySQLData;
import me.chrommob.minestore.mysql.connection.ConnectionPool;
import me.chrommob.minestore.mysql.data.UserManager;
import me.chrommob.minestore.placeholders.PlaceholderHook;
import me.chrommob.minestore.commandexecution.JoinQuitListener;
import me.chrommob.minestore.util.Mode;
import me.chrommob.minestore.util.Runnable;
import me.chrommob.minestore.websocket.Socket;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public final class MineStore extends JavaPlugin {
    @Getter
    UserManager userManager;
    public static MineStore instance;
    Mode mode = Mode.WEBSOCKET;

    @Override
    public void onLoad() {
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        instance = this;
        Metrics metrics = new Metrics(this, 14043);
        new AuthManager();
        dependencyCheck();
        PluginManager plManager = Bukkit.getPluginManager();
        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new Reload());
        manager.registerCommand(new Verify());
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
        MySQLData.setEnabled(getConfig().getBoolean("mysql.enabled"));
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
        if (MySQLData.isEnabled() && Config.isVaultPresent()) {
            new ConnectionPool(this);
            ConnectionPool.createTable();
            userManager = new UserManager();
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
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            Bukkit.getLogger().info("[MineStore] Vault found!");
            Config.setVaultPresent(false);
        }
        else {
            Config.setVaultPresent(true);
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
        MySQLData.setIp(getConfig().getString("mysql.ip"));
        MySQLData.setPort(getConfig().getInt("mysql.port"));
        MySQLData.setUser(getConfig().getString("mysql.username"));
        MySQLData.setPassword(getConfig().getString("mysql.password"));
        MySQLData.setDatabase(getConfig().getString("mysql.database"));
        Config.setPassword(getConfig().getString("password"));
        Config.getApiUrl().add(0, getConfig().getString("store-api"));
        Config.getEmpty().add(0, false);
        String url = getConfig().getString("store-api");
        for (int i = 1; url != null; i++){
            url = getConfig().getString("store-api-" + i);
            if (url != null) {
                Config.getApiUrl().add(i, url);
                Config.getEmpty().add(i, false);
            }
        }
        Config.getApiKey().add(0, getConfig().getString("api-key"));
        String key = getConfig().getString("api-key");
        for (int i = 1; key != null; i++){
            key = getConfig().getString("api-key-" + i);
            if (key != null) {
                Config.getApiKey().add(i, key);
            }
        }
        Config.setStoreMessage(getConfig().getString("store-message"));
        Config.getSecretKey().add(getConfig().getString("secret-key"));
        String secret = getConfig().getString("secret-key");
        for (int i = 1; secret != null; i++){
            secret = getConfig().getString("secret-key-" + i);
            if (secret != null) {
                Config.getSecretKey().add(i, secret);
            }
        }
        Config.setGuiName(getConfig().getString("settings.name"));
        Config.setPackageMessage(getConfig().getString("settings.message"));
        Config.setBuyUrl(getConfig().getString("settings.api-url"));
        Config.setItemName(getConfig().getString("format.item-name"));
        Config.setItemDescription(getConfig().getString("format.item-description"));
        Config.setItemPrice(getConfig().getString("format.item-price"));
        Config.setAuthMessage(getConfig().getString("auth.message"));
        Config.setAuthDelay(getConfig().getDouble("auth.time"));
        Config.setAuthSuccessful("auth.successful");
        Config.setAuthFailed("auth.failed");
    }
}