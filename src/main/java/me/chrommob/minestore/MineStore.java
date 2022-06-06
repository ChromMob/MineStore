package me.chrommob.minestore;

import com.google.inject.Inject;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;
import me.chrommob.minestore.commandexecution.JoinQuitListener;
import me.chrommob.minestore.commands.PunishmentManager;
import me.chrommob.minestore.data.Config;
import me.chrommob.minestore.weblistener.WebListener;
import me.chrommob.minestore.websocket.Socket;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Plugin(id = "minestore", name = "MineStore", version = "1.2.0")
public class MineStore {
    @Inject private Logger logger;
    public static MineStore instance;
    private WebListener webListener;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private Path defaultConfig;

    private Map<String, Object> config = new HashMap<>();

    @Getter
    private Config configData;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        instance = this;
        webListener = new WebListener();
        logger.info("Starting MineStore...");
        logger.info("Loading config...");
        PunishmentManager.create();
        PunishmentManager.get();

        Sponge.getEventManager().registerListeners(this, new JoinQuitListener());

        createConfig();
        //startListener();
        //startSocket();
    }

    private void createConfig(){
        File file = new File(privateConfigDir + "/config.yml");
        if (file.exists()){
            logger.info("Config file found.");
            try {
                InputStream inputStream = Files.newInputStream(new File(privateConfigDir + "/config.yml").toPath());
                Yaml yaml = new Yaml(new Constructor(Config.class));
                configData = (Config) yaml.load(inputStream);
                logger.info("Config loaded.");
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            logger.info("Config file not found. Creating new one.");
            configData = new Config();
            configData.setApi_link("https://example.yourdomain.com/api/");
            configData.setApi_key("api_key");
            configData.setMode("dev");
            configData.setSecret_key("secret_key");
            try {
                file.createNewFile();
                PrintWriter writer = new PrintWriter(privateConfigDir + "/config.yml");
                Yaml yaml = new Yaml();
                yaml.dump(configData, writer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        startListener();
    }

    /*private void startSocket() {
        int port = Config.getPort();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        getLogger().info("Starting Server at " + port);

        serverBootstrap.group(eventLoopGroup, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new Socket())
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        try {
            serverBootstrap.bind(port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }*/

    private void startListener() {
        new Thread( () -> {
            while (true) {
                if (webListener.run()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Getter
    @Inject
    @ConfigDir(sharedRoot = false)
    private Path privateConfigDir;

    public Logger getLogger() {
        return logger;
    }
}