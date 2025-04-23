package ru.ifmo.serverCommands;

public interface Icommand {
    String execute(String command);
    String getName();
}
