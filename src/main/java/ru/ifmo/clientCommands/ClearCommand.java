package ru.ifmo.clientCommands;

import ru.ifmo.ServerManager;
import ru.ifmo.coll.IRoutesHandler;
import ru.ifmo.passwordmanager.PasswordManager;
import ru.ifmo.transfer.Request;

public class ClearCommand implements Icommand {
    private IRoutesHandler executor;

    public ClearCommand(IRoutesHandler executor) {
        this.executor = executor;
    }

    @Override
    public String execute(Request com) {
        String command = com.getCommand();
        if(!PasswordManager.getInstance().checkPassword(com.getUser(),com.getPassword())) return "ошибка доступа";
        ServerManager.getInstance().addCommandToHistory(command.split(" ")[0]);
        if(!command.equals("clear")) throw new RuntimeException();
        return executor.clear(com.getUser());
    }

    @Override
    public String getName() {
        return "clear";
    }
}
