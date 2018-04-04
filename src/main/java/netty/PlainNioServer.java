package netty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author lxl lukas
 * @description
 * @create 2018/3/9
 */
public class PlainNioServer {
    public static void main(String[] args) {
        new PlainNioServer().start();
    }
    static final int PORT = 8007;
    public void start(){
        try {
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            Selector selector = Selector.open();
            ssc.register(selector, SelectionKey.OP_ACCEPT);

            ServerSocket ss = ssc.socket();
            ss.bind(new InetSocketAddress(PORT));
            final ByteBuffer msg = ByteBuffer.wrap("Hi!\r\n".getBytes());
            for (;;) {
                selector.select();

                Set<SelectionKey> keys = selector.keys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while(iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    iterator.remove();


                    try {
                        if(key.isAcceptable()){
                            ServerSocketChannel server = (ServerSocketChannel) key.channel();
                            SocketChannel client = server.accept();
                            client.configureBlocking(false);
                            client.register(selector,SelectionKey.OP_READ|SelectionKey.OP_WRITE,msg.duplicate());
                            System.out.println("Accepted connection from "+client);
                        }

                        if(key.isWritable()){
                            SocketChannel client = (SocketChannel) key.channel();
                            ByteBuffer bb = (ByteBuffer)key.attachment();
                            while(bb.hasRemaining()){
                                if(client.write(bb) == 0){
                                    break;
                                }
                            }
                            client.close();
                        }
                    } catch (IOException e) {
                        key.cancel();
                        key.channel().close();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

        }

    }


}
