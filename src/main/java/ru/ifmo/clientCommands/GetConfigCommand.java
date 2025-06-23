package ru.ifmo.clientCommands;

import ru.ifmo.ServerManager;
import ru.ifmo.passwordmanager.PasswordManager;
import ru.ifmo.transfer.Request;

public class GetConfigCommand implements Icommand{

    private ServerManager executor;

    public GetConfigCommand() {
        this.executor = ServerManager.getInstance();
    }

    @Override
    public String execute(Request com) {
        String command = com.getCommand();
        if(!PasswordManager.getInstance().checkPassword(com.getUser(),com.getPassword())) return "ошибка доступа";
        executor.addCommandToHistory(command.split(" ")[0]);
        if(!command.equals("load_config")) throw new RuntimeException();
        return executor.getConfig();
    }

    @Override
    public String getName() {
        return "load_config";
    }
}
