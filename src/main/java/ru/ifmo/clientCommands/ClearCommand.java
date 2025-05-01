package ru.ifmo.clientCommands;

import ru.ifmo.Commands;

public class ClearCommand implements Icommand {
    private Commands executor;

    public ClearCommand(Commands executor) {
        this.executor = executor;
    }

    @Override
    public String execute(String command) {
        executor.addCommandToHistory(command.split(" ")[0]);
        if(!command.equals("clear")) throw new RuntimeException();
        return executor.clear();
    }

    @Override
    public String getName() {
        return "clear";
    }
}
