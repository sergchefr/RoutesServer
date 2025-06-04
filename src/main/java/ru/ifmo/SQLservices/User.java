package ru.ifmo.SQLservices;

public class User {
    private final String username;
    private final String passwordHashed;

    public User(String username, String passwordHashed) {
        this.username = username;
        this.passwordHashed = passwordHashed;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHashed() {
        return passwordHashed;
    }
}
