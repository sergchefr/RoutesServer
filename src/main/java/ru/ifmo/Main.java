package ru.ifmo;

import ru.ifmo.ServerCommands.*;
import ru.ifmo.clientCommands.*;

public class Main {
    public static void main(String[] args) {
        ServerManager serverManager = new ServerManager("resources/config.xml");
        //"C:\\Users\\Сергей\\IdeaProjects\\RoutesServer\\resources\\config.xml"
        //"C:/Users/sergei/IdeaProjects/RoutesClient/config.xml"

        ConnectionManager connectionManager = new ConnectionManager(serverManager.getCommandManager(), 1111);
        CommandManager commandManager = serverManager.getCommandManager();


        commandManager.addClientCommand(new AddCommand(serverManager));
        commandManager.addClientCommand(new ClearCommand(serverManager));
        commandManager.addClientCommand(new GetConfigCommand(serverManager));
        commandManager.addClientCommand(new ShowCommand(serverManager));
        commandManager.addClientCommand(new AddIfMaxCommand(serverManager));
        commandManager.addClientCommand(new AddIfMinCommand(serverManager));
        commandManager.addClientCommand(new AvgDistanceCommand(serverManager));
        commandManager.addClientCommand(new InfoCommand(serverManager));
        commandManager.addClientCommand(new PrintAscCommand(serverManager));
        commandManager.addClientCommand(new PrintAscDistCommand(serverManager));
        commandManager.addClientCommand(new ShowHistoryCommand(serverManager));
        commandManager.addClientCommand(new UpdateCommand(serverManager));
        commandManager.addClientCommand(new RemoveByIdCommand(serverManager));

        commandManager.addServerCommand(new ExitCommand());
        commandManager.addServerCommand(new SaveCommand(serverManager));
        commandManager.addServerCommand(new LoadCommand(serverManager));


        Console console = new Console(commandManager);
        //System.out.println(serverManager.getConfig());


        while (true) {
            connectionManager.checkNewCommands();
            console.manage();

        }

    }
}