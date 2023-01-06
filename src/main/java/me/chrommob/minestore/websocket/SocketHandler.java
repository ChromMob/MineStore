package me.chrommob.minestore.websocket;

import com.google.gson.Gson;
import io.netty.channel.SimpleChannelInboundHandler;
import me.chrommob.minestore.MineStore;
import me.chrommob.minestore.commandexecution.Command;
import me.chrommob.minestore.data.Config;
import me.chrommob.minestore.websocket.objects.SocketObjects;
import org.bukkit.Bukkit;

import java.lang.reflect.Method;

public class SocketHandler extends SimpleChannelInboundHandler<String> {

    private final Gson gson = new Gson();

    @Override
    protected void channelRead0(io.netty.channel.ChannelHandlerContext ctx, String msg) {
        final SocketObjects data = gson.fromJson(msg, SocketObjects.class);
        if (Config.isDebug()) {
            StringBuilder sb = new StringBuilder();
            for (Method method : data.getClass().getMethods()) {
                if (method.getName().startsWith("get") && !method.getName().equals("getClass")) {
                    try {
                        sb.append(method.getName().substring(3)).append(": ").append(method.invoke(data).toString()).append(" ");
                    } catch (Exception ignored) {
                    }
                }
            }
            MineStore.instance.getLogger().info("SocketHandler.java " + sb);
        }
        //Removing "/" from the command
        String commandWithoutPrefix = data.getCommand().replaceFirst("/", "");
        commandWithoutPrefix = commandWithoutPrefix.replaceFirst("   ", " ");
        //Checking if the password is correct
        if (!data.getPassword().equalsIgnoreCase(Config.getPassword())) {
            Bukkit.getLogger().info("[MineStore] Wrong password!");
            return;
        }
        //Executing the command
        if (Bukkit.getPlayer(data.getUsername()) == null && data.isPlayerOnlineNeeded()) {
            Command.offline(data.getUsername(), commandWithoutPrefix);
        } else {
            Command.online(commandWithoutPrefix);
        }
    }
}
