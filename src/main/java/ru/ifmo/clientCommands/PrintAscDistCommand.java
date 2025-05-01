package ru.ifmo.clientCommands;

import ru.ifmo.Commands;

public class PrintAscDistCommand implements Icommand{
    private Commands executor;

    public PrintAscDistCommand(Commands executor) {
        this.executor = executor;
    }

    @Override
    public String execute(String command) {
        executor.addCommandToHistory(command.split(" ")[0]);
        return executor.printAscDist();
    }

    @Override
    public String getName() {
        return "print_asc_dist";
    }
}
