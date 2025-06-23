package ru.ifmo.SQLservices;

import ru.ifmo.coll.Location;
import ru.ifmo.coll.Route;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseManager {
    private volatile static DatabaseManager instance;
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
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO routes (routename, fromname, fromx, fromy, fromz, toname, tox, toy, toz, distance, owner_id)" +
                            "   VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, (SELECT id FROM users1 WHERE username = ?))", Statement.RETURN_GENERATED_KEYS);
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
            throw new RuntimeException(e);
        }finally{
            if(conn!=null) connectionManager.releaseConnection(conn);
        }
    }

    public int addIfMax(Route route, String ownerName) throws DatabaseException{
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO routes (routename, fromname, fromx, fromy, fromz, toname, tox, toy, toz, distance, owner_id)\n" +
                            "SELECT ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, (SELECT id FROM users1 WHERE username = ?)\n" +
                            "WHERE NOT EXISTS (\n" +
                            "    SELECT 1 FROM routes WHERE distance >= ?\n" +
                            ");\n",Statement.RETURN_GENERATED_KEYS);
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
        }finally{
            if(conn!=null) connectionManager.releaseConnection(conn);
        }
    }

    public int addIfMin(Route route, String ownerName) throws DatabaseException{
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO routes (routename, fromname, fromx, fromy, fromz, toname, tox, toy, toz, distance, owner_id)\n" +
                            "SELECT ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, (SELECT id FROM users1 WHERE username = ?)\n" +
                            "WHERE NOT EXISTS (\n" +
                            "    SELECT 1 FROM routes WHERE distance <= ?\n" +
                            ");\n",Statement.RETURN_GENERATED_KEYS);
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
        }finally{
            if(conn!=null) connectionManager.releaseConnection(conn);
        }
    }

    public void deleteRouteByID(int routeid, String username) throws DatabaseException{
        Connection conn = null;
        try{
            conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(
                    "DELETE FROM routes WHERE id = ? AND owner_id = (SELECT id FROM users1 WHERE username = ?);");
            statement.setInt(1, routeid);
            statement.setString(2,username);
            if (statement.executeUpdate()==0) {
                throw new DatabaseException("невозможно удалить данный элемент");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            if(conn!=null) connectionManager.releaseConnection(conn);
        }
    }

    public ArrayList<Route> getAllRoutes(){
        ArrayList<Route> routes = new ArrayList<>();
        Connection conn = null;
        try{
            conn= getConnection();
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT routes.id as rtid, routename, fromname, fromx, fromy, fromz, toname, tox, toy, toz, distance, creation_date, username FROM routes JOIN users1 ON routes.owner_id = users1.id");
            ResultSet result = statement.executeQuery();
            while(result.next()){
                //System.out.println(result.getBigDecimal(1);
                Route temp = new Route(
                        result.getInt("rtid"),
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
                        result.getDouble("distance"));
                temp.setOwnername(result.getString("username"));
                routes.add(temp);
            }
            return routes;

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }finally {
            if(conn!=null) connectionManager.releaseConnection(conn);
        }

    }

    public Route getRouteByID(int id) throws DatabaseException{
        Connection conn = null;
        try {
            conn = getConnection();
            Route route;
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT id, routename, fromname, fromx, fromy, fromz, toname, tox, toy, toz, distance, creation_date, username FROM routes JOIN users1 ON routes.owner_id = users1.id WHERE routes.id = ?");
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
                route.setOwnername(result.getString("username"));
                return route;
            }
            throw new DatabaseException("отсутствует элемент с таким id");
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }finally {
            if(conn!=null) connectionManager.releaseConnection(conn);
        }
    }

    public void updateRoute(int id, Route route, String username)throws DatabaseException{
        //TODO check
        Connection conn = null;
        try{
            conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(
                    "UPDATE routes SET" +
                            " routename = ?," +
                            " fromname = ?," +
                            " fromx = ?," +
                            " fromy = ?," +
                            " fromz = ?," +
                            " toname = ?," +
                            " tox = ?," +
                            " toy = ?," +
                            " toz = ?," +
                            " distance = ?," +
                            " owner_id = (SELECT id FROM users1 WHERE username = ?)" +
                            "  WHERE id = ?" +
                            " AND" +
                            " owner_id = (SELECT id FROM users1 WHERE username = ?)");
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
            statement.setString(11, username);

            statement.setInt(12, route.getId());
            statement.setString(13, username);

            if (statement.executeUpdate() == 0) {
                throw new DatabaseException("объект не обновлен");
            }
        }catch (SQLIntegrityConstraintViolationException e){
            throw new DatabaseException("ошибка обновления в базе данных: не подходит по ограничениям объекта");
        }catch (SQLException e){
            //TODO сделать нормальную обработку исключений
            throw new RuntimeException(e);
        }finally {
            if(conn!=null) connectionManager.releaseConnection(conn);
        }
    }

    public ArrayList<Integer> deleteForUser(String username){
        ArrayList<Integer> ids = new ArrayList<>();
        Connection conn = null;
        try{
            conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(
                    "DELETE FROM routes WHERE owner_id = (SELECT id FROM users1 WHERE username = ?) RETURNING id");
            statement.setString(1,username);
            var res = statement.executeQuery();
            while (res.next()){
                ids.add(res.getInt("id"));
            }
            return ids;
        }catch (SQLException e ){
            throw  new RuntimeException(e);
        }finally {
            if(conn!=null) connectionManager.releaseConnection(conn);
        }
    }

    public int size(){
        Connection conn = null;
        try{
            conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT COUNT(*) FROM routes");
            ResultSet result = statement.executeQuery();
            result.next();
            return result.getInt("count");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            if(conn!=null) connectionManager.releaseConnection(conn);
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
