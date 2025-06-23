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

    public void prepareBD(boolean readDB){
        boolean hasUsers = false;
        boolean hasRoutes = false;

        //безбожно дропает базу данных

        if (!readDB) {
            try {
                PreparedStatement statement = getConnection().prepareStatement("DROP TABLE IF EXISTS users1, routes;");
                statement.execute();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("проверка существования БД...");
        try (PreparedStatement stmt = getConnection().prepareStatement(
                "SELECT table_name FROM information_schema.tables");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String tableName = rs.getString("table_name");
                if (tableName.equals("users1")) hasUsers = true;
                if (tableName.equals("routes")) hasRoutes = true;
            }

            if (hasUsers) System.out.println("таблица \"users1\" найдена");
            else System.out.println("таблица \"users\" не найдена");

            if (hasRoutes) System.out.println("таблица \"routes\" найдена");
            else System.out.println("таблица \"routes\" не найдена");


            if (!hasUsers) {
                try{
                    PreparedStatement createusers = getConnection().prepareStatement(
                            "CREATE TABLE users1 (" +
                                    "    id SERIAL PRIMARY KEY," +
                                    "    username TEXT UNIQUE NOT NULL," +
                                    "    password_hash TEXT NOT NULL);");

                    createusers.execute();
                    System.out.println("таблица \"users1\" создана");
                } catch (SQLException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }

            if (!hasRoutes) {
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
                                    "   owner_id INTEGER NOT NULL REFERENCES users1(id) ON DELETE CASCADE);");

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
