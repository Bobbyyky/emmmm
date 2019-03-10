package com.example.shinelon.notebook.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Shinelon on 2019/3/9.
 */

public class OpenHelper extends SQLiteOpenHelper{

    private static final String name = "diary.db";
    private static final int version= 1;

    public OpenHelper(Context context){
        super(context,name,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS diary(diary_id INTEGER primary key autoincrement," +
                "title varchar(64),author varchar(64),date" +
                " varchar(64)," +
                "address varchar(64),uri varchar(256),content varchar(1024))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion){

        }
    }
}
