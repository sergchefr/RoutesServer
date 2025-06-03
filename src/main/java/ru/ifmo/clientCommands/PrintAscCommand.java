package ru.ifmo.clientCommands;

import ru.ifmo.ServerManager;
import ru.ifmo.coll.IRoutesHandler;

public class PrintAscCommand implements Icommand{
    IRoutesHandler executor;

    public PrintAscCommand(IRoutesHandler executor) {
        this.executor = executor;
    }

    @Override
    public String execute(String command) {
        ServerManager.getInstance().addCommandToHistory(command.split(" ")[0]);
        return executor.printAsc();
    }

    @Override
    public String getName() {
        return "print_asc";
    }
}
