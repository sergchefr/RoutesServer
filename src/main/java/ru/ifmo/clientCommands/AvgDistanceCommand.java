package ru.ifmo.clientCommands;
import ru.ifmo.Commands;

public class AvgDistanceCommand implements Icommand {
    private Commands executor;

    public AvgDistanceCommand(Commands executor) {
        this.executor = executor;
    }

    @Override
    public String execute(String command) {
        executor.addCommandToHistory(command.split(" ")[0]);
        if(!command.equals("avg_distance")) throw new RuntimeException();
        return executor.avgdistance();
    }

    @Override
    public String getName() {
        return "avg_dist";
    }
}
