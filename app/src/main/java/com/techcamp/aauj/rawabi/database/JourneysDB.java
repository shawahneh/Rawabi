package com.techcamp.aauj.rawabi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.maps.model.LatLng;
import com.techcamp.aauj.rawabi.model.Journey;
import com.techcamp.aauj.rawabi.model.User;
import com.techcamp.aauj.rawabi.database.schema.JourneyTable;

import java.util.Date;

/**
 * Created by ALa on 12/24/2017.
 */

public class JourneysDB<B extends Journey,T extends JourneyTable> extends DBHelper<Journey> {

    private static JourneysDB instance;
    public static JourneysDB getInstance(Context context){
        if(instance == null){
            instance = new JourneysDB(context);
        }
        return instance;
    }

    private JourneysDB(Context context) {
        super(context,Journey.class);
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

    public B getJourneyById(int id ){
        B journey = getBeanById(id);
        int uid = journey.getUser().getId();
        User user =  UsersDB.getInstance(mContext).getBeanById(uid);
        journey.setUser(user);
        return journey;
    }

    public void saveJourney(B journey){
        User user = journey.getUser();
        UsersDB.getInstance(mContext).saveBean(user);

        saveBean(journey);
    }



    @Override
    protected String getTableName() {
        return T.TBL_NAME;
    }

    @Override
    protected void fillBeanFromCursor(Cursor rs, Journey bean) {
        bean.setId(rs.getInt(rs.getColumnIndex(T.Cols.COL_ID)));
        bean.setStatus(rs.getInt(rs.getColumnIndex(T.Cols.COL_Status)));
        bean.setGoingDate(new Date(rs.getLong(rs.getColumnIndex(T.Cols.COL_GoingDate))));
        bean.setCarDescription(rs.getString(rs.getColumnIndex(T.Cols.COL_CarDescription)));
        bean.setGenderPrefer(rs.getInt(rs.getColumnIndex(T.Cols.COL_GenderPrefer)));
        bean.setSeats(rs.getInt(rs.getColumnIndex(T.Cols.COL_Seats)));

        long eX = rs.getLong(rs.getColumnIndex(T.Cols.COL_EndPointX));
        long eY = rs.getLong(rs.getColumnIndex(T.Cols.COL_EndPointY));
        LatLng endPonit = new LatLng(eX,eY);
        bean.setEndPoint(endPonit);

        long sX = rs.getLong(rs.getColumnIndex(T.Cols.COL_StartPointX));
        long sY = rs.getLong(rs.getColumnIndex(T.Cols.COL_StartPointY));
        LatLng startPoint = new LatLng(sX,sY);
        bean.setStartPoint(startPoint);

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
        values.put(T.Cols.COL_CarDescription, bean.getCarDescription());
        values.put(T.Cols.COL_EndPointX,bean.getEndPoint().latitude);
        values.put(T.Cols.COL_EndPointY,bean.getEndPoint().longitude);
        values.put(T.Cols.COL_StartPointX,bean.getStartPoint().latitude);
        values.put(T.Cols.COL_StartPointY,bean.getStartPoint().longitude);
        values.put(T.Cols.COL_GenderPrefer,bean.getGenderPrefer());
        values.put(T.Cols.COL_Seats,bean.getSeats());
        values.put(T.Cols.COL_UID,bean.getUser().getId());
        values.put(T.Cols.COL_GoingDate,bean.getGoingDate().getTime());
        values.put(T.Cols.COL_Status,bean.getStatus());
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


