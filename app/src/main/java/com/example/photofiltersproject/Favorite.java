package com.example.photofiltersproject;

public class Favorite {
    private long _pid;
    private String username;
    private Photo photo;

    public Favorite(long _pid, String username) {
        this._pid = _pid;
        this.username = username;
    }

    public Favorite(long _pid, String username, Photo photo) {
        this._pid = _pid;
        this.username = username;
        this.photo = photo;
    }

    public long get_pid() {
        return _pid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }
}
