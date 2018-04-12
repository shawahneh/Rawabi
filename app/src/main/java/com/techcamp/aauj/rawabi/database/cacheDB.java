package com.techcamp.aauj.rawabi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.techcamp.aauj.rawabi.database.schema.JobTable;
import com.techcamp.aauj.rawabi.database.schema.cacheTable;
import com.techcamp.aauj.rawabi.model.CacheItem;
import com.techcamp.aauj.rawabi.model.Job;

import java.util.Date;

/**
 * Created by maysaraodeh on 08/03/2018.
 */

public class cacheDB<B extends CacheItem,T extends cacheTable> extends DBHelper<CacheItem> {

    private static cacheDB instance;
    public static synchronized cacheDB getInstance(Context context){
        if(instance == null){
            instance = new cacheDB(context);
        }
        return instance;
    }

    private cacheDB(Context context) {
        super(context,CacheItem.class);
    }

    @Override
    public CacheItem getBeanById(int id) {
        return null;
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

    public B getBeanById(String id)
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
    protected void fillBeanFromCursor(Cursor rs, CacheItem bean) {
        bean.setId(rs.getString(rs.getColumnIndex(T.Cols.COL_ID)));
        bean.setValue(rs.getString(rs.getColumnIndex(T.Cols.COL_VALUE)));
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
        values.put(T.Cols.COL_VALUE , bean.getValue());
        values.put(T.Cols.COL_DATE , new Date().getTime());
    }

    public boolean deleteBean(String key){
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


