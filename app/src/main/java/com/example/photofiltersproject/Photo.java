package com.example.photofiltersproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class Photo {
    private String username;
    private long premium;
    private String filter;
    private byte[] photo;

    public Photo()
    {

    }

    public Photo(String username, long premium, String filter, byte[] photo) {
        this.username = username;
        this.premium = premium;
        this.filter = filter;
        this.photo = photo;
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
