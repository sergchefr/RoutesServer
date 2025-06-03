package ru.ifmo.clientCommands;

import ru.ifmo.ServerManager;

public class GetConfigCommand implements Icommand{

    private ServerManager executor;

    public GetConfigCommand() {
        this.executor = ServerManager.getInstance();
    }

    @Override
    public String execute(String command) {
        executor.addCommandToHistory(command.split(" ")[0]);
        if(!command.equals("load_config")) throw new RuntimeException();
        return executor.getConfig();
    }

    @Override
    public String getName() {
        return "load_config";
    }
}
