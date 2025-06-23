package ru.ifmo.clientCommands;


import ru.ifmo.ServerManager;
import ru.ifmo.coll.IRoutesHandler;
import ru.ifmo.coll.Location;
import ru.ifmo.coll.Route;
import ru.ifmo.passwordmanager.PasswordManager;
import ru.ifmo.transfer.Request;

import java.io.IOException;

public class UpdateCommand implements Icommand {
    private IRoutesHandler executor;

    public UpdateCommand(IRoutesHandler executor) {
        this.executor = executor;
    }

    @Override
    public String execute(Request com) {
        String command = com.getCommand();
        ServerManager.getInstance().addCommandToHistory(command.split(" ")[0]);
        try {
            if(!PasswordManager.getInstance().checkPassword(com.getUser(),com.getPassword())) return "ошибка доступа";
            Object[] args = parceCommand(command);
            Integer id = (Integer) args[0];
            Route route = (Route) args[1];
            if (id == null | route == null) return "ошибка при создании объекта";
            return executor.update(id, route, com.getUser());
        } catch (IOException e) {
            return "ошибка при создании объекта";
        }
    }

    @Override
    public String getName() {
        return "update";
    }

    private Object[] parceCommand(String command) throws IOException {

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
        Integer id = null;

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
                    case "id":
                        id = Integer.parseInt(arg.split("=")[1]);
                        break;
                }
            }
        } catch (NumberFormatException e) {
            throw new IOException("incorrect number format");
        }
        try {
            Object[] h = new Object[2];
            h[0]= id;
            Route route = new Route(RouteName, new Location(fromx, fromy, fromz, FromLocationName), new Location(tox, toy, toz, ToLocationName), distance);
            h[1] = route;
            return h;
        } catch (IOException e) {
            throw new IOException(e);
        }
    }
}
