package com.malsolo.sockets;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPooledDaytimeServer {

    private final static int PORT = 1414;
    private final static int THREADS = 50;

    public static void main(String[] args) {

        ExecutorService pool = Executors.newFixedThreadPool(THREADS);

        try(ServerSocket server = new ServerSocket(PORT)) {
            while (true) {
                try {
                    Socket connection = server.accept();
                    Callable<Void> task = new DaytimeTask(connection);
                    pool.submit(task);

                } catch (IOException ex) {
                    System.err.printf("Pooled Socket connection (accept) error: %s\n", ex);
                }

            }
        } catch (IOException ex) {
            System.err.printf("Pooled Server Socket error: %s\n", ex);
        }
    }

    private static class DaytimeTask implements Callable<Void> {

        private final Socket connection;

        DaytimeTask(Socket connection) {
            this.connection = connection;
        }

        @Override
        public Void call() throws Exception {
            try {
                Writer out = new OutputStreamWriter(connection.getOutputStream());
                Date now = new Date();
                out.write(now.toString() +"\r\n");
                out.flush();
            } catch (IOException ioe) {
                System.err.printf("Task from Pooled Server Socket error: %s\n", ioe);
            } finally {
                try {
                    connection.close();
                } catch (IOException ioex) {
                    System.err.printf("Task from Pooled Server Socket error closing the connection: %s\n", ioex);
                }
            }
            return null;
        }
    }

}
