package ru.ifmo.serverCommands;

import ru.ifmo.Commands;
import ru.ifmo.coll.Location;
import ru.ifmo.coll.Route;

import java.io.IOException;

public class AddIfMaxCommand implements Icommand{
    Commands executor;

    public AddIfMaxCommand(Commands executor) {
        this.executor = executor;
    }

    @Override
    public String execute(String command) {
        try {
            return executor.addIfMax(parceCommand(command));
        } catch (IOException e) {
            return "ошибка при создании объекта: "+e;
        }
    }

    @Override
    public String getName() {
        return "add_if_max";
    }

    private Route parceCommand(String command) throws IOException {

        String RouteName = null;
        String FromLocationName = null;
        Integer fromx = null;
        Integer fromy = null;
        Integer fromz = null;
        String ToLocationName = null;
        Integer tox = null;
        Integer toy = null;
        Integer toz = null;
        Double distance = null;

        String[] args = command.split(" ");
        if (!args[0].equals("add")) throw new RuntimeException();
        try {
            for (String arg : args) {
                switch (arg.split("=")[0]) {
                    case "RouteName":
                        RouteName = arg.split("=")[1];
                        break;
                    case "FromLocationName":
                        FromLocationName = arg.split("=")[1];
                        break;
                    case "fromx":
                        fromx = Integer.parseInt(arg.split("=")[1]);
                        break;
                    case "fromy":
                        fromy = Integer.parseInt(arg.split("=")[1]);
                        break;
                    case "fromz":
                        fromz = Integer.parseInt(arg.split("=")[1]);
                        break;
                    case "ToLocationName":
                        ToLocationName = arg.split("=")[1];
                        break;
                    case "tox":
                        tox = Integer.parseInt(arg.split("=")[1]);
                        break;
                    case "toy":
                        toy = Integer.parseInt(arg.split("=")[1]);
                        break;
                    case "toz":
                        toz = Integer.parseInt(arg.split("=")[1]);
                        break;
                    case "distance":
                        distance = Double.parseDouble(arg.split("=")[1]);
                        break;
                }
            }
        } catch (NumberFormatException e) {
            throw new IOException("incorrect number format");
        }
        try {
            return new Route(RouteName, new Location(fromx, fromy, fromz, FromLocationName), new Location(tox, toy, toz, ToLocationName), distance);
        } catch (IOException e) {
            throw new IOException(e);
        }
    }
}
