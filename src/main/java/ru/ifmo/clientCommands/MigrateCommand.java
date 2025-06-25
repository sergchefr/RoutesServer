package ru.ifmo.clientCommands;

import ru.ifmo.ServerManager;
import ru.ifmo.coll.Route;
import ru.ifmo.migration.*;
import ru.ifmo.passwordmanager.PasswordManager;
import ru.ifmo.transfer.Request;

import java.io.IOException;
import java.util.ArrayList;

public class MigrateCommand implements Icommand{
    private Jsonreader jsonreader;
    private XMLreader xmLreader;
    private Jsonwriter jsonwriter;
    private XMLwriter xmLwriter;

    public MigrateCommand() {
        jsonreader = new Jsonreader();
        jsonwriter = new Jsonwriter();
        xmLreader = new XMLreader();
        xmLwriter = new XMLwriter();
    }

    @Override
    public String execute(Request com) {
        String command = com.getCommand();
        //System.out.println(command);
        try{
            if(PasswordManager.getInstance().checkPassword(com.getUser(), com.getPassword())) {
                ServerManager.getInstance().addCommandToHistory(command.split(" ")[0]);

                var args = parsecommand(command);
                String fromname = args[0];
                String toname = args[1];

                ArrayList<Route> routes = new ArrayList<>();

                if(fromname.split("\\.")[1].equals("xml")){
                    routes = xmLreader.getRoutes(fromname);
                }else if (fromname.split("\\.")[1].equals("json")){
                    routes = jsonreader.getRoutes(fromname);
                }else{
                    return "расширение "+fromname.split("\\.")[1]+" не поддерживается";
                }


                Route[] rt = routes.toArray(new Route[0]);
                if(toname.split("\\.")[1].equals("xml")){
                    xmLwriter.writeRoute(rt, toname);
                }else if (toname.split("\\.")[1].equals("json")){
                    jsonwriter.writeRoute(rt, toname);
                }else{
                    return "расширение "+toname.split("\\.")[1]+" не поддерживается";
                }
                return "перемещено";

            }else{
            return "ошибка доступа";
        }
        } catch (IOException e) {
            return "ошибка открытия файла";
        }catch (IllegalParamException e){
            return "ошибка чтения файла";
        }


    }

    @Override
    public String getName() {
        return "migrate";
    }

    private String[] parsecommand(String command)throws IOException{
        String fromname = "";
        String toname = "";

        String[] args = command.split(" ");
        if(!args[0].equals("migrate")) throw new RuntimeException();

        for (String arg : args) {
            switch (arg.split("=")[0]) {
                case "fromname":
                    fromname = arg.split("=")[1];
                    break;
                case "toname":
                    toname = arg.split("=")[1];
                    break;
            }
        }
        if(fromname.isEmpty()|toname.isEmpty()) throw new IOException();
        return new String[]{fromname, toname};
    }
}
