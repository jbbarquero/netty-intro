package com.malsolo.sockets;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class EchoClientNio2NonBlocking {

    private static final int DEFAULT_PORT = 5555;
    private static final String IP = "127.0.0.1";
    private static final int MAGIC_NUMBER = 42;

    public static void main(String[] args) {

        ByteBuffer buffer = ByteBuffer.allocateDirect(2 * 1024);
        ByteBuffer randomBuffer;
        CharBuffer charBuffer;

        Charset charset = Charset.defaultCharset();
        CharsetDecoder decoder = charset.newDecoder();

        //open Selector and ServerSocketChannel by calling the open() method
        try (Selector selector = Selector.open(); SocketChannel socketChannel = SocketChannel.open()) {
            //configure non-blocking mode
            socketChannel.configureBlocking(false);
            //set some options
            socketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1024);
            socketChannel.setOption(StandardSocketOptions.SO_SNDBUF, 128 * 1024);
            socketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);

            //register the current channel with the given selector
            socketChannel.register(selector, SelectionKey.OP_CONNECT);

            //connect to remote host
            socketChannel.connect(new InetSocketAddress(IP, DEFAULT_PORT));

            System.out.printf("Localhost: %s\n", socketChannel.getLocalAddress());

            //waiting for the connection
            while (selector.select(1000) > 0) {

                //get keys
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> its = keys.iterator();

                //process each key
                while (its.hasNext()) {
                    SelectionKey key = its.next();

                    //remove the current key
                    its.remove();

                    //get the socket channel for this key
                    try (SocketChannel keySocketChannel = (SocketChannel) key.channel()) {

                        //attempt a connection
                        if (key.isConnectable()) {

                            //signal connection success
                            System.out.println("I am connected!");

                            //close pending connections
                            if (keySocketChannel.isConnectionPending()) {
                                keySocketChannel.finishConnect();
                            }

                            //read/write from/to server
                            while (keySocketChannel.read(buffer) != -1) {

                                buffer.flip();

                                charBuffer = decoder.decode(buffer);
                                System.out.println(charBuffer.toString());

                                if (buffer.hasRemaining()) {
                                    buffer.compact();
                                } else {
                                    buffer.clear();
                                }

                                int r = new Random().nextInt(100);
                                if (r == MAGIC_NUMBER) {
                                    System.out.printf("%d was generated! Close the socket channel!\n", r);
                                    break;
                                }
                                else {
                                    randomBuffer = ByteBuffer.wrap("Random number:"
                                            .concat(String.valueOf(r)).getBytes());
                                    keySocketChannel.write(randomBuffer);
                                    //Without the waiting the messages don't look well
                                    try {
                                        Thread.sleep(1);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        else {
                            System.out.println("The socket channel or selector cannot be opened!");
                        }
                    }
                    catch (IOException ioe) {
                        System.err.printf("Non-Blocking NIO.2 Echo Client, error with the key socket channel: %s\n", ioe);
                    }

                }

            }

        }
        catch (IOException ioe) {
            System.err.printf("Non-Blocking NIO.2 Echo Client, the socket channel or selector cannot be opened: %s\n", ioe);
        }

    }


}
