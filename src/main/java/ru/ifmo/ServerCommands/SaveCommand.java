package ru.ifmo.ServerCommands;

import ru.ifmo.ServerManager;
import ru.ifmo.clientCommands.Icommand;

public class SaveCommand implements Icommand {
    private ServerManager executor;

    public SaveCommand() {
        this.executor = ServerManager.getInstance();
    }

    @Override
    public String execute(String command) {
        String[] com = command.split(" ");
        if (com.length!=2) return "должен быть 1 аргумент";
        return executor.save(com[1]);
    }

    @Override
    public String getName() {
        return "save";
    }
}
