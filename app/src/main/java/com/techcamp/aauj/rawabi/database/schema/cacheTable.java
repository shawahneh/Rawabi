package com.techcamp.aauj.rawabi.database.schema;

/**
 * Created by maysaraodeh on 08/03/2018.
 */

public class cacheTable {

    public static final String TBL_NAME = "cacheTable";
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TBL_NAME + "("
            + Cols.COL_ID + " varchar NOT NULL PRIMARY KEY,"
            + Cols.COL_VALUE + " TEXT  ,"
            + Cols.COL_DATE + " integer "
            + ")";
    public class Cols{
        public static final String COL_ID = "COL_ID";
        public static final String COL_VALUE = "COL_VALUE";
        public static final String COL_DATE = "COL_DATE";


    }
}
