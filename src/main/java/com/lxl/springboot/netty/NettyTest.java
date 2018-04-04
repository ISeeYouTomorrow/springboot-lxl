package com.lxl.springboot.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;

/**
 *
 * Channel：通信通道，对应一个物理连接
 ChannelPipeline：事件处理管道
 ChannelHandler：事件处理器
 ChannelHandlerContext：上下文环境，包含了handler的引用
 * @author lxl lukas
 * @description
 * @create 2018/3/5
 *
 *
 * org
└── jboss
└── netty
├── bootstrap 配置并启动服务的类
├── buffer 缓冲相关类，对NIO Buffer做了一些封装
├── channel 核心部分，处理连接
├── container 连接其他容器的代码
├── example 使用示例
├── handler 基于handler的扩展部分，实现协议编解码等附加功能
├── logging 日志
└── util 工具类
 */
public class NettyTest {

    /**服务端*/
    static final class EchoServer{
        static final boolean SSL = System.getProperty("ssl") != null;
        static final int PORT = 8007;


        public static void start() throws CertificateException, SSLException {
            //构建安全套接字
            final SslContext sslContext;
            if(SSL){
                SelfSignedCertificate ssc = new SelfSignedCertificate();
                sslContext = SslContextBuilder.forServer(ssc.certificate(),ssc.privateKey()).build();
            }else {
                sslContext = null;
            }

            /**
             * 构建server
             */
            EventLoopGroup bossGroup = new NioEventLoopGroup(1);

            EventLoopGroup workerGroup = new NioEventLoopGroup();

            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,100)
                    .handler(new LoggingHandler())
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            if(sslContext != null){
                                p.addLast(sslContext.newHandler(ch.alloc()));
                            }
//                            p.addLast(new EchoServerHandler());
                            p.addLast(new TimeServerHandler());
                        }
                    });

            try {
                /** start the server */
                ChannelFuture f = bootstrap.bind(PORT).sync();
                /** Wait until the server socket is closed. */
                f.channel().closeFuture().sync();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                //  Shut down all event loops to terminate all threads.
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }

        }
    }


    public static void main(String[] args) {
        try {
            EchoServer.start();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (SSLException e) {
            e.printStackTrace();
        }
    }

}

/**
 * 进行业务逻辑处理的EchoServerHandler
 */
class EchoServerHandler extends ChannelInboundHandlerAdapter{
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf)msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String response = new String(req,"utf-8");
        System.out.println("服务端接收到的客户端请求信息是："+response);

        ctx.writeAndFlush(Unpooled.copiedBuffer("服务端返回信息hello".getBytes()))
           .addListener(ChannelFutureListener.CLOSE);

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
