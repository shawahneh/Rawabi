package com.techcamp.aauj.rawabi.database.schema;

import com.techcamp.aauj.rawabi.database.JourneysDB;

/**
 * Created by alaam on 2/14/2018.
 */

public class JourneyTable {

    public static final String TBL_NAME = "journeys";
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TBL_NAME + "("
            + Cols.COL_ID + " INTEGER NOT NULL PRIMARY KEY,"
            + Cols.COL_UID + " integer ,"
            + Cols.COL_StartPointX + " real ,"
            + Cols.COL_StartPointY + " real ,"
            + Cols.COL_EndPointX + " real ,"
            + Cols.COL_EndPointY + " real ,"
            + Cols.COL_Seats + " integer ,"

            + Cols.COL_GoingDate + " integer ,"
            + Cols.COL_GenderPrefer + " integer ,"
            + Cols.COL_CarDescription + " varchar ,"
            + Cols.COL_Status + " integer"

            + ")";
    public class Cols{
        public static final String COL_ID = "jid";
        public static final String COL_UID = "uid";
        public static final String COL_StartPointX = "startPointX";
        public static final String COL_StartPointY = "startPointY";
        public static final String COL_EndPointX = "endPointX";
        public static final String COL_EndPointY = "endPointY";
        public static final String COL_Seats = "seats";
        public static final String COL_GoingDate = "goingDate";
        public static final String COL_GenderPrefer = "genderPrefer";
        public static final String COL_CarDescription = "carDescription";
        public static final String COL_Status = "status";
    }
}
