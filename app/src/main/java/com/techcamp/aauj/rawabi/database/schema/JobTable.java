package com.techcamp.aauj.rawabi.database.schema;

/**
 * Created by maysaraodeh on 08/03/2018.
 */

public class JobTable {

    public static final String TBL_NAME = "jobs";
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TBL_NAME + "("
            + Cols.COL_ID + " INTEGER NOT NULL PRIMARY KEY,"
            + Cols.COL_NAME + " VARCHAR ,"
            + Cols.COL_DESCRIPTION + " VARCHAR ,"
            + Cols.COL_IMAGE_URL + "VARCHAR ,"
            + Cols.COL_DATE + "DATETIME "
            + ")";
    public class Cols{
        public static final String COL_ID = "jid";
        public static final String COL_NAME = "name";
        public static final String COL_DESCRIPTION = "description";
        public static final String COL_IMAGE_URL = "imageUrl";
        public static final String COL_DATE = "COL_DATE";


    }
}
