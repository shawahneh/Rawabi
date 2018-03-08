package com.techcamp.aauj.rawabi.database.schema;

/**
 * Created by ALa on 3/8/2018.
 */

public class TransportationTable {

    public static final String TBL_NAME = "transportation";
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TBL_NAME + "("
            + Cols.COL_ID + " INTEGER NOT NULL autoincrement PRIMARY KEY,"
            + Cols.COL_TIME + " varchar ,"
            + Cols.COL_TYPE + " integer "
            + ")";
    public class Cols{
        public static final String COL_ID = "COL_ID";
        public static final String COL_TIME = "COL_TIME";
        public static final String COL_TYPE = "COL_TYPE";
    }
}
