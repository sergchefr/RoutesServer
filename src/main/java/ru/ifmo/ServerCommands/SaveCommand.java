package ru.ifmo.ServerCommands;

import ru.ifmo.ServerManager;
import ru.ifmo.clientCommands.Icommand;
import ru.ifmo.transfer.Request;

public class SaveCommand implements Icommand {
    private ServerManager executor;

    public SaveCommand() {
        this.executor = ServerManager.getInstance();
    }

    @Override
    public String execute(Request com) {
        String command = com.getCommand();
        String[] coms = command.split(" ");
        if (coms.length!=2) return "должен быть 1 аргумент";
        return executor.save(coms[1]);
    }

    @Override
    public String getName() {
        return "save";
    }
}
