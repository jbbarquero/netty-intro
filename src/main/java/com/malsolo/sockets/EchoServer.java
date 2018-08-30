package com.malsolo.sockets;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer {

    private final static int PORT = 4141;
    private final static int THREADS = 500;

    public static void main(String[] args) {

        ExecutorService pool = Executors.newFixedThreadPool(THREADS);

        try (ServerSocket server = new ServerSocket(PORT)){
            for (;;) {
                try {
                    Socket connection = server.accept();
                    Callable<Void> task = new EchoTask(connection);
                    pool.submit(task);
                }
                catch (IOException e) {
                    System.err.printf("Pooled Socket connection (accept) error: %s\n", e);
                }
            }
        } catch (IOException ex) {
            System.err.printf("Echo Server Socket error: %s\n", ex);
        }
    }

    private static class EchoTask implements Callable<Void> {

        private final Socket connection;

        EchoTask(Socket connection) {
            this.connection = connection;
        }

        @Override
        public Void call() throws Exception {
            try {
                InputStream in = new BufferedInputStream(connection.getInputStream());
                OutputStream out = connection.getOutputStream();
                int c;
                while ((c = in.read()) != -1) {
                    out.write(c);
                    out.flush();
                }

            } catch (IOException ioe) {
                System.err.printf("Task from Echo Server Socket error: %s\n", ioe);
            } finally {
                try {
                    connection.close();
                } catch (IOException ioex) {
                    System.err.printf("Task from Echo Server Socket error closing the connection: %s\n", ioex);
                }
            }
            return null;
        }
    }
}
