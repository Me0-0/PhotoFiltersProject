package com.example.photofiltersproject;

public class User {
    private String username;
    private String password;
    private long open;
    private String code;
    public User()
    {

    }

    public User(String username, String password, long open, String code)
    {
        this.username = username;
        this.password = password;
        this.open = open;
        this.code = code;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getOpen() {
        return open != 0;
    }

    public void setOpen(long open) {
        this.open = open;
    }

    public void setOpen(boolean open) {
        this.open = open?1:0;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
