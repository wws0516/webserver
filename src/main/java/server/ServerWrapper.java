package server;

import http.response.Response;

import java.nio.channels.SocketChannel;

/**
 * @Author: wws
 * @Date: 2020-07-16 00:30
 */
public class ServerWrapper {

    public SocketChannel sChannel;

    public boolean keepALive = false;

    public void process(SocketChannel sChannel, Response response){

    }

//    public void run(){}

    public SocketChannel getsChannel() {
        return sChannel;
    }

    public void setsChannel(SocketChannel sChannel) {
        this.sChannel = sChannel;
    }
}
