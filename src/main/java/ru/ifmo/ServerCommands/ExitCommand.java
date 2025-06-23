package ru.ifmo.ServerCommands;

import ru.ifmo.clientCommands.Icommand;
import ru.ifmo.transfer.Request;

public class ExitCommand implements Icommand {

    @Override
    public String execute(Request command) {
        System.exit(0);
        return "exiting...";
    }

    @Override
    public String getName() {
        return "exit";
    }
}
