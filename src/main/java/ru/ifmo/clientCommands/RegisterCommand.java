package ru.ifmo.clientCommands;

import ru.ifmo.Commands;
import ru.ifmo.passwordmanager.PasswordManager;

public class RegisterCommand implements Icommand{
    private PasswordManager passwordManager;
    private String username;
    private String password;

    public RegisterCommand() {
        passwordManager = PasswordManager.getInstance();
    }

    @Override
    public String execute(String command) {
        parcecommand(command);
        if(username==null|password==null) return "команда введена неверно";
        passwordManager.addUser(username, password);
        return "пользователь добавлен";
    }

    @Override
    public String getName() {
        return "register";
    }

    private void parcecommand(String command){
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
    }
}
