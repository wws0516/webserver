package server;

import http.HttpStatusCode;
import http.RequestDispatcher;
import http.RequestHandler;
import http.request.Request;
import http.response.Response;
import org.junit.Test;
import servlet.Servlet;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: wws
 * @Date: 2020-07-14 18:53
 */
public class server extends ServerWrapper{

    private ThreadPoolExecutor threadPoolExecutor;

    private static RequestDispatcher requestDispatcher = new RequestDispatcher();
//    private RequestHandler requestHandler = new RequestHandler();

    @Test
    public void run() throws IOException {

        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        ssChannel.configureBlocking(false);
        ssChannel.bind(new InetSocketAddress(9898));
        Selector selector = Selector.open();
        ssChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (selector.select() > 0) {
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey sk = it.next();

                if (sk.isAcceptable()) {
                    SocketChannel sChannel = ssChannel.accept();
                    sChannel.configureBlocking(false);
                    sChannel.register(selector, SelectionKey.OP_READ);

                } else if (sk.isReadable()) {
                    sChannel = (SocketChannel) sk.channel();

                    try {
                        requestDispatcher.doDispatch(this);
                    }catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                it.remove();
            }
        }
    }




    public void start(){

    }


}
