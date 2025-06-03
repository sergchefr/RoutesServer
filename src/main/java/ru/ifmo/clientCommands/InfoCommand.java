package ru.ifmo.clientCommands;

import ru.ifmo.ServerManager;
import ru.ifmo.coll.IRoutesHandler;

public class InfoCommand implements Icommand{
    IRoutesHandler executor;

    public InfoCommand(IRoutesHandler executor) {
        this.executor = executor;
    }

    @Override
    public String execute(String command) {
        ServerManager.getInstance().addCommandToHistory(command.split(" ")[0]);
        return executor.info();
    }

    @Override
    public String getName() {
        return "info";
    }
}
