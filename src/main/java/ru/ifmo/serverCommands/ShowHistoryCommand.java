package ru.ifmo.serverCommands;

import ru.ifmo.Commands;

public class ShowHistoryCommand implements Icommand{
    private Commands executor;

    public ShowHistoryCommand(Commands executor) {
        this.executor = executor;
    }

    @Override
    public String execute(String command) {
        return executor.showHistory();
    }

    @Override
    public String getName() {
        return "show_history";
    }
}
