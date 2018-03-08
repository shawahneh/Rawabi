package com.techcamp.aauj.rawabi.database.schema;

/**
 * Created by maysaraodeh on 08/03/2018.
 */

public class MediaItemTable {

    public static final String TBL_NAME = "mediaItems";
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TBL_NAME + "("
            + Cols.COL_ID + " INTEGER NOT NULL PRIMARY KEY,"
            + Cols.COL_DESCRIPTION + " VARCHAR ,"
            + Cols.COL_IMAGE_URL + " VARCHAR "

            + ")";
    public class Cols{
        public static final String COL_ID = "mid";
        public static final String COL_DESCRIPTION = "description";
        public static final String COL_IMAGE_URL = "imageUrl";

    }
}