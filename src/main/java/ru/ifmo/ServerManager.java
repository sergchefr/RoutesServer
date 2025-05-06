package ru.ifmo;

import ru.ifmo.coll.Route;
import ru.ifmo.coll.TreeSetHandler;
import ru.ifmo.history.History;
import ru.ifmo.clientCommands.Icommand;
import ru.ifmo.xmlmanager.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Класс, управляющий функциями всеми функциями сервера и взаимодействующий с управляющим классом клиентской части
 */
public class ServerManager implements Commands{

    private ArrayDeque<String> comWaitList;
    private HashMap<String,Icommand> commands;
    private TreeSetHandler collhandler;
    private History hst = new History();
    private ConnectionManager connectionManager;
    private String configLocation;
    private CommandManager commandManager;



    public ServerManager(String configLocation) {
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
            return commands.get(com.split(" ")[0]).execute(com);
        }
        return "ошибка при выполнении команды: команда не получена сервером";
    }

    @Override
    public String save(String filename) {
        XMLwriter writer=new XMLwriter();
        try {
            writer.writeRoute(collhandler.getAllRoutes(), filename);
        }catch (IOException e){
            return "can`t create the file";
        }
        return "collection saved";
    }

    @Override
    public String load(String filename) {
        int errorCounter=0;
        XMLreader reader = new XMLreader();
        try{
            ArrayList<Route> routes=reader.getRoutes(filename);
            for (Route route : routes) {
                collhandler.add(route);
            }
        }catch (IOException e){
            return "error while opening file: "+ filename;
        }catch (IllegalParamException e){
            errorCounter+=1;
        }
        return "file loaded, "+errorCounter+" errors";
    }

    @Override
    public String add(Route route) {
        return collhandler.add(route);
    }

    @Override
    public String info() {
        return collhandler.info();
    }

    @Override
    public String show() {
        return collhandler.show();
    }

    @Override
    public String update(Integer id, Route route) {
        return collhandler.update(id,route);
    }

    @Override
    public String removeById(long id) {
        return collhandler.removeById(id);
    }

    @Override
    public String clear() {
        return collhandler.clear();
    }

    @Override
    public String exit() {
        System.exit(0);
        return "";
    }

    @Override
    public String addIfMax(Route route) {
        return collhandler.addIfMax(route);
    }

    @Override
    public String addIfMin(Route route) {
        return collhandler.addIfMin(route);
    }

    @Override
    public String avgdistance() {
        return collhandler.avgdistance();
    }

    @Override
    public String printAsc() {
        return collhandler.printAsc();
    }

    @Override
    public String printAscDist() {
        return collhandler.printAscDist();
    }

    @Override
    public String showHistory() {
        return hst.showHistory();
    }

    @Override
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
}
