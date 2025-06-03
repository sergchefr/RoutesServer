package ru.ifmo.clientCommands;

import ru.ifmo.ServerManager;
import ru.ifmo.coll.IRoutesHandler;

public class ShowCommand implements Icommand {
    IRoutesHandler executor;

    public ShowCommand(IRoutesHandler executor) {
        this.executor = executor;
    }

    @Override
    public String execute(String command) {
        ServerManager.getInstance().addCommandToHistory(command.split(" ")[0]);
        return executor.show();
    }

    @Override
    public String getName() {
        return "show";
    }
}
