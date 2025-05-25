package ru.ifmo.transfer;

import java.io.Serializable;

public class Request implements Serializable {
    private final String command;
    private final String user;
    private final String password;

    public Request(String command, String user, String password) {
        this.command = command;
        this.user = user;
        this.password = password;
    }

    public String getCommand() {
        return command;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
