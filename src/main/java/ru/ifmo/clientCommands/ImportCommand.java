package ru.ifmo.clientCommands;

import ru.ifmo.ServerManager;
import ru.ifmo.coll.IRoutesHandler;
import ru.ifmo.coll.ProxyCacheArray;
import ru.ifmo.coll.Route;
import ru.ifmo.migration.IllegalParamException;
import ru.ifmo.migration.Jsonreader;
import ru.ifmo.migration.XMLreader;
import ru.ifmo.migration.XMLwriter;
import ru.ifmo.passwordmanager.PasswordManager;
import ru.ifmo.transfer.Request;

import java.io.IOException;
import java.util.ArrayList;

public class ImportCommand implements Icommand {
    private IRoutesHandler collection;
    private Jsonreader jsonreader;
    private XMLreader xmLreader;


    public ImportCommand(IRoutesHandler collection) {
        this.collection = collection;
        xmLreader = new XMLreader();
        jsonreader = new Jsonreader();
    }

    @Override
    public String execute(Request com) {
        String command = com.getCommand();
        String filename = command.split(" ")[1].split("=")[1];
        try {
            if(PasswordManager.getInstance().checkPassword(com.getUser(), com.getPassword())){
                ServerManager.getInstance().addCommandToHistory(command.split(" ")[0]);
                ArrayList<Route> routes;
                if(filename.split("\\.")[1].equals("xml")){
                    routes = xmLreader.getRoutes(filename);
                }else if (filename.split("\\.")[1].equals("json")){
                    routes = jsonreader.getRoutes(filename);
                }else{
                    return "расширение "+filename.split("\\.")[1]+" не поддерживается";
                }
                for (Route route : routes) {
                    collection.add(route, route.getOwnername());
                }
                return "элементы добавлены в коллекцию";
            }else{
                return "ошибка доступа";
            }
        } catch (IOException e) {
            return "ошибка при загрузке файла: "+e.getMessage();
        }catch (IllegalParamException e){
            return "неверные параметры";
        } catch (Exception e) {
            return "ошибка загрузки файла "+e.getMessage();
        }
    }

    @Override
    public String getName() {
        return "import";
    }
}
