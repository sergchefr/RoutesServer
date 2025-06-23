package ru.ifmo.coll;

import ru.ifmo.SQLservices.DatabaseException;
import ru.ifmo.SQLservices.DatabaseManager;

import java.util.Comparator;
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
        usedelay = false;

        for (Route route : database.getAllRoutes()) {;
            localcoll.put(route.getId(), route);
            //System.out.println(route);
        }
        if(database.size()!=localcoll.size()) System.err.println("ОШИБКА ЗАГРУЗКИ КОЛЛЕКЦИИ: ЗАГРУЖЕНА НЕ ВСЯ КОЛЛЕКЦИЯ");
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
            route.setOwnername(username);
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
            return "размер:"+ localcoll.size()+", дата инициализации:"+initDate;
        }finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public String show() {
        lock.readLock().lock();
        if(usedelay){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            StringBuilder builder = new StringBuilder();
            for (Route value : localcoll.values()) {
                builder.append(value).append("\n");
            }
            String a = builder.toString().strip();
            if(!a.isEmpty()) return a;
            return "коллекция пуста";
        }finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public String update(int id, Route route, String username) {
        lock.writeLock().lock();
        if(usedelay){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        try{
            database.updateRoute(id,route, username);
            localcoll.put(id, route);
            return "путь обновлен";
        }catch (DatabaseException e){
        return "ошибка обновления";
        }finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public String clear(String username) {
        lock.writeLock().lock();
        if(usedelay){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            for (Integer i : database.deleteForUser(username)) {
                localcoll.remove(i);
            }
            return "данные пользователя "+ username +" удалены";
        }finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public String addIfMax(Route route, String username) {
        lock.writeLock().lock();
        if(usedelay){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        try{
            int id = database.addIfMax(route, username);
            route.setId(id);
            route.setOwnername(username);
            localcoll.put(id, route);
            return "путь успешно добавлен. Его id: "+id;
        }catch (DatabaseException e){
            return "путь не добавлен";
        }finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public String addIfMin(Route route, String username) {
        lock.writeLock().lock();
        if(usedelay){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        try{
            int id = database.addIfMin(route, username);
            route.setId(id);
            route.setOwnername(username);
            localcoll.put(id, route);
            return "путь успешно добавлен. Его id: "+id;
        }catch (DatabaseException e){
            return "путь не добавлен";
        }finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public String avgdistance() {
        lock.readLock().lock();
        if(usedelay){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        try{
            double s=0;
            if(localcoll.isEmpty()) return "collection is empty\n";
            for (Route o : localcoll.values()) {
                s=s+ o.getDistance();
            }
            return s/localcoll.size()+"";
        }finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public String printAsc() {
        lock.readLock().lock();
        if(usedelay){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            StringBuilder s = new StringBuilder();
            if(localcoll.isEmpty()) return "collection is empty\n";
            localcoll.values().stream().sorted(Comparator.comparing(Route::getDistance)).forEach(x-> s.append(x.toString()).append("\n"));
            return s.toString().strip();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public String printAscDist() {
        lock.readLock().lock();
        if(usedelay){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            StringBuilder s = new StringBuilder();
            if(localcoll.isEmpty()) return "collection is empty\n";
            localcoll.values().stream().sorted(Comparator.comparing(Route::getDistance)).forEach(x-> s.append(x.getDistance()).append(", "));
            return s.toString().substring(0, s.length()-2);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Route[] getAllRoutes() {
        lock.readLock().lock();
        if(usedelay){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        try{
            return localcoll.values().toArray(new Route[0]);
        }finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public String removeById(int id, String username) {
        lock.writeLock().lock();
        if(usedelay){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        try{
           database.deleteRouteByID(id, username);
           localcoll.remove(id);
           return "путь удален";
        }catch (DatabaseException e){
            return e.getMessage();
        }finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public long size() {
        return localcoll.size();
    }
}
