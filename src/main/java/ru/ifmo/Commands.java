package ru.ifmo;

import ru.ifmo.coll.Route;

/** интерфейс с командами управления серверной частью*/
public interface Commands {
    String save(String filename);
    String load(String filename);
    String add(Route route);
    String info();
    String show();
    String update(Integer id, Route obj);
    String removeById(long id);
    String clear();
    String exit();
    String addIfMax(Route route);
    String addIfMin(Route route);
    String avgdistance();
    String printAsc();//TODO use lambda
    String printAscDist();//TODO use lambda
    String showHistory();
    String getConfig();
    void addCommandToHistiry(String com);
}
