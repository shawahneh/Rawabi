package com.techcamp.aauj.rawabi.database;

import android.content.Context;

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

}
