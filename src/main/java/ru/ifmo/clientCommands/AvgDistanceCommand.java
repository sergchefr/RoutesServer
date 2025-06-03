package ru.ifmo.clientCommands;
import ru.ifmo.ServerManager;
import ru.ifmo.coll.IRoutesHandler;

public class AvgDistanceCommand implements Icommand {
    private IRoutesHandler executor;

    public AvgDistanceCommand(IRoutesHandler executor) {
        this.executor = executor;
    }

    @Override
    public String execute(String command) {
        ServerManager.getInstance().addCommandToHistory(command.split(" ")[0]);
        if(!command.equals("avg_distance")) throw new RuntimeException();
        return executor.avgdistance();
    }

    @Override
    public String getName() {
        return "avg_dist";
    }
}
