package ru.ifmo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.ifmo.SQLservices.DBcreator;
import ru.ifmo.SQLservices.DatabaseManager;
import ru.ifmo.ServerCommands.*;
import ru.ifmo.clientCommands.*;
import ru.ifmo.coll.IRoutesHandler;
import ru.ifmo.coll.Location;
import ru.ifmo.coll.ProxyCacheArray;
import ru.ifmo.coll.Route;
import ru.ifmo.migration.*;
import ru.ifmo.passwordmanager.PasswordManager;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        IconnManager connectionManager = null;
        IRoutesHandler collection = null;
        Console console = null;
            DBcreator dBcreator = new DBcreator();
            dBcreator.prepareBD(true);
            PasswordManager passwordManager = PasswordManager.getInstance();

            passwordManager.addUser("admin", "not_password");
            System.out.println("при доступе с консоли сервера юзер: admin");
            ServerManager serverManager = ServerManager.getInstance();

            connectionManager = new MultithreadConnMannager(serverManager.getCommandManager(), 5317);
            CommandManager commandManager = serverManager.getCommandManager();

            collection = new ProxyCacheArray();

            DatabaseManager databaseManager = DatabaseManager.getInstance();

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
            commandManager.addClientCommand(new ImportCommand(collection));
            commandManager.addClientCommand(new MigrateCommand());
            commandManager.addClientCommand(new ExportCommand(collection));
            commandManager.addClientCommand(new printMetaCommand());

            commandManager.addServerCommand(new ExitCommand());
            commandManager.addServerCommand(new SaveCommand());
            commandManager.addServerCommand(new LoadCommand());


            console = new Console(commandManager);


        Console finalConsole = console;
        new Thread(() -> {
            while (true) finalConsole.manage();
        }).start();

//        ObjectMapper objectMapper = new ObjectMapper();
//
//
//        Jsonwriter jsonwriter = new Jsonwriter();
//        Jsonreader jsonreader = new Jsonreader();
//        XMLwriter xmLwriter = new XMLwriter();
//        XMLreader xmLreader = new XMLreader();
//
//
//        try {
//            jsonwriter.writeRoute(collection.getAllRoutes(), "resources/save1.json");
//            for (Route arg : jsonreader.getRoutes("resources/save1.json")) {
//                System.out.println("from json: "+arg.toString());
//            }
//
//            xmLwriter.writeRoute(collection.getAllRoutes(), "resources/save2.xml");
//            for (Route arg : xmLreader.getRoutes("resources/save2.xml")) {
//                System.out.println("from xml: "+arg.toString());
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (IllegalParamException e) {
//            throw new RuntimeException(e);
//        }


        while (true) {
            connectionManager.checkNewCommands();

        }


    }
}