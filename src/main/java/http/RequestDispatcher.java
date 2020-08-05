package http;

import http.request.Request;
import http.response.Response;
import server.ServerWrapper;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.*;

/**
 * @Author: wws
 * @Date: 2020-07-24 22:07
 */
public class RequestDispatcher {

    private ServerWrapper serverWrapper;

    private ThreadPoolExecutor threadPoolExecutor;
    public RequestDispatcher() {

        threadPoolExecutor = new ThreadPoolExecutor(100, 100, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<>(200));

    }

    public void doDispatch(ServerWrapper serverWrapper) throws IOException {

        SocketChannel sChannel = (SocketChannel) serverWrapper.getsChannel();
        ByteBuffer buf = ByteBuffer.allocate(1024);

        int len;
        String request = "";
            if ((len = sChannel.read(buf)) > 0) {

//                requestbuf.array()
                buf.flip();
                Request req = new Request(new String(buf.array(), 0, len));
                req.parseRequest();
                if (req.getHeaders().get("Connection").equals("keep-alive"))
                    serverWrapper.keepALive = true;

                Response response = new Response();

//                threadPoolExecutor.execute(new RequestHandler(serverWrapper, req, response));
                new RequestHandler(serverWrapper, req, response).handler();

                buf.clear();
            }

    }
}
