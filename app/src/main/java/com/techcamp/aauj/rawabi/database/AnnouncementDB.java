package com.techcamp.aauj.rawabi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.techcamp.aauj.rawabi.Beans.Announcement;
import com.techcamp.aauj.rawabi.Beans.Job;
import com.techcamp.aauj.rawabi.database.schema.AnnouncementTable;
import com.techcamp.aauj.rawabi.database.schema.JobTable;

import java.util.Date;

/**
 * Created by maysaraodeh on 08/03/2018.
 */

public class AnnouncementDB <B extends Announcement,T extends AnnouncementTable> extends DBHelper<Announcement> {

    private static AnnouncementDB instance;
    public static AnnouncementDB getInstance(Context context){
        if(instance == null){
            instance = new AnnouncementDB(context);
        }
        return instance;
    }

    private AnnouncementDB(Context context) {
        super(context,Announcement.class);
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
    protected void fillBeanFromCursor(Cursor rs, Announcement bean) {
        bean.setId(rs.getInt(rs.getColumnIndex(T.Cols.COL_ID)));
        bean.setName(rs.getString(rs.getColumnIndex(T.Cols.COL_NAME)));
        bean.setDescription(rs.getString(rs.getColumnIndex(T.Cols.COL_DESCRIPTION)));
        bean.setImageUrl(rs.getString(rs.getColumnIndex(T.Cols.COL_IMAGE_URL)));
        bean.setDate(new Date(rs.getLong(rs.getColumnIndex(T.Cols.COL_DATE))));

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
        values.put(T.Cols.COL_NAME , bean.getName());
        values.put(T.Cols.COL_DESCRIPTION , bean.getDescription());
        values.put(T.Cols.COL_IMAGE_URL , bean.getImageUrl());
        values.put(T.Cols.COL_DATE , bean.getDate().getTime());
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



