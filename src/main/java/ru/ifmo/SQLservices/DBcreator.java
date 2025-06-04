package ru.ifmo.SQLservices;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBcreator {
    private SQLconnectionManager connectionManager;

    public DBcreator() {
        try {
            connectionManager = new SQLconnectionManager();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void prepareBD(){
        boolean hasUsers = false;
        boolean hasRoutes = false;

        //безбожно дропает базу данных
//        try {
//            PreparedStatement statement = connection.prepareStatement("DROP TABLE routes; DROP TABLE users;");
//            statement.execute();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }

        System.out.println("проверка существования БД...");
        try (PreparedStatement stmt = getConnection().prepareStatement(
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
                    PreparedStatement createusers = getConnection().prepareStatement(
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
                    PreparedStatement createRoutes = getConnection().prepareStatement(
                            "CREATE TABLE routes (" +
                                    "   id SERIAL PRIMARY KEY," +
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
