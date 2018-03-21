package com.techcamp.aauj.rawabi.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.techcamp.aauj.rawabi.database.schema.AnnouncementTable;
import com.techcamp.aauj.rawabi.database.schema.EventTable;
import com.techcamp.aauj.rawabi.database.schema.JobTable;
import com.techcamp.aauj.rawabi.database.schema.JourneyTable;
import com.techcamp.aauj.rawabi.database.schema.MediaItemTable;
import com.techcamp.aauj.rawabi.database.schema.RideTable;
import com.techcamp.aauj.rawabi.database.schema.TransportationTable;
import com.techcamp.aauj.rawabi.database.schema.UserTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ALa on 12/24/2017.
 */

public abstract class DBHelper<T> extends SQLiteOpenHelper
{

    protected Class<T> entityClass;

    protected Context mContext;
    private int mNumberOfItems = 100;

    public static final int VERSION = 12;
    public static final String DATABASE_NAME = "rawabi.db";

    protected DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
        mContext = context;
    }

    protected DBHelper(Context context, Class<T> entityClass){
        this(context);
        this.entityClass = entityClass;
    }

    protected T newBean(){
        T bean = null;
        try {
            bean = (T)entityClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return bean;
    }

    public void setNumberOfItems(int numberOfItems){
        mNumberOfItems = numberOfItems;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(UserTable.CREATE_TABLE);
        db.execSQL(JourneyTable.CREATE_TABLE);
        db.execSQL(RideTable.CREATE_TABLE);
        db.execSQL(EventTable.CREATE_TABLE);
        db.execSQL(JobTable.CREATE_TABLE);
        db.execSQL(MediaItemTable.CREATE_TABLE);
        db.execSQL(TransportationTable.CREATE_TABLE);
        db.execSQL(AnnouncementTable.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("drop table if EXISTS " + UserTable.TBL_NAME);
        db.execSQL("drop table if EXISTS " + JourneyTable.TBL_NAME);
        db.execSQL("drop table if EXISTS " + RideTable.TBL_NAME);
        db.execSQL("drop table if EXISTS " + EventTable.TBL_NAME);
        db.execSQL("drop table if EXISTS " + JobTable.TBL_NAME);
        db.execSQL("drop table if EXISTS " + AnnouncementTable.TBL_NAME);
        db.execSQL("drop table if EXISTS " + MediaItemTable.TBL_NAME);
        db.execSQL("drop table if EXISTS " + TransportationTable.TBL_NAME);
        onCreate(db);
    }

    public int getNoOfBeans(){
        int count = 0;
        String sql = "SELECT COUNT(*) FROM " + getTableName();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, null);
            if(cursor.moveToFirst())
                count = cursor.getInt(0);

        }finally {
            if(cursor != null)
                cursor.close();
            db.close();
        }
        return count;
    }

    public List<T> getAll()
    {
        SQLiteDatabase db = getReadableDatabase();
        List<T> list = new ArrayList<>();
        String selection=null;
        String[] args = null;

        Cursor rs = null;
        try {
            rs = db.query(getTableName(), null
                    , selection, args , null, null, orderBy());

            if (rs.moveToFirst())
            {
                while (!rs.isAfterLast()) {
                    T bean = (T)newBean();
                    fillBeanFromCursor(rs, bean);
                    list.add(bean);
                    rs.moveToNext();
                }
            }
        }finally {
            if (rs != null)
                rs.close();
        }
        return list;
    }

    protected String orderBy(){
        return null;
    }

    public int deleteAll()
    {
        SQLiteDatabase db = getWritableDatabase();
        int result = db.delete(getTableName(), null, null);
        return result;
    }


    public abstract T getBeanById(int id);
    protected abstract String getTableName();
    protected abstract void fillBeanFromCursor(Cursor rs,T bean);
}