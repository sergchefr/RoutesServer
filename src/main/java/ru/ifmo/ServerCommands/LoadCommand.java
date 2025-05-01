package ru.ifmo.ServerCommands;

import ru.ifmo.Commands;
import ru.ifmo.clientCommands.Icommand;

public class LoadCommand implements Icommand {
    Commands executor;

    public LoadCommand(Commands executor) {
        this.executor = executor;
    }

    @Override
    public String execute(String command) {
        String[] com = command.split(" ");
        if (com.length!=2) return "должен быть 1 аргумент";
        return executor.load(com[1]);
    }

    @Override
    public String getName() {
        return "load";
    }
}