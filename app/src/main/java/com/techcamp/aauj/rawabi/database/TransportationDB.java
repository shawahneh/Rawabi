package com.techcamp.aauj.rawabi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.techcamp.aauj.rawabi.Beans.TransportationElement;
import com.techcamp.aauj.rawabi.database.schema.TransportationTable;

/**
 * Created by ALa on 3/8/2018.
 */

public class TransportationDB<B extends TransportationElement,T extends TransportationTable> extends DBHelper<TransportationElement> {

    private static TransportationDB instance;
    public static TransportationDB getInstance(Context context){
        if(instance == null){
            instance = new TransportationDB(context);
        }
        return instance;
    }

    private TransportationDB(Context context) {
        super(context,TransportationElement.class);
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
    protected void fillBeanFromCursor(Cursor rs, TransportationElement bean) {
        bean.setId(rs.getInt(rs.getColumnIndex(T.Cols.COL_ID)));
        bean.setTime(rs.getString(rs.getColumnIndex(T.Cols.COL_TIME)));
        bean.setType(rs.getInt(rs.getColumnIndex(T.Cols.COL_TYPE)));
    }



    private void fillValuesFromBean(B bean, ContentValues values) {
        values.put(T.Cols.COL_TIME, bean.getTime());
        values.put(T.Cols.COL_TYPE,bean.getType());
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


