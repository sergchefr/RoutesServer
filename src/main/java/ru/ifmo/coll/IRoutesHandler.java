package ru.ifmo.coll;

import java.io.IOException;
import java.util.Comparator;

public interface IRoutesHandler {
    public String add(Route route, String username);
    public String info();
    public String show();
    public String update(int id, Route route);
    public Route getById(long id);
    public String clear();
    public String addIfMax(Route route);
    public String addIfMin(Route route);
    public String avgdistance();
    public String printAsc();
    public String printAscDist();
    public Route[] getAllRoutes();
    public String removeById(long id);
    public long size();
}
