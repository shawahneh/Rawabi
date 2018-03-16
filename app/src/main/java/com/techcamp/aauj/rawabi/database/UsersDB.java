package com.techcamp.aauj.rawabi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.maps.model.LatLng;
import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.Beans.Ride;
import com.techcamp.aauj.rawabi.Beans.User;
import com.techcamp.aauj.rawabi.database.schema.RideTable;
import com.techcamp.aauj.rawabi.database.schema.UserTable;

import java.util.Date;
import java.util.zip.CheckedOutputStream;

/**
 * Created by ALa on 12/24/2017.
 */

public class UsersDB<B extends User,T extends UserTable> extends DBHelper<User> {

    private static UsersDB instance;
    public static UsersDB getInstance(Context context){
        if(instance == null){
            instance = new UsersDB(context);
        }
        return instance;
    }

    private UsersDB(Context context) {
        super(context,User.class);
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
    protected void fillBeanFromCursor(Cursor rs, User bean) {
        bean.setId(rs.getInt(rs.getColumnIndex(T.Cols.COL_ID)));
        bean.setImageurl(rs.getString(rs.getColumnIndex(T.Cols.COL_IMAGEURL)));
        bean.setPassword(rs.getString(rs.getColumnIndex(T.Cols.COL_PASSWORD)));


        bean.setBirthdate(new Date(rs.getLong(rs.getColumnIndex(T.Cols.COL_BIRTHDATE))));

        bean.setGender(rs.getInt(rs.getColumnIndex(T.Cols.COL_GENDER)));
        bean.setPhone(rs.getString(rs.getColumnIndex(T.Cols.COL_PHONE)));
        bean.setUsername(rs.getString(rs.getColumnIndex(T.Cols.COL_USERNAME)));
        bean.setFullname(rs.getString(rs.getColumnIndex(T.Cols.COL_FULLNAME)));
        bean.setAddress(rs.getString(rs.getColumnIndex(T.Cols.COL_ADDRESS)));
        bean.setRating(rs.getInt(rs.getColumnIndex(T.Cols.COL_RATING)));



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
        values.put(T.Cols.COL_IMAGEURL, bean.getImageurl());
        values.put(T.Cols.COL_PASSWORD, bean.getPassword());
        values.put(T.Cols.COL_BIRTHDATE, bean.getBirthdate().getTime());
        values.put(T.Cols.COL_GENDER, bean.getGender());
        values.put(T.Cols.COL_PHONE, bean.getPhone());
        values.put(T.Cols.COL_USERNAME, bean.getUsername());
        values.put(T.Cols.COL_FULLNAME, bean.getFullname());
        values.put(T.Cols.COL_ADDRESS, bean.getAddress());
        values.put(T.Cols.COL_RATING, bean.getRating());


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
