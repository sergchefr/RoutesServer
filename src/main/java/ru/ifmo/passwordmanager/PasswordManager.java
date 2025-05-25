package ru.ifmo.passwordmanager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;

public class PasswordManager {
    private HashMap<String, byte[]> users;
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

    public void addUser(String username, String password){
        try {
            MessageDigest digester = MessageDigest.getInstance("SHA-512");
            byte[] input = password.getBytes();
            byte[] digest = digester.digest(input);
            users.put(username, digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkPassword(String username, String password){
        try {
            if(username==null|password==null) return false;
            MessageDigest digester = MessageDigest.getInstance("SHA-512");
            byte[] input = password.getBytes();
            byte[] digest = digester.digest(input);
            if(digest==null) return false;
            return Arrays.equals(users.get(username), digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
