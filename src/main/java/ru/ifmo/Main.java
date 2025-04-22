package ru.ifmo;

import ru.ifmo.transfer.Request;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;

public class Main {
    public static void main(String[] args) {
        ConnectionManager connectionManager = new ConnectionManager(111);
        while (true) {
            //connectionManager.AcceptNewConnections();
            connectionManager.checkNewCommands();
        }
//        ConnectionManager connectionManager = new ConnectionManager();
//        try {
//            connectionManager.nio_non_blockable_selector_server();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }


//        try {
//            ServerSocket server = new ServerSocket(111);
//            try  {
//
//
//                //   объявить о своем запуске
//                Socket socket;
//
//                socket  = server.accept(); // accept() будет ждать пока
//                //кто-нибудь не захочет подключиться
//                try { // установив связь и воссоздав сокет для общения с клиентом можно перейти
//                    // к созданию потоков ввода/вывода.
//                    // теперь мы можем принимать сообщения
//                    var in = socket.getInputStream();
//                    var out = socket.getOutputStream();
//
//                    ObjectOutputStream serialiser = new ObjectOutputStream(out);
//                    ObjectInputStream deserialiser = new ObjectInputStream(in);
//
//                    String word = "ошибка передачи команды"; // ждём пока клиент что-нибудь нам напишет
//                    try {
//                        word = ((Request) deserialiser.readObject()).getCommand();
//                    } catch (ClassNotFoundException e) {
//                        throw new RuntimeException(e);
//                    }
//                    System.out.println(word);
////                    // не долго думая отвечает клиенту
////                    out.write("Привет, это Сервер! Подтверждаю, вы написали : " + word + "\n");
////                    out.flush(); // выталкиваем все из буфера
//
//                } finally { // в любом случае сокет будет закрыт
//                    socket.close();
//                }
//            } finally {
//                System.out.println("Сервер закрыт!");
//                server.close();
//            }
//        } catch (IOException e) {
//            System.err.println(e);
//        }
    }
}