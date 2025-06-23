package ru.ifmo;

import ru.ifmo.coll.IRoutesHandler;
import ru.ifmo.coll.Route;
import ru.ifmo.coll.TreeSetHandler;
import ru.ifmo.history.History;
import ru.ifmo.clientCommands.Icommand;
import ru.ifmo.transfer.Request;
import ru.ifmo.xmlmanager.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Класс, управляющий всеми функциями сервера
 */
public class ServerManager{

    private ArrayDeque<String> comWaitList;
    private HashMap<String,Icommand> commands;
    private IRoutesHandler collhandler;
    private History hst = new History();
    private String configLocation;
    private CommandManager commandManager;

    private static volatile ServerManager instance;

    private ServerManager(String configLocation) {
        this.configLocation = configLocation;
        collhandler= new TreeSetHandler();
        this.collhandler = collhandler;
        comWaitList = new ArrayDeque<>();
        commands = new HashMap<>();
        commandManager = new CommandManager();
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public String execute(){
        if(!comWaitList.isEmpty()){
            String com  = comWaitList.pollFirst();
            return commands.get(com.split(" ")[0]).execute((new Request(com, "server_admin", "not_password")));
        }
        return "ошибка при выполнении команды: команда не получена сервером";
    }

    public String save(String filename) {
        XMLwriter writer=new XMLwriter();
        try {
            writer.writeRoute(collhandler.getAllRoutes(), filename);
        }catch (IOException e){
            return "can`t create the file";
        }
        return "collection saved";
    }

    public String load(String filename) {
        int errorCounter=0;
        XMLreader reader = new XMLreader();
        try{
            ArrayList<Route> routes=reader.getRoutes(filename);
            for (Route route : routes) {
                collhandler.add(route, "unknown");
            }
        }catch (IOException e){
            return "error while opening file: "+ filename;
        }catch (IllegalParamException e){
            errorCounter+=1;
        }
        return "file loaded, "+errorCounter+" errors";
    }

    public String getConfig() {
        try {
            String rt = System.getenv("config_location");
        if(rt==null) throw new IOException("нет переменной окружения: config_location");
            Path path = Paths.get(configLocation);
            var fileString = Files.readString(path);
            //System.out.println(fileString);
            return fileString;
        } catch (IOException e) {

            System.err.println("ошибка при чтении конфига:"+ e);
            return "внутренняя ошибка сервера ";
        }
    }

    public void addCommandToHistory(String com){
        hst.add(com);
    }

    public String showHistory(){
        return hst.showHistory();
    }

    public static ServerManager getInstance() {
        if(instance==null){
            synchronized (ServerManager.class){
                if(instance==null) instance = new ServerManager("resources/config.xml");
            }
        }
        return instance;
    }
}
