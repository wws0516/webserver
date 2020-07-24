package server;

import http.HttpStatusCode;
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

/**
 * @Author: wws
 * @Date: 2020-07-14 18:53
 */
public class server extends ServerWrapper{

    private RequestHandler requestHandler = new RequestHandler();

//    @Test
//    public void test(){
//        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("web.xml");
//    }

    @Test
    public void run() throws IOException {

        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        ssChannel.configureBlocking(false);
        ssChannel.bind(new InetSocketAddress(9898));
        Selector selector = Selector.open();
        ssChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (selector.select() > 0){
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()){
                SelectionKey sk = it.next();
                if (sk.isAcceptable()){
                    SocketChannel sChannel = ssChannel.accept();
                    System.out.println("dsaf");
                    sChannel.configureBlocking(false);
                    sChannel.register(selector, SelectionKey.OP_READ);

                }else if (sk.isReadable()){
                    sChannel = (SocketChannel) sk.channel();
                    ByteBuffer buf = ByteBuffer.allocate(1024);
                    int len;
                    while ((len = sChannel.read(buf)) > 0){

                        buf.flip();
                        Request req = new Request(new String(buf.array(), 0, len));
                        req.parseRequest();

                        Response response = new Response();
                        requestHandler.handler(req, response);

                        requestHandler.setServerWrapper(this);
                        requestHandler.flushRes(response);

                        buf.clear();
                    }


                }
                it.remove();
            }
        }

    }

    public void start(){

    }


}
