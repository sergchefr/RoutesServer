package ru.ifmo.coll;

import java.util.Objects;

/** Класс, характеризующий некую локацию. используется в {@link Route}*/
public class Location {
    private final int x; //Поле не может быть null
    private final int y; //Поле не может быть null
    private final int z; //Поле не может быть null
    private final String name; //Строка не может быть пустой, Поле не может быть null

    public Location(int x, int y, int z, String name) {
        if(Objects.equals(name,"")) throw new NumberFormatException();
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
        //System.out.println("location created");
    }

    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", name='" + name + '\'' +
                '}';
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(name, location.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
