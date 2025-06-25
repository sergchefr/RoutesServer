package ru.ifmo.migration;

import ru.ifmo.coll.Route;

import java.io.IOException;
import java.util.ArrayList;

public interface Loader {
    ArrayList<Route> getRoutes(String filename)throws IOException, IllegalParamException;
}
