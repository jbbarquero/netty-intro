package com.malsolo.netty.transports;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

public class JavaPlainOioServer implements HiServer {
    public void serve(int port) throws Exception {
        final ServerSocket socket = new ServerSocket(port);
        try {
            for (;;) {
                final Socket clientSocket = socket.accept();
                System.out.println("Accepted connection from " + clientSocket);
                new Thread(() -> {
                    OutputStream out;
                    try {
                        out = clientSocket.getOutputStream();
                        out.write("Hi!\r\n".getBytes(Charset.forName("UTF-8")));
                        out.flush();
                        clientSocket.close();
                    }
                    catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    finally {
                        try {
                            clientSocket.close();
                        }
                        catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                    }
                }).start();
            }
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
