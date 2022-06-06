package me.chrommob.minestore.websocket;

import com.google.gson.Gson;
import io.netty.channel.SimpleChannelInboundHandler;
import me.chrommob.minestore.MineStore;
import me.chrommob.minestore.commandexecution.Command;
import me.chrommob.minestore.data.Config;
import me.chrommob.minestore.websocket.objects.SocketObjects;
import org.spongepowered.api.Sponge;

public class SocketHandler extends SimpleChannelInboundHandler<String> {

    private final Gson gson = new Gson();

    @Override
    protected void channelRead0(io.netty.channel.ChannelHandlerContext ctx, String msg) {
        final SocketObjects data = gson.fromJson(msg, SocketObjects.class);
        //Removing "/" from the command
        String commandWithoutPrefix = data.getCommand().replaceFirst("/", "");
        commandWithoutPrefix = commandWithoutPrefix.replaceFirst("   ", " ");
        //Checking if the password is correct
        if (!data.getPassword().equalsIgnoreCase("")) {
            MineStore.instance.getLogger().info("[MineStore] Wrong password!");
            return;
        }
        //Executing the command
        if (!Sponge.getServer().getPlayer(data.getUsername()).isPresent() && data.isPlayerOnlineNeeded()) {
            Command.offline(data.getUsername(), commandWithoutPrefix);
        } else {
            Command.online(commandWithoutPrefix);
        }
    }
}
