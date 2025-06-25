package ru.ifmo.passwordmanager;

import ru.ifmo.SQLservices.User;
import ru.ifmo.SQLservices.DatabaseException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import ru.ifmo.SQLservices.DatabaseUserManager;

public class PasswordManager {
    private HashMap<String, String> users;
    private static volatile PasswordManager instance;

    private PasswordManager() {
        users = new HashMap<>();

        User[] usersDB = null;
        try {
            usersDB = DatabaseUserManager.getInstance().getAllUsers();
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
        for (User user : usersDB) {
            users.put(user.getUsername(),user.getPasswordHashed());
            //System.out.println(user.getUsername()+"--->"+user.getPasswordHashed());
        }

        System.out.println("зарегестрированные пользователи\n"+users.keySet());
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
        try {
            MessageDigest digester = MessageDigest.getInstance("SHA-512");
            byte[] input = password.getBytes();
            byte[] digest = digester.digest(input);
            String hashAsString = Base64.getEncoder().encodeToString(digest);
            //System.out.println(input+"-->digest-->:"+digest);
            DatabaseUserManager.getInstance().addUserToDatabase(username, new String(hashAsString));
            users.put(username, new String(hashAsString));
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
            String hashAsString = Base64.getEncoder().encodeToString(digest);
            if(hashAsString==null) return false;
            String pswd = users.get(username);
            if(pswd==null) return false;
            return pswd.equals(new String(hashAsString));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
