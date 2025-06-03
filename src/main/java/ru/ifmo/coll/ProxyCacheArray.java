package ru.ifmo.coll;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ProxyCacheArray implements IRoutesHandler{
    private final ArrayList<Route> coll;
    private final Date initDate;
    private ReentrantReadWriteLock lock;
    private boolean usedelay;

    public ProxyCacheArray(){
        coll = new ArrayList<>();
        initDate = new Date();
        lock = new ReentrantReadWriteLock();
        usedelay = true;
    }

    @Override
    public String add(Route route) {
        //lock.lock();
        if(usedelay){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        try{
            if(coll.add(route)) return "маршрут добавлен";
            return "маршрут не добавлен";
        }finally {
            //lock.unlock();
        }
    }

    @Override
    public String info() {
        //lock.lock();
        if(usedelay){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        try{
            return "размер:"+coll.size()+", дата инициализаии:"+initDate;
        }finally {
            //lock.unlock();
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
