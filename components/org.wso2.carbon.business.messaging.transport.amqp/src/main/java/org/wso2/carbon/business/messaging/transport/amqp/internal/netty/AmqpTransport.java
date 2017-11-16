package org.wso2.carbon.business.messaging.transport.amqp.internal.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Netty based AMQP server implementation
 */
public class AmqpTransport {
    private static final Logger LOGGER = LoggerFactory.getLogger(AmqpTransport.class);

    private NioEventLoopGroup eventLoopGroup;

    private static final ChannelInitializer<SocketChannel> CHANNEL_INITIALIZER
            = new ChannelInitializer<SocketChannel>() {
        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                @Override
                public void channelRead(ChannelHandlerContext ctx, Object msg) {
                    ByteBuf in = (ByteBuf) msg;
                    LOGGER.info("Server received: " + in.toString(CharsetUtil.UTF_8));
                    ctx.write(in);
                }

                @Override
                public void channelReadComplete(ChannelHandlerContext ctx) {
                    ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
                }

                @Override
                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                    LOGGER.error("Error while receiving message", cause);
                    ctx.close();
                }
            });
        }
    };

    public void start() {
        eventLoopGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(eventLoopGroup)
                     .channel(NioServerSocketChannel.class)
                     .localAddress(5673)
                     .childHandler(CHANNEL_INITIALIZER);
            bootstrap.bind().sync();
        } catch (InterruptedException e) {
            LOGGER.error("Error while creating server socket", e);
        }

    }

    public void stop() {
        try {
            eventLoopGroup.shutdownGracefully().sync();
        } catch (InterruptedException e) {
            LOGGER.error("Error while shutting down AMQP server socket");
        }
    }
}
