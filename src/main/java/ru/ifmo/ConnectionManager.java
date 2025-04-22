package ru.ifmo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ConnectionManager {
    Selector selector;
    int port;

    public ConnectionManager(int port) {
        this.port = port;
        try {
            selector = Selector.open();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void AcceptNewConnections() {
        try {
            ServerSocketChannel server = ServerSocketChannel.open();
            server.bind(new InetSocketAddress(port));
            server.configureBlocking(false);

            var chan = server.accept();
            SelectionKey key;
            if (chan != null) key = server.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            System.out.println("ошибка при принятии подключения");
        }
    }

    public void checkNewCommands() {
        Set selectedKeys = selector.selectedKeys();
        Iterator keyIterator = selectedKeys.iterator();
        while (keyIterator.hasNext()) {

            SelectionKey key = (SelectionKey) keyIterator.next();
            if (key.isAcceptable()) {
                // a connection was accepted by a ServerSocketChannel.
            } else if (key.isConnectable()) {
                // a connection was established with a remote server.
            } else if (key.isReadable()) {
                try {
                    SocketChannel client = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int bytesRead = client.read(buffer);

                    if (bytesRead == -1) {
                        // Клиент отключился
                        client.close();
                        continue;
                    }

                    buffer.flip();
                    byte[] data = new byte[buffer.remaining()];
                    buffer.get(data);

                    System.out.println("Получено: " + new String(data));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (key.isWritable()) {
                // a channel is ready for writing
            }
            keyIterator.remove();
        }


    }
}
