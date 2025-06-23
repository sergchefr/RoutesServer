package ru.ifmo.clientCommands;
import ru.ifmo.ServerManager;
import ru.ifmo.coll.IRoutesHandler;
import ru.ifmo.passwordmanager.PasswordManager;
import ru.ifmo.transfer.Request;

public class AvgDistanceCommand implements Icommand {
    private IRoutesHandler executor;

    public AvgDistanceCommand(IRoutesHandler executor) {
        this.executor = executor;
    }

    @Override
    public String execute(Request com) {
        String command = com.getCommand();
        if(!PasswordManager.getInstance().checkPassword(com.getUser(),com.getPassword())) return "ошибка доступа";
        ServerManager.getInstance().addCommandToHistory(command.split(" ")[0]);
        if(!command.equals("avg_distance")) throw new RuntimeException();
        return executor.avgdistance();
    }

    @Override
    public String getName() {
        return "avg_dist";
    }
}
