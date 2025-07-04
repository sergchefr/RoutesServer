package ru.ifmo.SQLservices;

import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;

public class DatabaseUserManager{
    private volatile static DatabaseUserManager instance;
    private SQLconnectionManager connectionManager;

    private DatabaseUserManager() {
        try {
            this.connectionManager = new SQLconnectionManager();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addUserToDatabase(String username, String passwordHashed) throws DatabaseException {
        try{
            PreparedStatement statement = getConnection().prepareStatement(
                    "INSERT INTO users1 (username, password_hash)" +
                            "   VALUES(?, ?)");
            statement.setString(1,username);
            statement.setString(2,passwordHashed);
            statement.executeUpdate();
        }catch (SQLIntegrityConstraintViolationException e){
            throw new DatabaseException(e.getMessage());
        }catch (SQLException e){
            if (e.getSQLState().equals("23505")) throw new DatabaseException("Пользователь с таким именем уже существует.");
            throw new RuntimeException(e);
        }
    }

    public void deleteUser(String username) throws DatabaseException{
        try{
            PreparedStatement statement = getConnection().prepareStatement(
                    "DELETE FROM users1 WHERE username = ?");
            statement.setString(1,username);

            if (statement.executeUpdate()==0) throw new DatabaseException("пользователя с таким именем не существует");

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public User[] getAllUsers() throws DatabaseException{
        try{
            ArrayList<User> users= new ArrayList<>();
            PreparedStatement statement = getConnection().prepareStatement(
                    "SELECT username, password_hash FROM users1");

            ResultSet res = statement.executeQuery();
            while (res.next()){
                users.add(new User(res.getString("username"), res.getString("password_hash")));
            }
            return users.toArray(users.toArray(new User[0]));
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
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

    public static DatabaseUserManager getInstance() {
        if(instance==null){
            synchronized(DatabaseUserManager.class){
                if(instance==null){
                    instance=new DatabaseUserManager();
                }
            }
        }
        return instance;
    }

}
