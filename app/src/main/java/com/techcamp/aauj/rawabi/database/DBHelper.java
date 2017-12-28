package com.techcamp.aauj.rawabi.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.techcamp.aauj.rawabi.Beans.Journey;

/**
 * Created by alaam on 12/24/2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "database1";
    public static final int DB_VERSION = 1;
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UsersDB.CREATE_TABLE);
        db.execSQL(JourneysDB.CREATE_TABLE);
        db.execSQL(RidesDB.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + RidesDB.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + JourneysDB.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + UsersDB.TABLE_NAME);
        onCreate(db);
    }
}
