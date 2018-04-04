package netty;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * @author lxl lukas
 * @description
 * @create 2018/3/9
 */
public class PlainOioServer {
    static final int PORT = 8007;

    public void start(){
        try {
            final ServerSocket serverSocket = new ServerSocket(PORT);


            for(;;) {
                new Thread(() -> {
                    Socket client = null;
                    try {
                        client = serverSocket.accept();
                        System.out.println("Accepted connection from " + client);
                        OutputStream out = client.getOutputStream();
                        out.write("Hello,Netty!\r\n".getBytes(Charset.forName("UTF-8")));
                        client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        try {
                            client.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        new PlainOioServer().start();
    }
}
