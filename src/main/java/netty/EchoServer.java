package netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author lxl lukas
 * @description
 * @create 2018/3/8
 */
public class EchoServer {
    static final int PORT = 8007;


    public static void main(String[] args) {

        try {
            new EchoServer().start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void start() throws InterruptedException {
        EventLoopGroup server = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(server)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(PORT))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline cp = ch.pipeline();
                        cp.addLast(new EchoServerHandler());
                    }
                });
        try {
            ChannelFuture future = serverBootstrap.bind().sync();
            System.out.println(EchoServer.class.getName()+" started and listen on "+future.channel().localAddress());
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            server.shutdownGracefully().sync();
        }


    }

}
