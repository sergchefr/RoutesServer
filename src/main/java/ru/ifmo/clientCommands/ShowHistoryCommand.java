package ru.ifmo.clientCommands;

import ru.ifmo.ServerManager;

public class ShowHistoryCommand implements Icommand{
    private ServerManager executor;

    public ShowHistoryCommand() {
        this.executor = ServerManager.getInstance();
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
