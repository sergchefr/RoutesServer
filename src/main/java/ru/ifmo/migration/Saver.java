package ru.ifmo.migration;

import ru.ifmo.coll.Route;

import java.io.IOException;

public interface Saver {
    void writeRoute(Route[] routes, String filename)throws IOException;
}
