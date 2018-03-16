package com.techcamp.aauj.rawabi.database.schema;

import com.techcamp.aauj.rawabi.database.RidesDB;

/**
 * Created by ALa on 2/14/2018.
 */

public class RideTable {
    public static final String TBL_NAME = "rides";
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TBL_NAME + "("
            + Cols.COL_ID + " INTEGER NOT NULL PRIMARY KEY,"
            + Cols.COL_JID + " integer ,"
            + Cols.COL_UID + " INTEGER ,"
            + Cols.COL_MeetingLocationX + " real ,"
            + Cols.COL_MeetingLocationY + " real, "
            + Cols.COL_OrderStatus + " INTEGER "
            + ")";
    public class Cols{
        public static final String COL_ID = "rid";
        public static final String COL_JID = "jid";
        public static final String COL_UID = "uid";
        public static final String COL_MeetingLocationX = "MeetingLocationX";
        public static final String COL_MeetingLocationY = "MeetingLocationY";
        public static final String COL_OrderStatus = "orderStatus";
    }
}
