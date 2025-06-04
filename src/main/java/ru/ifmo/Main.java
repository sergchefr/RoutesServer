package ru.ifmo;

import ru.ifmo.SQLservices.DBcreator;
import ru.ifmo.SQLservices.DatabaseManager;
import ru.ifmo.coll.ProxyCacheArray;
import ru.ifmo.passwordmanager.PasswordManager;

public class Main {
    public static void main(String[] args) {
        DBcreator dBcreator =new DBcreator();
        dBcreator.prepareBD();
        PasswordManager passwordManager = PasswordManager.getInstance();
        ServerManager serverManager =ServerManager.getInstance();


        //"C:\\Users\\Сергей\\IdeaProjects\\RoutesServer\\resources\\config.xml"
        //"C:/Users/sergei/IdeaProjects/RoutesClient/config.xml"

        ConnectionManager connectionManager = new ConnectionManager(serverManager.getCommandManager(), 1111);
        CommandManager commandManager = serverManager.getCommandManager();

        //passwordManager.addUser("admin", "password");

        ProxyCacheArray cache = new ProxyCacheArray();
        DatabaseManager databaseManager = DatabaseManager.getInstance();
//        try {
//            databaseManager.addRoute(new Route("rtname", new Location(1,2,3,"frname"), new Location(5,6,7,"tonn"), 500), "admin");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }


//        commandManager.addClientCommand(new AddCommand());
//        commandManager.addClientCommand(new ClearCommand(serverManager));
//        commandManager.addClientCommand(new GetConfigCommand(serverManager));
//        commandManager.addClientCommand(new ShowCommand(serverManager));
//        commandManager.addClientCommand(new AddIfMaxCommand(serverManager));
//        commandManager.addClientCommand(new AddIfMinCommand(serverManager));
//        commandManager.addClientCommand(new AvgDistanceCommand(serverManager));
//        commandManager.addClientCommand(new InfoCommand(serverManager));
//        commandManager.addClientCommand(new PrintAscCommand(serverManager));
//        commandManager.addClientCommand(new PrintAscDistCommand(serverManager));
//        commandManager.addClientCommand(new ShowHistoryCommand(serverManager));
//        commandManager.addClientCommand(new UpdateCommand(serverManager));
//        commandManager.addClientCommand(new RemoveByIdCommand(serverManager));
//        commandManager.addClientCommand(new RegisterCommand());
//
//        commandManager.addServerCommand(new ExitCommand());
//        commandManager.addServerCommand(new SaveCommand(serverManager));
//        commandManager.addServerCommand(new LoadCommand(serverManager));


        //Console console = new Console(commandManager);
        //System.out.println(serverManager.getConfig());


//        while (true) {
//            connectionManager.checkNewCommands();
//            console.manage();
//        }

        

    }
}