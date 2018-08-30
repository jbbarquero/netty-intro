package com.malsolo.sockets;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class DaytimeServer {

    private final static int PORT = 1313;

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(PORT)) {
            while (true) {
                try (Socket connection = server.accept()) {
                    Writer out = new OutputStreamWriter(connection.getOutputStream());
                    Date now = new Date();
                    out.write(now.toString() +"\r\n");
                    out.flush();
                    connection.close();
                } catch (IOException ex) {
                    System.err.printf("Socket connection (accept) error: %s\n", ex);
                }
            }
        } catch (IOException ex) {
            System.err.printf("Server Socket error: %s\n", ex);
        }
    }
}
