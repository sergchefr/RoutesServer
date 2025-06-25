package ru.ifmo.SQLservices;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SQLconnectionManager {
    private static final int POOL_SIZE = 10;

    private final BlockingQueue<Connection> connectionPool;

    public SQLconnectionManager() throws SQLException {
        String url = "jdbc:postgresql://pg/studs";
        String user = "s468005";
        String password = "dwMa7WeQx03E5HC2";

        this.connectionPool = new ArrayBlockingQueue<>(POOL_SIZE);

        for (int i = 0; i < POOL_SIZE; i++) {
            Connection connection = DriverManager.getConnection(url, user, password);
            if(connection==null) throw new RuntimeException();
            connectionPool.add(connection);
        }
    }

    public Connection getConnection() throws InterruptedException {
        return connectionPool.take(); // блокирующий вызов, если соединений нет
    }

    public void releaseConnection(Connection connection){
        try {
            if (connection != null && !connection.isClosed()) {
                connectionPool.offer(connection);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
