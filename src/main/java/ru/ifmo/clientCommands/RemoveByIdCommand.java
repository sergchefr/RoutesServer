package ru.ifmo.clientCommands;

import ru.ifmo.ServerManager;
import ru.ifmo.coll.IRoutesHandler;
import ru.ifmo.passwordmanager.PasswordManager;
import ru.ifmo.transfer.Request;

import java.io.IOException;

public class RemoveByIdCommand implements Icommand{
    private IRoutesHandler executor;

    public RemoveByIdCommand(IRoutesHandler executor) {
        this.executor = executor;
    }

    @Override
    public String execute(Request com) {
        String command = com.getCommand();

        if (PasswordManager.getInstance().checkPassword(com.getUser(), com.getPassword())) {
            ServerManager.getInstance().addCommandToHistory(command.split(" ")[0]);
            Integer id = parceCommand(command);
            if (id == null) return "ошибка при выполнении команды";
            return executor.removeById(id, com.getUser());
        } else {
            return "ошибка доступа";
        }
    }

    private Integer parceCommand(String command){
        String[] args = command.split(" ");
        if(!args[0].equals("remove_by_id")) throw new RuntimeException();
        Integer id = null;
        for (String arg : args) {
            switch (arg.split("=")[0]) {
                case "id":
                    id = Integer.parseInt(arg.split("=")[1]);
                    break;
            }
        }
        return id;
    }

    @Override
    public String getName() {
        return "remove_by_id";
    }
}
