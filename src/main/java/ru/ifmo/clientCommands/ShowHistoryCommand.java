package ru.ifmo.clientCommands;

import ru.ifmo.Commands;

public class ShowHistoryCommand implements Icommand{
    private Commands executor;

    public ShowHistoryCommand(Commands executor) {
        this.executor = executor;
    }

    @Override
    public String execute(String command) {
        executor.addCommandToHistory(command.split(" ")[0]);
        return executor.showHistory();
    }

    @Override
    public String getName() {
        return "show_history";
    }
}
