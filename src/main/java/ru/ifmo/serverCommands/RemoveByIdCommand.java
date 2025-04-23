package ru.ifmo.serverCommands;

import ru.ifmo.Commands;

public class RemoveByIdCommand implements Icommand{
    private Commands executor;

    public RemoveByIdCommand(Commands executor) {
        this.executor = executor;
    }

    @Override
    public String execute(String command) {
        Integer id = parceCommand(command);
        if(id==null) return "ошибка при выполнении команды";
        return executor.removeById(id);
    }

    private Integer parceCommand(String command){
        String[] args = command.split(" ");
        if(!args[0].equals("remove_by_id")) throw new RuntimeException();
        Integer id = null;
        for (String arg : args) {
            switch (arg.split("=")[0]) {
                case "id":
                    id = Integer.parseInt(arg.split("=")[1]);
                    break;
            }
        }
        return id;
    }

    @Override
    public String getName() {
        return "remove_by_id";
    }
}
