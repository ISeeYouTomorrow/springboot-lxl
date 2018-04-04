package com.lxl.springboot.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.util.CharsetUtil;

import javax.net.ssl.SSLException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * @author lxl lukas
 * @description
 * @create 2018/3/6
 */
public class EchoClient {
    static final boolean SSL = System.getProperty("ssl") != null;
    static final String HOST =  System.getProperty("host","127.0.0.1");
    static final int PORT = 8007;
    static final int SIZE = 256;

    public static void main(String[] args) throws SSLException {
        final SslContext sslContext;

        if(SSL){
            sslContext = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        }else {
            sslContext = null;
        }

        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();//bootstrap 启动入口
        b.group(group)
         .channel(NioSocketChannel.class)
         .option(ChannelOption.TCP_NODELAY,true)
         .handler(new ChannelInitializer<SocketChannel>() {
             @Override
             protected void initChannel(SocketChannel ch) throws Exception {
                 ChannelPipeline p = ch.pipeline();
                 if (sslContext != null) {
                     p.addLast(sslContext.newHandler(ch.alloc(), HOST, PORT));
                 }
                 //p.addLast(new LoggingHandler(LogLevel.INFO));
                 p.addLast(new EchoClientHandler());
             }
         });

        try {
            ChannelFuture f = b.connect(HOST,PORT).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

   static class EchoClientHandler extends ChannelInboundHandlerAdapter {
        private ByteBuf firstMessage;

        public EchoClientHandler(){
            firstMessage = Unpooled.buffer(SIZE);
//            for (int i=0;i<firstMessage.capacity();i++){
//                firstMessage.writeByte((byte)i);
//            }
            firstMessage.writeBytes("我是客户端".getBytes());
        }

       @Override
       public void channelActive(ChannelHandlerContext ctx) throws Exception {

           System.out.println("Connected");
           ctx.writeAndFlush(Unpooled.copiedBuffer(("This message from client "+new Date().toString()).getBytes(CharsetUtil.UTF_8)));
       }

       @Override
       public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
           try {
               ByteBuf buf = (ByteBuf)msg;
               byte[] req = new byte[buf.readableBytes()];
               buf.readBytes(req);
               String response = new String(req,"utf-8");
               System.out.println("收到服务端返回的消息"+response);
           } catch (UnsupportedEncodingException e) {
               e.printStackTrace();
           }
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
}
