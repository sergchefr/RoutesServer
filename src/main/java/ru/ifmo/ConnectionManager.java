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
            //System.out.println(key);
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


                ByteBuffer requestBuffer = ByteBuffer.allocate(10000);
                try {
                    int r = client.read(requestBuffer);
                    var buf = new byte[r];
                    requestBuffer.get(0,buf);
                    if (r == -1) {
                        client.close();
                    } else {
                        ByteArrayInputStream bis = new ByteArrayInputStream(buf);
                        ObjectInputStream ois = new ObjectInputStream(bis);
                        System.out.println(((Request)ois.readObject()).getCommand());
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


    public void nio_non_blockable_selector_server() throws IOException {
        try (ServerSocketChannel channel = ServerSocketChannel.open();
             //Открытие селектора. Под капотом вызывается SelectorProvider, реализация которого является платформозависимой
             Selector selector = Selector.open()) {
            channel.socket().bind(new InetSocketAddress(port));
            channel.configureBlocking(false);
            //Регистрируем серверный канал в селекторе с интересующим типом операции - принятие подключения
            SelectionKey registeredKey = channel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                //Получаем количество готовых к обработке каналов.
                int numReadyChannels = selector.select();
                if (numReadyChannels == 0) {
                    continue;
                }
                //Получаем готовые к обработке каналы
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                //Обрабатываем каналы в соответствии с типом доступной каналу операции
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();

                    if (key.isAcceptable()) {
                        //Принятие подключения серверным сокетом
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel client = server.accept();
                        if (client == null) {
                            continue;
                        }
                        client.configureBlocking(false);
                        //Регистрируем принятое подключение в селекторе с интересующим типом операции - чтение
                        client.register(selector, SelectionKey.OP_READ);
                    }

                    if (key.isReadable()) {
                        //Тут происходит обработка принятых подключений
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer requestBuffer = ByteBuffer.allocate(100);
                        int r = client.read(requestBuffer);
                        if (r == -1) {
                            client.close();
                        } else {
                            //В этом блоке происходит обработка запроса
                            System.out.println(new String(requestBuffer.array()));
                            String responseMessage = "Привет от сервера! : " + client.socket().getLocalSocketAddress();
                            //Несмотря на то, что интересующая операция, переданная в селектор - чтение, мы все равно можем писать в сокет
                            client.write(ByteBuffer.wrap(responseMessage.getBytes()));
                        }
                    }
                    //Удаляем ключ после обработки. Если канал снова будет доступным, его ключ снова появится в selectedKeys
                    keyIterator.remove();
                }
            }
        }
    }
}
