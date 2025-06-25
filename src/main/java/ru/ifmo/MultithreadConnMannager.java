package ru.ifmo;

import ru.ifmo.passwordmanager.PasswordManager;
import ru.ifmo.transfer.Request;
import ru.ifmo.transfer.Response;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MultithreadConnMannager implements IconnManager {

    private final int port;
    private final CommandManager commandManager;
    private final PasswordManager passwordManager;
    private final ServerSocketChannel serverChannel;

    private final ExecutorService responseSender = Executors.newFixedThreadPool(4);

    public MultithreadConnMannager(CommandManager commandManager, int port) {
        this.commandManager = commandManager;
        this.port = port;
        this.passwordManager = PasswordManager.getInstance();

        try {
            serverChannel = ServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress(port));
            serverChannel.configureBlocking(true);
            System.out.println("Сервер запущен на порту " + port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void checkNewCommands() {
        try {
            SocketChannel clientChannel = serverChannel.accept();
            new Thread(() -> handleClient(clientChannel)).start();
        } catch (IOException e) {
            System.err.println("Ошибка соединения: " + e.getMessage());
        }
    }

    private void handleClient(SocketChannel clientChannel) {
        try {
            InputStream inStream = Channels.newInputStream(clientChannel);
            ObjectInputStream in = new ObjectInputStream(inStream);
            Request request = (Request) in.readObject();
            String responseText = commandManager.executeClient(request);
            Response response = new Response(responseText);
            responseSender.submit(() -> {
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream ous = new ObjectOutputStream(baos);
                    ous.writeObject(response);
                    ByteBuffer outbuf = ByteBuffer.wrap(baos.toByteArray());
                    clientChannel.write(outbuf);
                } catch (IOException e) {
                    System.err.println("Ошибка при отправке ответа: " + e.getMessage());
                }finally {
                    try {
                        clientChannel.close();
                    } catch (IOException ignored) {}
                }
            });

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка при работе с клиентом: " + e.getMessage());
        }
    }
}
