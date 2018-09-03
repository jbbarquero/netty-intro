package com.malsolo.sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SimpleEchoClient {

    private static final String IP = "127.0.0.1";
    private final static int DEFAULT_PORT = 4040;

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : IP;
        int port;
        try {
            port = Integer.parseInt(args[1]);
        } catch (RuntimeException ex) {
            port = DEFAULT_PORT;
        }
        System.out.printf("Connecting to host %s and port %d \n", host, port);

        try (
                Socket echoSocket = new Socket(host, port);
                PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                System.out.printf("echo: %s\n", in.readLine());
            }
        } catch (UnknownHostException e) {
            System.err.printf("Don't know about host %s \n", host);
            System.exit(1);
        } catch (IOException e) {
            System.err.printf("Couldn't get I/O for the connection to %s at %d\n", host, port);
            System.exit(1);
        }


    }

}
