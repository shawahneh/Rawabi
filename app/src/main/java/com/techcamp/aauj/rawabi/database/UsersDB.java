package com.techcamp.aauj.rawabi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.techcamp.aauj.rawabi.Beans.User;

import java.util.zip.CheckedOutputStream;

/**
 * Created by alaam on 12/24/2017.
 */

public class UsersDB extends DBHelper {
    public static final String TABLE_NAME = "rides";
    class Cols{
        public static final String COL_ID = "uid";
        public static final String COL_FULLNAME = "fullname";
        public static final String COL_GENDER = "gender";
        public static final String COL_BIRTHDATE = "birthdate";
        public static final String COL_USERNAME = "username";
        public static final String COL_PASSWORD = "password";
        public static final String COL_IMAGEURL = "imageurl";
        public static final String COL_PHONE = "phone";
        public static final String COL_ADDRESS = "address";
        public static final String COL_RATING = "rating";
    }
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
             + Cols.COL_ID + " INTEGER NOT NULL PRIMARY KEY,"
            + Cols.COL_FULLNAME + " VARCHAR ,"
            + Cols.COL_GENDER + " INTEGER ,"
            + Cols.COL_BIRTHDATE + " INTEGER ,"
            + Cols.COL_USERNAME + " VARCHAR, "
            + Cols.COL_PASSWORD + " VARCHAR, "
            + Cols.COL_IMAGEURL + " VARCHAR, "
            + Cols.COL_PHONE + " VARCHAR, "
            + Cols.COL_ADDRESS + " VARCHAR, "
            + Cols.COL_RATING + " INTEGER "
            + ")";

    public UsersDB(Context context) {
        super(context);
    }

    public boolean addUser(User user){
        SQLiteDatabase db = null;
        ContentValues contentValues = new ContentValues();
        contentValues.put(Cols.COL_ID,user.getId());
        contentValues.put(Cols.COL_FULLNAME,user.getFullname());
        contentValues.put(Cols.COL_GENDER,user.getGender());
        contentValues.put(Cols.COL_BIRTHDATE,user.getBirthdate().getTime());
        contentValues.put(Cols.COL_USERNAME,user.getUsername());
        contentValues.put(Cols.COL_PASSWORD,user.getPassword());
        contentValues.put(Cols.COL_IMAGEURL,user.getImageurl());
        contentValues.put(Cols.COL_PHONE,user.getPhone());
        contentValues.put(Cols.COL_ADDRESS,user.getAddress());
        contentValues.put(Cols.COL_RATING,user.getRating());

        try {
            db = getWritableDatabase();
            if(db.insert(TABLE_NAME,null,contentValues) > 0){
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (db != null) {
                db.close();
            }
        }
        return false;

    }
    public User getUser(int uid){
        SQLiteDatabase db = null;
        try {
            db = getReadableDatabase();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
