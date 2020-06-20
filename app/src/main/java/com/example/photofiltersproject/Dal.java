package com.example.photofiltersproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Dal extends SQLiteAssetHelper {

    public Dal(Context context)
    {
        super(context, "photo_filters_project.db",null,1);
    }

    //users:

    //Checkers:

    //check if user[username] exists in users
    public boolean users_check_existence_username(String username)
    {
        SQLiteDatabase db = getWritableDatabase();
        String qr = "SELECT * FROM users WHERE username = '"+username+"'";
        Cursor c = db.rawQuery(qr,null);
        boolean ret =  c.moveToFirst();
        c.close();
        return ret;
    }
    //check if user[username,password] exists in users
    public boolean users_check_existence_username_password(String username, String password)
    {
        SQLiteDatabase db = getWritableDatabase();
        String qr = "SELECT * FROM users WHERE username = '"+username+"' AND password = '"+password+"'";
        Cursor c = db.rawQuery(qr,null);
        boolean ret = c.moveToFirst();
        c.close();
        return ret;
    }

    //check if user[username].code in users matches code
    public boolean users_check_username_code_matches_code(String username, String code)
    {
        boolean ret;
        SQLiteDatabase db = getWritableDatabase();
        String qr = "SELECT * FROM users WHERE username = '"+username+"' , code ='"+code+"'";
        Cursor c = db.rawQuery(qr,null);
        ret = c.moveToFirst();
        c.close();
        return ret;
    }

    //Adders:

    //add user to users
    public void users_add_user(String username, String password)
    {
        SQLiteDatabase db = getWritableDatabase();
        String sqlInsert = "INSERT INTO users (username ,password) VALUES(?,?)";
        SQLiteStatement statement = db.compileStatement(sqlInsert);
        statement.bindString(1,username);
        statement.bindString(2,password);
        statement.execute();
    }

    //Getters:

    //get user[username] from users
    public User users_get_user_by_username(String username)
    {
        User ret = new User();
        SQLiteDatabase db = getWritableDatabase();
        String qr = "SELECT * FROM users WHERE username = '"+username+"'";
        Cursor c = db.rawQuery(qr,null);
        if(c.moveToFirst())
        {
            ret = new User(c.getString(c.getColumnIndex("username")),c.getString(c.getColumnIndex("password")),c.getLong(c.getColumnIndex("open")),c.getString(c.getColumnIndex("code")));
        }
        c.close();
        return ret;
    }

    //get user[username].code from users
    public String users_get_code_by_username(String username)
    {
        String ret = "";
        SQLiteDatabase db = getWritableDatabase();
        String qr = "SELECT code FROM users WHERE username = '"+username+"'";
        Cursor c = db.rawQuery(qr,null);
        if(c.moveToFirst())
        {
            ret = c.getString(c.getColumnIndex("code"));
        }
        c.close();
        return ret;
    }

    //get user[username].open from users
    public boolean users_get_open_by_username(String username)
    {
        boolean ret = true;
        SQLiteDatabase db = getWritableDatabase();
        String qr = "SELECT open FROM users WHERE username = '"+username+"'";
        Cursor c = db.rawQuery(qr,null);
        if(c.moveToFirst())
        {
            ret = c.getLong(c.getColumnIndex("open"))!=0;
        }
        c.close();
        return ret;
    }

    //Update:

    //update password of user[username]
    public void users_update_password_for_username(String username, String password)
    {
        SQLiteDatabase db = getWritableDatabase();
        String qr = "UPDATE users SET password = '"+password+"' WHERE username = '"+username+"'";
        db.execSQL(qr);
    }
    //update open,code of user[username]
    public void users_update_open_code_for_username(long open, String code, String username)
    {
        SQLiteDatabase db = getWritableDatabase();
        String qr = "UPDATE users SET open = '"+open+"' , code = '"+code+"' WHERE username = '"+username+"'";
        db.execSQL(qr);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    //photos:

    //Getters:

    //get all photos
    public ArrayList<Photo> photos_get_photos()
    {
        ArrayList<Photo> ret = new ArrayList<>();
        Photo photo = new Photo();
        SQLiteDatabase db = getWritableDatabase();
        String qr = "SELECT * FROM photos";
        Cursor c = db.rawQuery(qr,null);
        if(c.moveToFirst())
        {
            do {
                photo.setUsername(c.getString(c.getColumnIndex("username")));
                photo.setPremium(c.getLong(c.getColumnIndex("premium")));
                photo.setFilter(c.getString(c.getColumnIndex("filter")));
                photo.setOpen(c.getLong(c.getColumnIndex("open")));
                photo.setPhoto(c.getBlob(c.getColumnIndex("photo")));
                photo.setName(String.valueOf((c.getLong(c.getColumnIndex("_pid")))));
                ret.add(photo);
            }while (c.moveToNext());
        }
        return ret;
    }
    //get photos[username]
    public ArrayList<Photo> photos_get_photos_by_username(String username)
    {
        ArrayList<Photo> ret = new ArrayList<>();
        Photo photo = new Photo();
        SQLiteDatabase db = getWritableDatabase();
        String qr = "SELECT * FROM photos WHERE username = '"+username+"'";
        Cursor c = db.rawQuery(qr,null);
        if(c.moveToFirst())
        {
            do {
                photo.setUsername(c.getString(c.getColumnIndex("username")));
                photo.setPremium(c.getLong(c.getColumnIndex("premium")));
                photo.setFilter(c.getString(c.getColumnIndex("filter")));
                photo.setOpen(c.getLong(c.getColumnIndex("open")));
                photo.setPhoto(c.getBlob(c.getColumnIndex("photo")));
                photo.setName(String.valueOf((c.getLong(c.getColumnIndex("_pid")))));
                ret.add(photo);
            }while (c.moveToNext());
        }
        return ret;
    }

    //Checkers:

    //check if photos[username] exists
    public boolean photos_check_existence_username(String username)
    {
        SQLiteDatabase db = getWritableDatabase();
        String qr = "SELECT * FROM photos Where username ='"+username+"'";
        Cursor c = db.rawQuery(qr,null);
        boolean ret = c.moveToFirst();
        c.close();
        return ret;
    }
    //check if photos exists
    public boolean photos_check_existence()
    {
        SQLiteDatabase db = getWritableDatabase();
        String qr = "SELECT * FROM photos";
        Cursor c = db.rawQuery(qr,null);
        boolean ret = c.moveToFirst();
        c.close();
        return ret;
    }

    //Adders:

    //add photo to photos
    public void photos_add_photo(String username, long premium, String filter, byte[] photo, long open)
    {
        SQLiteDatabase db = getWritableDatabase();
        String sqlInsert = "INSERT INTO photos (username ,premioum, filter, photo,open) VALUES(?,?,?,?,?)";
        SQLiteStatement statement = db.compileStatement(sqlInsert);
        statement.bindString(1,username);
        statement.bindLong(2,premium);
        statement.bindString(3,filter);
        statement.bindBlob(4,photo);
        statement.bindLong(5,open);
        statement.execute();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
/**
    //check if photos[filter] exists
    public boolean photos_check_existence_filter(String filter)
    {
        return true;
    }
    //check if photos[premium] exists
    public boolean photos_check_existence_premium(long premium)
    {
        return true;
    }
    //get photos[_pid]
    public Photo photos_get_photo_by_pid(long _pid)
    {
        Photo ret = new Photo();

        return ret;
    }
    //check if photos[_pid] exists
    public boolean photos_check_existence_pid(long _pid)
    {
        return true;
    }
    //get photos[filter]
    public ArrayList<Photo> photos_get_photo_by_filter(String filter)
    {
        ArrayList<Photo> ret = new ArrayList<>();

        return ret;
    }
    //get photos[premium]
    public ArrayList<Photo> photos_get_photo_by_premium(long premium)
    {
        ArrayList<Photo> ret = new ArrayList<>();

        return ret;
    }

    //favorite:

    //Checkers:

    //check if favorites[username] exists
    public boolean favorites_check_existence_username(String username)
    {
        return true;
    }
    //check if favorite[username,_pid] exists
    public boolean favorites_check_existence_username_pid(String username, long _pid)
    {
        return true;
    }

    //Delete:

    //delete favorites[username]
    public void favorites_delete_all_username(String username)
    {

    }
    //delete favorite[username,_pid]
    public void favorite_delete_username_pid(String username, long _pid)
    {

    }

    //Adders:

    //add favorite
    public void favorites_add_favorite(long _pid, String username)
    {
        Date date = new Date(); // your date
        // Choose time zone in which you want to interpret your Date
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Jerusalem"));
        cal.setTime(date);
        long year = cal.get(Calendar.YEAR);
        long month = cal.get(Calendar.MONTH);
        long day = cal.get(Calendar.DAY_OF_MONTH);
    }

    //Getters:

    //get favorites[username]
*/
}
