package ru.ifmo.clientCommands;

import ru.ifmo.Commands;

public class GetConfigCommand implements Icommand{

    private Commands executor;

    public GetConfigCommand(Commands executor) {
        this.executor = executor;
    }

    @Override
    public String execute(String command) {
        executor.addCommandToHistory(command.split(" ")[0]);
        if(!command.equals("GetConfigCommand")) throw new RuntimeException();
        return executor.getConfig();
    }

    @Override
    public String getName() {
        return "GetConfigCommand";
    }
}
