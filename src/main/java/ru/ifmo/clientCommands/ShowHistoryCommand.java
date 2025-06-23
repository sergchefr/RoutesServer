package ru.ifmo.clientCommands;

import ru.ifmo.ServerManager;
import ru.ifmo.passwordmanager.PasswordManager;
import ru.ifmo.transfer.Request;

public class ShowHistoryCommand implements Icommand{
    private ServerManager executor;

    public ShowHistoryCommand() {
        this.executor = ServerManager.getInstance();
    }

    @Override
    public String execute(Request com) {
        String command = com.getCommand();
        if(!PasswordManager.getInstance().checkPassword(com.getUser(),com.getPassword())) return "ошибка доступа";
        executor.addCommandToHistory(command.split(" ")[0]);
        return executor.showHistory();
    }

    @Override
    public String getName() {
        return "show_history";
    }
}
