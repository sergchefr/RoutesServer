package ru.ifmo.clientCommands;

import ru.ifmo.Commands;

public class PrintAscCommand implements Icommand{
    Commands executor;

    public PrintAscCommand(Commands executor) {
        this.executor = executor;
    }

    @Override
    public String execute(String command) {
        executor.addCommandToHistory(command.split(" ")[0]);
        return executor.printAsc();
    }

    @Override
    public String getName() {
        return "print_asc";
    }
}
