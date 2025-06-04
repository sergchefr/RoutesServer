package ru.ifmo.passwordmanager;

import ru.ifmo.SQLservices.User;
import ru.ifmo.coll.DatabaseException;
import ru.ifmo.SQLservices.DatabaseManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import ru.ifmo.SQLservices.DatabaseUserManager;

public class PasswordManager {
    private HashMap<String, String> users;
    private static volatile PasswordManager instance;

    private PasswordManager() {
        users = new HashMap<>();
        User[] usersDB = DatabaseUserManager.getInstance().getAllUsers();
        //TODO nullcheck
        for (User user : usersDB) {
            users.put(user.getUsername(),user.getPasswordHashed());
            System.out.println(user.getUsername()+"--->"+user.getPasswordHashed());
        }
    }

    public static PasswordManager getInstance(){
        if(instance == null){
            synchronized (PasswordManager.class){
                if(instance==null){
                    instance = new PasswordManager();
                }
            }

        }return instance;
    }

    public String addUser(String username, String password){
        //TODO добавить загрузку тз бд новых паролей
        try {
            MessageDigest digester = MessageDigest.getInstance("SHA-512");
            byte[] input = password.getBytes();
            byte[] digest = digester.digest(input);
            DatabaseUserManager.getInstance().addUserToDatabase(username, new String(digest));
            users.put(username, new String(digest));
            return "пользователь добавлен";

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }catch (DatabaseException e){
            return "такой пользователь уже существует";
        }
    }

    public boolean checkPassword(String username, String password){
        try {
            if(username==null|password==null) return false;
            MessageDigest digester = MessageDigest.getInstance("SHA-512");
            byte[] input = password.getBytes();
            byte[] digest = digester.digest(input);
            if(digest==null) return false;
            return users.get(username).equals(new String(digest));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
