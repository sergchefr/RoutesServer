package ru.ifmo;

import ru.ifmo.transfer.Request;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

public class ConnectionManager {
    private Selector selector;
    private int port;
    ServerSocketChannel server;

    public ConnectionManager(int port) {
        this.port = port;
        try {

            server = ServerSocketChannel.open();
            server.socket().bind(new InetSocketAddress(port));
            server.configureBlocking(false);

            selector = Selector.open();

            server.register(selector, SelectionKey.OP_ACCEPT);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ConnectionManager() {
        port =111;
    }

    public void AcceptNewConnections() {
        try {
            var chan = server.accept();
            SelectionKey key;
            if (chan != null){
                chan.configureBlocking(false);

                chan.register(selector,SelectionKey.OP_READ);
//                key = server.register(selector, SelectionKey.OP_ACCEPT);
//                System.out.println(key);
                System.out.println("подключение приято и зарегистрировано");
            }
        } catch (IOException e) {
            System.out.println("ошибка при принятии подключения");
        }
    }

    public void checkNewCommands() {

        try {
            if (selector.selectNow()==0)   return;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Set<SelectionKey> selectedKeys = selector.selectedKeys();
        //System.out.println(selectedKeys);



        Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
        while (keyIterator.hasNext()) {

            SelectionKey key = (SelectionKey) keyIterator.next();
//            System.out.println(key);
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
            if (key.isAcceptable()) {
                System.out.println("acceptable");
                try {
                    var chan = server.accept();
                    if (chan != null){
                        chan.configureBlocking(false);

                        chan.register(selector,SelectionKey.OP_READ);
                        System.out.println("подключение приято и зарегистрировано");
                    }
                } catch (IOException e) {
                    System.out.println("ошибка при принятии подключения");
                }
                // a connection was accepted by a ServerSocketChannel.


            } else if (key.isConnectable()) {
                System.out.println("connectable");
                // a connection was established with a remote server.
            } else if (key.isReadable()) {
                System.out.println("readable");
                SocketChannel client = (SocketChannel) key.channel();


                ByteBuffer requestBuffer = ByteBuffer.allocate(1000);
                try {
                    int r = client.read(requestBuffer);
                    if (r == -1) {
                        client.close();
                        System.out.println("client closed");
                    } else {
                        var buf = new byte[r];
                        requestBuffer.get(0,buf);
                        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
                        ObjectInputStream ois = new ObjectInputStream(bais);
                        Request request = (Request)ois.readObject();
                        System.out.println(request.getCommand());
//                        ByteArrayInputStream bis = new ByteArrayInputStream(buf);
//                        ObjectInputStream ois = new ObjectInputStream(bis);
//                        System.out.println(((Request)ois.readObject()).getCommand());
                    }
                } catch (IOException|ClassNotFoundException e) {
                    e.printStackTrace();
                }



            } else if (key.isWritable()) {
                System.out.println("writeable");
                // a channel is ready for writing
            }
            keyIterator.remove();
        }


    }
}
