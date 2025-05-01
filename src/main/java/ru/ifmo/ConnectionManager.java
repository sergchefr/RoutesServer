package ru.ifmo;

import ru.ifmo.transfer.Request;
import ru.ifmo.transfer.Response;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketException;
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
    private ServerSocketChannel server;
    private  CommandManager commandManager;


    public ConnectionManager(CommandManager commandManager, int port) {
        this.commandManager = commandManager;
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
        port =1111;
    }

    public void checkNewCommands() {
        SocketChannel chan = null;
        try {
            if (selector.selectNow()==0)   return;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Set<SelectionKey> selectedKeys = selector.selectedKeys();

        Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
        while (keyIterator.hasNext()) {

            SelectionKey key = (SelectionKey) keyIterator.next();
            if (key.isAcceptable()) {
                //System.out.println("acceptable");
                try {
                    chan = server.accept();
                    if (chan != null){
                        chan.configureBlocking(false);

                        chan.register(selector,SelectionKey.OP_READ);
                        System.out.println("подключение приято и зарегистрировано");
                    }
                } catch (IOException e) {
                    System.out.println("ошибка при принятии подключения");
                }

            } else if (key.isConnectable()) {
                //System.out.println("connectable");
                // a connection was established with a remote server.
            } else if (key.isReadable()) {
                //System.out.println("readable");
                SocketChannel client = (SocketChannel) key.channel();


                ByteBuffer requestBuffer = ByteBuffer.allocate(1000);
                try {
                    int r = client.read(requestBuffer);
                    if (r == -1) {
                        client.close();
                        System.out.println("client closed");
                    } else {
                        var buf = new byte[r];
                        requestBuffer.get(0, buf);
                        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
                        ObjectInputStream ois = new ObjectInputStream(bais);
                        Request request = (Request) ois.readObject();
                        String ans = commandManager.executeClient(request.getCommand());

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ObjectOutputStream ous = new ObjectOutputStream(baos);
                        ous.writeObject(new Response(ans));
                        ByteBuffer outbuf = ByteBuffer.wrap(baos.toByteArray());

                        client.write(outbuf);
                        //System.out.println();

                    }
                }catch (SocketException e){
                    //System.out.println(e);
                    key.cancel();
                    System.out.println("канал закрыт");
//                    try {
//                        chan.close();
//                    } catch (IOException|NullPointerException ex) {
//                        System.out.println("ошибка закрытия канала");
//                    }
                }catch (IOException|ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }



            } else if (key.isWritable()) {
                //System.out.println("writeable");
                // a channel is ready for writing
            }
            keyIterator.remove();
        }


    }
}
