package netty.talk;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;

import java.net.InetSocketAddress;

/**
 * @author lxl lukas
 * @description
 * @create 2018/3/14
 */
public class ChatServer {

    private final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
    private final EventLoopGroup loopGroup = new NioEventLoopGroup();
    private Channel channel;


    public ChannelFuture start(InetSocketAddress address) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(loopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(createInitializer(channelGroup));

        ChannelFuture future = serverBootstrap.bind(address);
        future.syncUninterruptibly();
        channel = future.channel();
        return future;
    }


    protected ChannelInitializer<Channel> createInitializer(ChannelGroup group) {        //3
        return new ChatServerInitializer(group);
    }

    public void destory() {
        if(null != channel){
            channel.close();
        }

        channelGroup.close();
        loopGroup.shutdownGracefully();
    }

    public static void main(String[] args) {
        final ChatServer server = new ChatServer();
        ChannelFuture channelFuture = server.start(new InetSocketAddress(8080));
        Runtime.getRuntime().addShutdownHook(new Thread(() -> server.destory()));
        channelFuture.channel().closeFuture().syncUninterruptibly();
    }

}
