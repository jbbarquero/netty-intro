package com.malsolo.netty.transports;

import java.io.IOException;

public class TransportsApp {
    public static void main(String[] args) throws Exception {
        HiServer server;
        //server = new JavaPlainOioServer();
        server = new JavaPlainNioServer();
        //server = new NettyOioServer();
        //server = new NettyNioServer();
        server.serve(8282);
    }
}
