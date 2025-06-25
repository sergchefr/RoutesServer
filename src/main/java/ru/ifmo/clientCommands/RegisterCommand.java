package ru.ifmo.clientCommands;

import ru.ifmo.passwordmanager.PasswordManager;
import ru.ifmo.transfer.Request;

public class RegisterCommand implements Icommand{
    private PasswordManager passwordManager;
    public RegisterCommand() {
        passwordManager = PasswordManager.getInstance();
    }
    @Override
    public String execute(Request com) {
        String command = com.getCommand();
        String[] args = parcecommand(command);
        if(args==null) return "команда введена неверно";
        return passwordManager.addUser(args[0],args[1]);
    }
    @Override
    public String getName() {
        return "register";
    }
    private String[] parcecommand(String command){
        String username = null;
        String password = null;
        var args = command.strip().split(" ");
        for (String arg : args) {
            switch (arg.split("=")[0]) {
                case "username":
                    username = arg.split("=")[1];
                    break;
                case "password":
                    password = arg.split("=")[1];
                    break;
            }
        }
        if(username==null|password==null) return null;
        return new String[] {username,password};
    }
}
