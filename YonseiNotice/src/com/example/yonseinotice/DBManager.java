package com.example.yonseinotice;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper {
	 
    public DBManager(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        // ���ο� ���̺��� �����Ѵ�.
        // create table ���̺�� (�÷��� Ÿ�� �ɼ�);
        db.execSQL("CREATE TABLE LIKE_LIST( _id INTEGER PRIMARY KEY AUTOINCREMENT, URL TEXT, NOTICE TEXT);");
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
 
    public void insert(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();     
    }
     
    public void update(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();     
    }
     
    public void delete(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();     
    }
     
    public String PrintData() {
        SQLiteDatabase db = getReadableDatabase();
        String str = "";
         
        Cursor cursor = db.rawQuery("select * from LIKE_LIST", null);
        while(cursor.moveToNext()) {
            str += cursor.getInt(0)
                + " : foodName "
                + cursor.getString(1)
                + ", price = "
                + cursor.getInt(2)
                + "\n";
        }
         
        return str;
    }
}