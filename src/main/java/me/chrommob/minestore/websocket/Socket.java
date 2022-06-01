package me.chrommob.minestore.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class Socket extends ChannelInitializer<SocketChannel> {

    /**
     * Netty Server init
     * @param socket
     * @throws Exception
     */
    @Override
    protected void initChannel(SocketChannel socket) throws Exception {
        socket.pipeline().addLast(new StringEncoder());
        socket.pipeline().addLast(new StringDecoder());
        socket.pipeline().addLast(new SocketHandler());
    }
}