package ru.ifmo.SQLservices;

import ru.ifmo.coll.DatabaseException;
import ru.ifmo.coll.Location;
import ru.ifmo.coll.Route;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseManager {
    private static DatabaseManager instance;
    private SQLconnectionManager connectionManager;

    private DatabaseManager() {
        try {
            connectionManager = new SQLconnectionManager();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

/**
 * принимает добаляемый путь и имя пользователя, возвращает сгенерированный id.
 * @throws DatabaseException если путь не был добавлен
 */
    public int addRoute(Route route, String ownerName)throws DatabaseException{
        try {
            PreparedStatement statement = getConnection().prepareStatement(
                    "INSERT INTO routes (routename, fromname, fromx, fromy, fromz, toname, tox, toy, toz, distance, owner_id)" +
                            "   VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, (SELECT id FROM users WHERE username = ?))", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, route.getName());

            statement.setString(2, route.getFromLocation().getName());
            statement.setInt(3, route.getFromLocation().getX());
            statement.setInt(4, route.getFromLocation().getY());
            statement.setInt(5, route.getFromLocation().getZ());

            statement.setString(6, route.getToLocation().getName());
            statement.setInt(7, route.getToLocation().getX());
            statement.setInt(8, route.getToLocation().getY());
            statement.setInt(9, route.getToLocation().getZ());

            statement.setDouble(10, route.getDistance());
            statement.setString(11, ownerName);

            //System.out.println(statement);
            if (statement.executeUpdate() == 0) {
                throw new DatabaseException("объект не добавлен");
            }
            var keys = statement.getGeneratedKeys();
            keys.next();
           return keys.getInt("id");

        }catch (SQLIntegrityConstraintViolationException e){
            throw new DatabaseException("ошибка добавления в базу данных: не подходит по ограничениям объекта");
        }catch (SQLException e){
            //TODO сделать нормальную обработку исключений
            throw new RuntimeException(e);
        }
    }

    public void deleteRouteByID(int routeid, String username) throws DatabaseException{
        try{
            PreparedStatement statement = getConnection().prepareStatement(
                    "DELETE FROM routes WHERE id = (SELECT id FROM users WHERE username = ?);");
            statement.setString(1,username);
            if (statement.executeUpdate()==0) {
                throw new DatabaseException("невозможно удалить данный элемент");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Route> getAllRoutes(){
        ArrayList<Route> routes = new ArrayList<>();
        try{
            PreparedStatement statement = getConnection().prepareStatement(
                    "SELECT id, routename, fromname, fromx, fromy, fromz, toname, tox, toy, toz, distance, creation_date FROM ROUTES");
            ResultSet result = statement.executeQuery();
            while(result.next()){
                routes.add(new Route(
                        result.getInt("id"),
                        result.getString("routename"),
                        result.getDate("creation_date"),
                        new Location(
                                result.getInt("fromx"),
                                result.getInt("fromy"),
                                result.getInt("fromz"),
                                result.getString("fromname")),
                        new Location(result.getInt("tox"),
                                result.getInt("toy"),
                                result.getInt("toz"),
                                result.getString("toname")),
                        result.getDouble("distance")));
            }
            return routes;

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Route getRouteByID(int id) throws DatabaseException{
        try {
            Route route;
            PreparedStatement statement = getConnection().prepareStatement(
                    "SELECT id, routename, fromname, fromx, fromy, fromz, toname, tox, toy, toz, distance, creation_date FROM ROUTES WHERE id = ?");
            statement.setInt(1, id);
            //System.out.println(statement);
            ResultSet result = statement.executeQuery();
            if(result.next()){
                route = (new Route(
                        result.getInt("id"),
                        result.getString("routename"),
                        result.getDate("creation_date"),
                        new Location(
                                result.getInt("fromx"),
                                result.getInt("fromy"),
                                result.getInt("fromz"),
                                result.getString("fromname")),
                        new Location(result.getInt("tox"),
                                result.getInt("toy"),
                                result.getInt("toz"),
                                result.getString("toname")),
                        result.getDouble("distance")));
                return route;
            }
            throw new DatabaseException("отсутствует элемент с таким id");
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static DatabaseManager getInstance() {
        if (instance == null) {
            synchronized (DatabaseManager.class) {
                if (instance == null) {
                    instance = new DatabaseManager();
                }
            }
        }
        return instance;
    }

    private Connection getConnection(){
        for (int i = 0; i < 10; i++) {
            try {
                return connectionManager.getConnection();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        throw new RuntimeException("это летально: 10 раз подряд прерывание - так нельзя");
    }
}
