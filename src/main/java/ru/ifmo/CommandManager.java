package ru.ifmo;

import ru.ifmo.clientCommands.Icommand;

import java.util.HashMap;

public class CommandManager {
    HashMap<String, Icommand> clientcommands;
    HashMap<String, Icommand> servercommands;

    public CommandManager() {
        clientcommands = new HashMap<>();
        servercommands = new HashMap<>();
    }

    public String executeServer(String command){
        String name = command.split(" ")[0];
        var comObj = clientcommands.get(name);
        if (comObj==null) comObj=servercommands.get(name);
        if (comObj==null) return "команды с таким именем не существует";


        return comObj.execute(command);
    }

    public String executeClient(String command){
        String name = command.split(" ")[0];
        var comObj = clientcommands.get(name);
        if (comObj==null) return "команды с таким именем не существует";

        return comObj.execute(command);
    }

    public void addServerCommand(Icommand icommand){
        servercommands.put(icommand.getName(),icommand);
    }

    public void addClientCommand(Icommand icommand){
        clientcommands.put(icommand.getName(),icommand);
    }


}
