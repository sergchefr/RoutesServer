package ru.ifmo;

import ru.ifmo.coll.Route;

/** интерфейс с командами управления серверной частью*/
public interface Commands {
    String save(String filename);
    String load(String filename);
    String add(Route route);
    String info();
    String show();
    String update(Long id, Route obj);
    String removeById(long id);
    String clear();
    String exit();
    String addIfMax(Route route);
    String addIfMin(Route route);
    String avgdistance();
    String printAsc();
    String printAscDist();
    String showHistory();
    void addCommandToHistiry(String com);
}
