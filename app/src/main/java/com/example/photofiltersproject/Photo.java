package com.example.photofiltersproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class Photo {
    private String username;
    private long premium;
    private String filter;
    private byte[] photo;
    private long open;

    public Photo()
    {

    }

    public Photo(String username, long premium, String filter, byte[] photo, long open) {
        this.username = username;
        this.premium = premium;
        this.filter = filter;
        this.photo = photo;
        this.open = open;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPremium(long premium) {
        this.premium = premium;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public void setOpen(long open) {
        this.open = open;
    }

    public String getUsername() {
        return username;
    }

    public long getPremium() {
        return premium;
    }

    public String getFilter() {
        return filter;
    }

    public byte[] getPhoto() {
        byte[] b = new byte[this.photo.length];
        int i = 0;
        while( i < b.length)
        {
            b[i] = photo[i];
            i++;
        }
        return b;
    }

    public boolean getOpen() {
        return open != 0;
    }

    public Bitmap getBitmapPhoto() {
        return BitmapFactory.decodeByteArray(this.photo, 0 ,this.photo.length);
    }
    public static byte[] getBitmapToByte(Bitmap bitmap)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        return bos.toByteArray();
    }
}
