package ru.ifmo.serverCommands;

import ru.ifmo.Commands;

public class PrintAscDistCommand implements Icommand{
    private Commands executor;

    public PrintAscDistCommand(Commands executor) {
        this.executor = executor;
    }

    @Override
    public String execute(String command) {
        return executor.printAscDist();
    }

    @Override
    public String getName() {
        return "print_asc_dist";
    }
}
