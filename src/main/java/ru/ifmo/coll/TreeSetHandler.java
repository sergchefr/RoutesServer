package ru.ifmo.coll;

import java.io.IOException;
import java.util.*;

/**
 * Класс, управляющий коллекцией
 */
public class TreeSetHandler implements IRoutesHandler{
    private final TreeSet<Route> coll;
    private final Date initDate;

    public TreeSetHandler()  {
        coll = new TreeSet<>();
        this.initDate = new Date();
    }
    @Override
    public String add(Route route, String username){
        Route oldRoute;
        if(coll.contains(route)) {
            oldRoute = getById(route.getId());
            if (oldRoute.equals(route)) return "element is already in the collection";
        }
        try {
            while(!coll.add(route)){
                route = new Route(route.getId()+1, route.getName(), route.getCreationDate(),route.getFromLocation(),route.getToLocation(), route.getDistance());
            }
        } catch (IOException e) {
            return "unexpected adding error\n";
        }
        return "element added\n";
    }
    @Override
    public String info(){
        return "initialisation date: "+initDate+"\n"+"size: "+ coll.size()+"\n"+ "collection class: " + coll.getClass()+"\n";
    }
    @Override
    public String show(){
        StringBuilder s = new StringBuilder();
        if(coll.isEmpty()) return "collection is empty\n";
        coll.stream().sorted(Comparator.comparing(Route::getName)).forEach(x-> s.append(x.toString()).append("\n"));
        return s.toString();
    }
    @Override
    public String update(int id, Route route, String username){
        for (Route o : coll) {
            if(o.getId().equals(id)) {
                coll.remove(o);
                try{
                    if(coll.add(new Route(id, route.getName(),route.getCreationDate(), route.getFromLocation(),route.getToLocation(), route.getDistance()))){
                        return "element updated by id\n";
                    }else{
                        return "element wasn`t updated\n";
                    }
                }catch (IOException e){
                    return "error\n";
                }

            }
        }
        return "element with this id doesn`t exist\n";
    }
    @Override
    public String removeById(int id, String username){
        for (Route o : coll) {
            if(o.getId()==id) {
                coll.remove(o);
                return "element deleted\n";
            }
        }
        return "element with this id doesn`t exist\n";
    }

    private Route getById(int id){
        for (Route o : coll) {
            if (o.getId() == id) {
                return o;
            }
        }
        return null;
    }
    @Override
    public String clear(String username){
        coll.clear();
        return "collection cleared\n";
    }
    @Override
    public String addIfMax(Route route, String username){
        double maxd=0;
        for (Route o : coll) {
            if(o.getDistance()>maxd) maxd= o.getDistance();
        }
        if(route.getDistance()>maxd) {
            coll.add(route);
            return "element was added\n";
        }
        return "element is not max\n";
    }
    @Override
    public String addIfMin(Route route, String username){

        double mind=Float.POSITIVE_INFINITY;
        for (Route o : coll) {
            if(o.getDistance()<mind) mind= o.getDistance();
        }
        if(route.getDistance()<mind) {
            coll.add(route);
            return "element was added\n";
        }
        return "element is not min\n";
    }
    @Override
    public String avgdistance(){
        double s=0;
        if(coll.isEmpty()) return "collection is empty\n";
        for (Route o : coll) {
            s=s+ o.getDistance();
        }
        return s/coll.size()+"\n";
    }
    @Override
    public String printAsc(){
        StringBuilder s = new StringBuilder();
        if(coll.isEmpty()) return "collection is empty\n";
        coll.stream().sorted(Comparator.comparing(Route::getDistance)).forEach(x-> s.append(x.toString()).append("\n"));
        return s.toString();

    }
    @Override
    public String printAscDist(){
        StringBuilder s = new StringBuilder();
        if(coll.isEmpty()) return "collection is empty\n";
        coll.stream().sorted(Comparator.comparing(Route::getDistance)).forEach(x-> s.append(x.getDistance()).append(", "));
        return s.toString().substring(0, s.length()-2);
    }
    @Override
    public Route[] getAllRoutes(){
       return coll.toArray(new Route[0]);
    }
    @Override
    public long size(){
        return coll.size();
    }
}
//add RouteName=zov ToLocation=home fromx=1 fromy=2 fromz=3 FromLocation=front tox=0 toy=2 toz=1 distance=1488
//add RouteName=goida ToLocation=goidaaa fromx=1 fromy=2 fromz=3 FromLocation=zzzzz tox=0 toy=2 toz=1 distance=1488