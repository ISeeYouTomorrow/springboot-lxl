package netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author lxl lukas
 * @description
 * @create 2018/3/8
 */
public class EchoClient {
    static final String HOST = "localhost";
    static final int PORT = 8007;
    public static void main(String[] args) {
        try {
            new EchoClient().start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void start() throws InterruptedException {

        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(HOST,PORT))
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new EchoClientHandler());
                    }
                });
        try {
            ChannelFuture future = bootstrap.connect().sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully().sync();
        }


    }
}
