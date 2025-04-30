package ru.ifmo.serverCommands;
import ru.ifmo.Commands;

public class AvgDistanceCommand implements Icommand {
    private Commands executor;

    public AvgDistanceCommand(Commands executor) {
        this.executor = executor;
    }

    @Override
    public String execute(String command) {
        if(!command.equals("avg_distance")) throw new RuntimeException();
        return executor.avgdistance();
    }

    @Override
    public String getName() {
        return "avg_dist";
    }
}
