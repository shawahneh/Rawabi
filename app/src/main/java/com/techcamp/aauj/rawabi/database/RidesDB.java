package com.techcamp.aauj.rawabi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.maps.model.LatLng;
import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.Beans.Ride;
import com.techcamp.aauj.rawabi.Beans.User;
import com.techcamp.aauj.rawabi.database.schema.JourneyTable;
import com.techcamp.aauj.rawabi.database.schema.RideTable;

import java.util.Date;

/**
 * Created by alaam on 12/24/2017.
 */

public class RidesDB<B extends Ride,T extends RideTable> extends DBHelper<Ride> {

    private static RidesDB instance;
    public static RidesDB getInstance(Context context){
        if(instance == null){
            instance = new RidesDB(context);
        }
        return instance;
    }

    private RidesDB(Context context) {
        super(context,Ride.class);
    }


    public int saveBean(B bean)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        fillValuesFromBean(bean, values);

        if(this.getBeanById(bean.getId()) != null)
        {
            db.update(T.TBL_NAME, values, T.Cols.COL_ID + " = ?",
                    new String[]{bean.getId()+""});
        }
        else
            db.insert(T.TBL_NAME, null, values);

        return 1;
    }

    public B getBeanById(int id)
    {
        SQLiteDatabase db = getReadableDatabase();
        B bean = null;
        Cursor rs = null;
        try {
            rs = db.query(T.TBL_NAME, null
                    , T.Cols.COL_ID + "=?", new String[]{id+""}, null, null, null);
            if (rs.moveToFirst()) {
                bean = (B)newBean();
                fillBeanFromCursor(rs, bean);
            }
        }finally {
            if (rs != null)
                rs.close();
        }
        return bean;
    }



    @Override
    protected String getTableName() {
        return T.TBL_NAME;
    }

    @Override
    protected void fillBeanFromCursor(Cursor rs, Ride bean) {
        bean.setId(rs.getInt(rs.getColumnIndex(T.Cols.COL_ID)));
        bean.setOrderStatus(rs.getInt(rs.getColumnIndex(T.Cols.COL_OrderStatus)));

        Journey journey = new Journey();
        journey.setId(rs.getInt(rs.getColumnIndex(T.Cols.COL_JID)));
        bean.setJourney(journey);


        long eX = rs.getLong(rs.getColumnIndex(T.Cols.COL_MeetingLocationX));
        long eY = rs.getLong(rs.getColumnIndex(T.Cols.COL_MeetingLocationY));
        LatLng meeting = new LatLng(eX,eY);
        bean.setMeetingLocation(meeting);

        User user = new User();
        user.setId(rs.getInt(rs.getColumnIndex(T.Cols.COL_UID)));
        bean.setUser(user);

    }

    public int updateBean(B bean)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        fillValuesFromBean(bean, values);
        db.update(T.TBL_NAME, values, T.Cols.COL_ID + " = ?",
                new String[]{bean.getId()+""});

        return 1;
    }


    private void fillValuesFromBean(B bean, ContentValues values) {
        values.put(T.Cols.COL_ID, bean.getId());
        values.put(T.Cols.COL_JID, bean.getJourney().getId());
        values.put(T.Cols.COL_MeetingLocationX,bean.getMeetingLocation().latitude);
        values.put(T.Cols.COL_MeetingLocationY,bean.getMeetingLocation().longitude);
        values.put(T.Cols.COL_OrderStatus,bean.getOrderStatus());
        values.put(T.Cols.COL_UID,bean.getUser().getId());
    }

    public boolean deleteBean(int key){
        B person = getBeanById(key);
        if(person != null){
            //delete
            String selection = T.Cols.COL_ID + " = ? ";
            String[] selectionArgs = new String[]{key + ""};
            SQLiteDatabase db = getWritableDatabase();
            db.delete(T.TBL_NAME, selection, selectionArgs);
        }
        return person != null;
    }



}


