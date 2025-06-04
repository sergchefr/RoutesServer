package ru.ifmo.coll;

import ru.ifmo.SQLservices.DatabaseManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ProxyCacheArray implements IRoutesHandler{
    private final HashMap<Integer, Route> localcoll;
    private final DatabaseManager database;
    private final Date initDate;
    private ReentrantReadWriteLock lock;
    private boolean usedelay;

    public ProxyCacheArray(){
        localcoll = new HashMap<>();
        initDate = new Date();
        lock = new ReentrantReadWriteLock();
        database = DatabaseManager.getInstance();
        usedelay = true;
        // TODO загрузка из БД
    }

    @Override
    public String add(Route route, String username) {
        lock.writeLock().lock();

        if(usedelay){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            int id = database.addRoute(route, username);
            route.setId(id);
            localcoll.put(id, route);
            return "путь успешно добавлен. Его id: "+id;
        }catch (DatabaseException e){
            return "ошибка добавления";
        }finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public String info() {
        lock.readLock().lock();
        if(usedelay){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        try{
            return "размер:"+ localcoll.size()+", дата инициализаии:"+initDate;
        }finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public String show() {
        return "";
    }

    @Override
    public String update(int id, Route route) {
        return "";
    }

    @Override
    public Route getById(long id) {
        return null;
    }

    @Override
    public String clear() {
        return "";
    }

    @Override
    public String addIfMax(Route route) {
        return "";
    }

    @Override
    public String addIfMin(Route route) {
        return "";
    }

    @Override
    public String avgdistance() {
        return "";
    }

    @Override
    public String printAsc() {
        return "";
    }

    @Override
    public String printAscDist() {
        return "";
    }

    @Override
    public Route[] getAllRoutes() {
        return new Route[0];
    }

    @Override
    public String removeById(long id) {
        return "";
    }

    @Override
    public long size() {
        return 0;
    }
}
