package ru.ifmo.coll;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/** Класс, характеризующий некий маршрут.*/
public class Route implements Comparable{
    private Integer id;
    private final String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private final java.util.Date creationDate;
    private final Location fromLocation;
    private final Location toLocation;
    private final double distance;
    private String ownername;

    @JsonCreator
    public Route(@JsonProperty("id") Integer id,
                 @JsonProperty("name") String name,

                 @JsonProperty("creationDate")
                     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
                     Date creationDate,

                 @JsonProperty("fromLocation") Location fromLocation,
                 @JsonProperty("toLocation") Location toLocation,
                 @JsonProperty("distance") double distance)throws IOException {
        if(id ==null)throw new IOException("id can`t be null");
        if(Objects.equals(name, ""))throw new IOException("name can`t be null or empty");
        if(fromLocation ==null)throw new IOException("from location can`t be null");
        if(toLocation ==null)throw new IOException("destination location can`t be null");
        if(name.strip().isEmpty())throw new IOException("name can`t be null");
        if(distance<0) throw new IOException("distance can't be negative");

        this.id=id;
        this.name = name;
        this.creationDate = creationDate;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.distance = distance;
        ownername = "unknown";
    }

    public Route(String name,Location from, Location to, double distance) throws IOException{
        if(Objects.equals(name, ""))throw new IOException("name can`t be null or empty");
        if(from ==null)throw new IOException("from location can`t be null");
        if(to ==null)throw new IOException("destination location can`t be null");
        if(name.strip().isEmpty())throw new IOException("name can`t be null");
        if(distance<0) throw new IOException("distance can't be negative");

        this.name = name;
        this.fromLocation = from;
        this.toLocation = to;
        this.distance = distance;
        this.creationDate=new Date();
        ownername = "unknown";
    }

    @Override
    public String toString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return "Route{" +
                "id=" + id +
                ", name='" + name + '\'' +
                //", coordinates=" + coordinates +
                ", creationDate=" + simpleDateFormat.format(creationDate) +
                ", from=" + fromLocation +
                ", to=" + toLocation +
                ", distance=" + distance +
                ", owner=" + ownername+
                '}';
    }

    @Override
    public int compareTo(Object o) {
        return (int)(this.id-((Route)o).id);
    }

    public Integer getId() {
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
        return Objects.equals(name, route.name) && Objects.equals(fromLocation, route.fromLocation) && Objects.equals(toLocation, route.toLocation) && Objects.equals(distance, route.distance);
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public double getDistance() {
        return distance;
    }

    public Location getFromLocation(){return fromLocation;}

    public Location getToLocation(){return toLocation;}

    public void setId(int id){
        this.id=id;
    }

    @JsonProperty("ownername")
    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }

    public String getOwnername() {
        return ownername;
    }
}
