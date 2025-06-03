package ru.ifmo.clientCommands;

import ru.ifmo.ServerManager;
import ru.ifmo.coll.IRoutesHandler;

public class ClearCommand implements Icommand {
    private IRoutesHandler executor;

    public ClearCommand(IRoutesHandler executor) {
        this.executor = executor;
    }

    @Override
    public String execute(String command) {
        ServerManager.getInstance().addCommandToHistory(command.split(" ")[0]);
        if(!command.equals("clear")) throw new RuntimeException();
        return executor.clear();
    }

    @Override
    public String getName() {
        return "clear";
    }
}
