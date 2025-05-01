package ru.ifmo.clientCommands;

import ru.ifmo.Commands;

public class InfoCommand implements Icommand{
    Commands executor;

    public InfoCommand(Commands executor) {
        this.executor = executor;
    }

    @Override
    public String execute(String command) {
        executor.addCommandToHistory(command.split(" ")[0]);
        return executor.info();
    }

    @Override
    public String getName() {
        return "info";
    }
}
