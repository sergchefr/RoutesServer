package ru.ifmo;

import ru.ifmo.coll.Route;
import ru.ifmo.coll.TreeSetHandler;
import ru.ifmo.history.History;
import ru.ifmo.serverCommands.Icommand;
import ru.ifmo.xmlmanager.*;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Класс, управляющий функциями сервера и взаимодействующий с управляющим классом клиентской части
 */
public class ServerManager implements Commands{

    private ArrayDeque<String> comWaitList= new ArrayDeque<>();
    private HashMap<String,Icommand> commands;
    private TreeSetHandler collhandler;
    private History hst = new History();
    private ConnectionManager connectionManager;



    public ServerManager(TreeSetHandler collhandler) {
        this.collhandler = collhandler;
    }

    public void addCommand(Icommand command){
        commands.put(command.getName(),command);
    }

    public void execute(){
        if(!comWaitList.isEmpty()){
            String com  = comWaitList.pollFirst();
            commands.get(com.split(" ")[0]).execute(com);
        }
    }

    public void addCommandToWaitList(String com){
        comWaitList.addLast(com);
        execute();
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

//            for (Route route : routes) {
//                while((add(route)).equals("element is already in the collection")){
//                    route = new Route(route.getId()+1, route.getName(), route.getCreationDate(),route.getFromLocation(),route.getToLocation(), route.getDistance());
//                }
//            }
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
    public String update(Long id, Route route) {
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

//    public void setClientManager(ClientManager clientManager) {
//        this.clientManager = clientManager;
//    }

    @Override
    public String showHistory() {
        return hst.showHistory();
    }

    public void addCommandToHistiry(String com){
        hst.add(com);
    }
}
