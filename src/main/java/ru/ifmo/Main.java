package ru.ifmo;

import ru.ifmo.SQLservices.DBcreator;
import ru.ifmo.SQLservices.DatabaseManager;
import ru.ifmo.ServerCommands.*;
import ru.ifmo.clientCommands.*;
import ru.ifmo.coll.IRoutesHandler;
import ru.ifmo.coll.ProxyCacheArray;
import ru.ifmo.passwordmanager.PasswordManager;

public class Main {
    public static void main(String[] args) {
        DBcreator dBcreator =new DBcreator();
        dBcreator.prepareBD(true);
        PasswordManager passwordManager = PasswordManager.getInstance();

        passwordManager.addUser("admin", "not_password");
        System.out.println("при доступе с консоли сервера юзер: admin");
        ServerManager serverManager =ServerManager.getInstance();

        IconnManager connectionManager = new MultithreadConnMannager(serverManager.getCommandManager(), 5317);
        CommandManager commandManager = serverManager.getCommandManager();

        IRoutesHandler collection = new ProxyCacheArray();

        //"C:\\Users\\Сергей\\IdeaProjects\\RoutesServer\\resources\\config.xml"
        //"C:/Users/sergei/IdeaProjects/RoutesClient/config.xml"


        //passwordManager.addUser("admin", "password");
        DatabaseManager databaseManager = DatabaseManager.getInstance();
//        try {
//            databaseManager.addRoute(new Route("rtname", new Location(1,2,3,"frname"), new Location(5,6,7,"tonn"), 500), "admin");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }


        commandManager.addClientCommand(new AddCommand(collection));
        commandManager.addClientCommand(new ClearCommand(collection));
        commandManager.addClientCommand(new GetConfigCommand());
        commandManager.addClientCommand(new ShowCommand(collection));
        commandManager.addClientCommand(new AddIfMaxCommand(collection));
        commandManager.addClientCommand(new AddIfMinCommand(collection));
        commandManager.addClientCommand(new AvgDistanceCommand(collection));
        commandManager.addClientCommand(new InfoCommand(collection));
        commandManager.addClientCommand(new PrintAscCommand(collection));
        commandManager.addClientCommand(new PrintAscDistCommand(collection));
        commandManager.addClientCommand(new ShowHistoryCommand());
        commandManager.addClientCommand(new UpdateCommand(collection));
        commandManager.addClientCommand(new RemoveByIdCommand(collection));
        commandManager.addClientCommand(new RegisterCommand());
        commandManager.addClientCommand(new GetindependentCommand(collection));

        commandManager.addServerCommand(new ExitCommand());
        commandManager.addServerCommand(new SaveCommand());
        commandManager.addServerCommand(new LoadCommand());


        Console console = new Console(commandManager);
        //System.out.println(serverManager.getConfig());

        new Thread(() -> {
            //System.out.println("Поток выполняется: " + Thread.currentThread().getName());
            while (true) console.manage();
        }).start();

        while (true) {
            connectionManager.checkNewCommands();

        }


    }
}