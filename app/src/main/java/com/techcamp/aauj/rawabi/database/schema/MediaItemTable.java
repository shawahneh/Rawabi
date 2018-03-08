package com.techcamp.aauj.rawabi.database.schema;

/**
 * Created by maysaraodeh on 08/03/2018.
 */

public class MediaItemTable {

    public static final String TBL_NAME = "mediaItems";
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TBL_NAME + "("
            + EventTable.Cols.COL_ID + " INTEGER NOT NULL PRIMARY KEY,"
            + EventTable.Cols.COL_DESCRIPTION + " VARCHAR ,"
            + EventTable.Cols.COL_IMAGE_URL + "VARCHAR "

            + ")";
    public class Cols{
        public static final String COL_ID = "jid";
        public static final String COL_DESCRIPTION = "description";
        public static final String COL_IMAGE_URL = "imageUrl";

    }
}