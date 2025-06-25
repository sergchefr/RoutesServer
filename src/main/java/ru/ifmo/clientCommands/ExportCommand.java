package ru.ifmo.clientCommands;

import ru.ifmo.ServerManager;
import ru.ifmo.coll.IRoutesHandler;
import ru.ifmo.coll.Route;
import ru.ifmo.migration.IllegalParamException;
import ru.ifmo.migration.Jsonwriter;
import ru.ifmo.migration.XMLwriter;
import ru.ifmo.passwordmanager.PasswordManager;
import ru.ifmo.transfer.Request;

import java.io.IOException;
import java.util.ArrayList;

public class ExportCommand implements Icommand{
    private IRoutesHandler collection;
    private XMLwriter xmLwriter;
    private Jsonwriter jsonwriter;

    public ExportCommand(IRoutesHandler collection) {
        this.collection = collection;
        xmLwriter = new XMLwriter();
        jsonwriter = new Jsonwriter();
    }

    @Override
    public String execute(Request com) {
        Route[] routes = collection.getAllRoutes();
        String command = com.getCommand();
        String filename = command.split(" ")[1].split("=")[1];
        try {
            if(PasswordManager.getInstance().checkPassword(com.getUser(), com.getPassword())){
                ServerManager.getInstance().addCommandToHistory(command.split(" ")[0]);
                if(filename.split("\\.")[1].equals("xml")){
                    xmLwriter.writeRoute(routes, filename);
                }else if (filename.split("\\.")[1].equals("json")){
                    jsonwriter.writeRoute(routes, filename);
                }else{
                    return "расширение "+filename.split("\\.")[1]+" не поддерживается";
                }
                for (Route route : routes) {
                    collection.add(route, com.getUser());
                }
                return "файл сохранен";
            }else{
                return "ошибка доступа";
            }
        } catch (IOException e) {
            return "ошибка при сохранении файла: "+e.getMessage();
        } catch (Exception e) {
            return "ошибка сохранения файла";
        }
    }

    @Override
    public String getName() {
        return "export";
    }
}
