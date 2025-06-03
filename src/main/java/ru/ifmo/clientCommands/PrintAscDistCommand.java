package ru.ifmo.clientCommands;

import ru.ifmo.ServerManager;
import ru.ifmo.coll.IRoutesHandler;

public class PrintAscDistCommand implements Icommand{
    private IRoutesHandler executor;

    public PrintAscDistCommand(IRoutesHandler executor) {
        this.executor = executor;
    }

    @Override
    public String execute(String command) {
        ServerManager.getInstance().addCommandToHistory(command.split(" ")[0]);
        return executor.printAscDist();
    }

    @Override
    public String getName() {
        return "print_asc_dist";
    }
}
