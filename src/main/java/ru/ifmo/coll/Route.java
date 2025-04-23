package ru.ifmo.coll;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

/** Класс, характеризующий некий маршрут.*/
public class Route implements Comparable{
    private final Long id;//Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private static Long nextid=1L;
    private final String name; //Поле не может быть null, Строка не может быть пустой
    //private Coordinates coordinates; //Поле не может быть null
    private final java.util.Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private final Location from; //Поле может быть null
    private final Location to; //Поле не может быть null
    private final double distance; //Поле не может быть null, Значение поля должно быть больше 1


    public Route(Long id, String name, Date creationDate, Location from, Location to, double distance)throws IOException {
        if(id ==null)throw new IOException("id can`t be null");
        if(Objects.equals(name, ""))throw new IOException("name can`t be null or empty");
        if(from ==null)throw new IOException("from location can`t be null");
        if(to ==null)throw new IOException("destination location can`t be null");
        if(name.strip().isEmpty())throw new IOException("name can`t be null");
        if(distance<0) throw new IOException("distance can't be negative");
        if(id<1)throw new IOException("id can't be negative or zero");
        if(id >=nextid) nextid=id+1;

        this.id = id;
        this.name = name;
        //this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    public Route(String name,Location from, Location to, double distance) throws IOException{
        if(Objects.equals(name, ""))throw new IOException("name can`t be null or empty");
        if(from ==null)throw new IOException("from location can`t be null");
        if(to ==null)throw new IOException("destination location can`t be null");
        if(name.strip().isEmpty())throw new IOException("name can`t be null");
        if(distance<0) throw new IOException("distance can't be negative");

        id=nextid;
        nextid++;
        this.name = name;
        //this.coordinates = coordinates;
        this.from = from;
        this.to = to;
        this.distance = distance;
        this.creationDate=new Date();
    }

    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", name='" + name + '\'' +
                //", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", from=" + from +
                ", to=" + to +
                ", distance=" + distance +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        return (int)(this.id-((Route)o).id);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return Objects.equals(name, route.name) && Objects.equals(from, route.from) && Objects.equals(to, route.to) && Objects.equals(distance, route.distance);
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public double getDistance() {
        return distance;
    }

    public Location getFromLocation(){return from;}

    public Location getToLocation(){return to;}
}
