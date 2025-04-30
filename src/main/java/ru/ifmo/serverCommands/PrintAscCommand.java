package ru.ifmo.serverCommands;

import ru.ifmo.Commands;

public class PrintAscCommand implements Icommand{
    Commands executor;

    public PrintAscCommand(Commands executor) {
        this.executor = executor;
    }

    @Override
    public String execute(String command) {
        return executor.printAsc();
    }

    @Override
    public String getName() {
        return "print_asc";
    }
}
