package me.chrommob.minestore.websocket;

import com.google.gson.Gson;
import io.netty.channel.SimpleChannelInboundHandler;
import me.chrommob.minestore.commandsend.Command;
import me.chrommob.minestore.data.Config;
import me.chrommob.minestore.websocket.objects.SocketObjects;
import org.bukkit.Bukkit;

public class SocketHandler extends SimpleChannelInboundHandler<String> {

    private final Gson gson = new Gson();


    @Override
    protected void channelRead0(io.netty.channel.ChannelHandlerContext ctx, String msg) {
        final SocketObjects data = gson.fromJson(msg, SocketObjects.class);
        String commandWithoutPrefix = data.getCommand().replaceFirst("/", "");
        if (!data.getPassword().equalsIgnoreCase(Config.getPassword())) {
            Bukkit.getLogger().info("[MineStore] Wrong password!");
            return;
        }
        if (Bukkit.getPlayer(data.getUsername()) == null && data.isPlayerOnlineNeeded()) {
            Command.offline(data.getUsername(), commandWithoutPrefix);
        } else {
            Command.online(commandWithoutPrefix);
        }
    }
}
