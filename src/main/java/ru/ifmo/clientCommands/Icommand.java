package ru.ifmo.clientCommands;

import ru.ifmo.transfer.Request;

public interface Icommand {
    String execute(Request command);
    String getName();
}
