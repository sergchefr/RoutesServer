package ru.ifmo.coll;

import java.sql.*;

public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;

    private DatabaseManager() {
        String url = "jdbc:postgresql://pg/studs";
        String user = "s468005";
        String password = "dwMa7WeQx03E5HC2";
        boolean hasUsers = false;
        boolean hasRoutes = false;

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        //безбожно дропает базу данных
//        try {
//            PreparedStatement statement = connection.prepareStatement("DROP TABLE routes; DROP TABLE users;");
//            statement.execute();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }

        System.out.println("проверка существования БД...");
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT table_name FROM information_schema.tables");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String tableName = rs.getString("table_name");
                //System.out.println("Found table: " + tableName);
                if (tableName.equals("users")) hasUsers = true;
                if (tableName.equals("routes")) hasRoutes = true;
            }

            if (hasUsers) System.out.println("таблица \"users\" найдена");
            else System.out.println("таблица \"users\" не найдена");

            if (hasRoutes) System.out.println("таблица \"routes\" найдена");
            else System.out.println("таблица \"routes\" не найдена");


            if (hasUsers == false) {
                try{
                    PreparedStatement createusers = connection.prepareStatement(
                        "CREATE TABLE users (" +
                                "    id SERIAL PRIMARY KEY," +
                                "    username TEXT UNIQUE NOT NULL," +
                                "    password_hash TEXT NOT NULL);");

                     createusers.execute();
                     System.out.println("таблица \"users\" создана");
                } catch (SQLException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }

            if (hasRoutes == false) {
                try {
                    PreparedStatement createRoutes = connection.prepareStatement(
                            "CREATE TABLE routes (" +
                                    "id SERIAL PRIMARY KEY," +
                                    "   routename TEXT NOT NULL," +
                                    "   fromname TEXT NOT NULL," +
                                    "   fromx INTEGER NOT NULL," +
                                    "   fromy INTEGER NOT NULL," +
                                    "   fromz INTEGER NOT NULL," +
                                    "   toname TEXT NOT NULL," +
                                    "   tox INTEGER NOT NULL," +
                                    "   toy INTEGER NOT NULL," +
                                    "   toz INTEGER NOT NULL," +
                                    "   creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                                    "   distance DOUBLE PRECISION CHECK (distance > 0) NOT NULL," +
                                    "   owner_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE);");

                     createRoutes.execute();
                    System.out.println("таблица \"routes\" создана");
                } catch (SQLException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        System.out.println("база данных готова!");
    }

    public void addRoute(Route route, int ownerID){
        try {
            PreparedStatement statement = getConnection().prepareStatement(
                    "INSERT INTO routes (routename, fromname, fromx, fromy, fromz, toname, tox, toy, toz, distance, owner_id)" +
                            "   VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
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
            statement.setInt(11, ownerID);

            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            keys.next();
            int generated_id = keys.getInt("id");
            //TODO добаввить кэширование
        }catch (SQLException e){
            //TODO сделать нормальную обработку исключений
            throw new RuntimeException(e);
        }
    }

    public void addUser(String username, String password) throws SQLIntegrityConstraintViolationException{
        try{
            PreparedStatement statement = getConnection().prepareStatement(
                    "INSERT INTO users (username, password)" +
                            "   VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS);
            ResultSet res = statement.getGeneratedKeys();
            res.next();
            int generated_id = res.getInt("id");
        }catch (SQLIntegrityConstraintViolationException e){
            throw new SQLIntegrityConstraintViolationException(e);
        }catch (SQLException e){
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
        return connection;
    }
}
