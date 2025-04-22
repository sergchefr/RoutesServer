package ru.ifmo.transfer;

import java.io.Serializable;

public class Request implements Serializable {
    private final String command;

    private static final long serialVersionUID = 1L;

    public Request(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
