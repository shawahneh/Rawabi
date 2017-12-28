package com.techcamp.aauj.rawabi.database;

import android.content.Context;

/**
 * Created by alaam on 12/24/2017.
 */

public class RidesDB extends DBHelper {
    public static final String TABLE_NAME = "rides";
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + Cols.COL_ID + " INTEGER NOT NULL PRIMARY KEY,"
            + Cols.COL_JID + " integer ,"
            + Cols.COL_UID + " INTEGER ,"
            + Cols.COL_MeetingLocationX + " real ,"
            + Cols.COL_MeetingLocationY + " real, "
            + Cols.COL_OrderStatus + " INTEGER "
            + ")";
    class Cols{
        public static final String COL_ID = "rid";
        public static final String COL_JID = "jid";
        public static final String COL_UID = "uid";
        public static final String COL_MeetingLocationX = "MeetingLocationX";
        public static final String COL_MeetingLocationY = "MeetingLocationY";
        public static final String COL_OrderStatus = "orderStatus";
    }
    public RidesDB(Context context) {
        super(context);
    }

}
