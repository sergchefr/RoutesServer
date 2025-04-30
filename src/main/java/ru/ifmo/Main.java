package ru.ifmo;

import ru.ifmo.serverCommands.*;
import ru.ifmo.transfer.Request;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;

public class Main {
    public static void main(String[] args) {
        ServerManager serverManager = new ServerManager("C:/Users/sergei/IdeaProjects/RoutesClient/src/main/java/ru/ifmo/config.xml");
        ConnectionManager connectionManager = new ConnectionManager(serverManager, 1111);
        serverManager.addCommand(new AddCommand(serverManager));
        serverManager.addCommand(new ClearCommand(serverManager));
        serverManager.addCommand(new GetConfigCommand(serverManager));
        serverManager.addCommand(new ShowCommand(serverManager));
        serverManager.addCommand(new AddIfMaxCommand(serverManager));
        serverManager.addCommand(new AddIfMinCommand(serverManager));
        serverManager.addCommand(new AvgDistanceCommand(serverManager));
        serverManager.addCommand(new InfoCommand(serverManager));
        serverManager.addCommand(new PrintAscCommand(serverManager));
        serverManager.addCommand(new PrintAscDistCommand(serverManager));
        serverManager.addCommand(new ShowHistoryCommand(serverManager));
        serverManager.addCommand(new UpdateCommand(serverManager));
        Console console = new Console();
        //System.out.println(serverManager.getConfig());
        while (true) {
            connectionManager.checkNewCommands();
            var a = console.checkInput();
            if(a!=null){
                if(a.equals("exit")) System.exit(0);
            }
        }

    }
}