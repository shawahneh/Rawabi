package com.techcamp.aauj.rawabi.database.schema;

/**
 * Created by maysaraodeh on 08/03/2018.
 */

public class AnnouncementTable {

    public static final String TBL_NAME = "announcement";
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TBL_NAME + "("
            + EventTable.Cols.COL_ID + " INTEGER NOT NULL PRIMARY KEY,"
            + EventTable.Cols.COL_NAME + " VARCHAR ,"
            + EventTable.Cols.COL_DESCRIPTION + " VARCHAR ,"
            + EventTable.Cols.COL_IMAGE_URL + "VARCHAR ,"
            + EventTable.Cols.COL_DATE + "DATETIME ,"
            + ")";
    public class Cols{
        public static final String COL_ID = "aid";
        public static final String COL_NAME = "name";
        public static final String COL_DESCRIPTION = "description";
        public static final String COL_IMAGE_URL = "imageUrl";
        public static final String COL_DATE = "date";


    }
}
