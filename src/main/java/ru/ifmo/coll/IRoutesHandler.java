package ru.ifmo.coll;

import java.io.IOException;
import java.util.Comparator;

public interface IRoutesHandler {
    public String add(Route route, String username);
    public String info();
    public String show();
    public String update(int id, Route route, String username);
    public String clear(String username);
    public String addIfMax(Route route, String username);
    public String addIfMin(Route route, String username);
    public String avgdistance();
    public String printAsc();
    public String printAscDist();
    public Route[] getAllRoutes();
    public String removeById(int id, String username);
    public long size();
}
