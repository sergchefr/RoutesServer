package ru.ifmo.ServerCommands;

import ru.ifmo.clientCommands.Icommand;

public class ExitCommand implements Icommand {

    @Override
    public String execute(String command) {
        System.exit(0);
        return "exiting...";
    }

    @Override
    public String getName() {
        return "exit";
    }
}
