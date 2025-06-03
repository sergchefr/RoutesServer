package ru.ifmo.passwordmanager;

import ru.ifmo.coll.DatabaseManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import java.util.HashMap;

public class PasswordManager {
    private HashMap<String, String> users;
    private static volatile PasswordManager instance;

    private PasswordManager() {
        users = new HashMap<>();
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
            DatabaseManager.getInstance().addUser(username, new String(digest));
            users.put(username, new String(digest));
            return "пользователь добавлен";
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }catch (SQLIntegrityConstraintViolationException e){
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
