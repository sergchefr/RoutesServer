package ru.ifmo.serverCommands;

import ru.ifmo.Commands;

public class ShowCommand implements Icommand {
    Commands executor;

    public ShowCommand(Commands executor) {
        this.executor = executor;
    }

    @Override
    public String execute(String command) {
        return executor.show();
    }

    @Override
    public String getName() {
        return "show";
    }
}
