package com.malsolo.sockets.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class DaytimeClient {

    private final static int TIMEOUT = 5000;

    public static void main(String[] args) {
        String hostname = args.length > 0 ? args[0] : "time.nist.gov";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 13;

        try (Socket socket = new Socket(hostname, port)) {
            socket.setSoTimeout(TIMEOUT);
            InputStream in = socket.getInputStream();
            StringBuilder time = new StringBuilder();
            InputStreamReader reader = new InputStreamReader(in, "ASCII");
            for (int c = reader.read(); c != -1 ; c = reader.read()) {
                time.append((char) c);
            }
            System.out.println(time);
        } catch (IOException ex) {
            System.err.printf("Could not connect to host %s and port %d due to %s \n",
                    hostname, port, ex.getMessage());
        }
    }

}
