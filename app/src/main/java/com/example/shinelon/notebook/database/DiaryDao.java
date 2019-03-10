package com.example.shinelon.notebook.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.shinelon.notebook.entity.Diary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shinelon on 2019/3/9.
 */

public class DiaryDao {
    private SQLiteDatabase db;

    public DiaryDao(SQLiteDatabase sqLiteDatabase){
        this.db = sqLiteDatabase;
    }

    public boolean insert(Diary diary){
        ContentValues contentValues = new ContentValues();
        contentValues.put("author",diary.getAuthor());
        contentValues.put("title",diary.getTitle());
        contentValues.put("date",diary.getDate());
        contentValues.put("address",diary.getAddress());
        contentValues.put("uri",diary.getUri());
        contentValues.put("content",diary.getContent());
        long insertResult = db.insert("diary",null,contentValues);
        if (insertResult == -1){
            return false;
        }
        return true;
    }

    public boolean delete(Diary diary){
        if (diary == null){
            db.delete("diary","",null);
            return true;
        }
        int deleteResult = db.delete("diary","diary_id=?",
                new String[]{diary.getDiaryID() + ""});
        if (deleteResult == 0){
            return false;
        }
        return true;
    }

    public boolean update(Diary diary){
        ContentValues contentValues = new ContentValues();
        contentValues.put("author",diary.getAuthor());
        contentValues.put("title",diary.getTitle());
        contentValues.put("date",diary.getDate());
        contentValues.put("address",diary.getAddress());
        contentValues.put("uri",diary.getUri());
        contentValues.put("content",diary.getContent());
        int updteResult = db.update("diary",contentValues,"diary_id=?",
                new String[]{diary.getDiaryID() + ""});
        if (updteResult == 0){
            return false;
        }
        return true;
    }

    public List<Diary>queryAll(){
        List<Diary>diaryList = new ArrayList<>();
        Cursor cursor = db.query("diary",null,null,
                null,null,null,null);
        while (cursor.moveToNext()){
            Diary diary = new Diary();
            diary.setDiaryID(cursor.getInt(0));
            diary.setTitle(cursor.getString(1));
            diary.setAuthor(cursor.getString(2));
            diary.setDate(cursor.getString(3));
            diary.setAddress(cursor.getString(4));
            diary.setUri(cursor.getString(5));
            diary.setContent(cursor.getString(6));
            diaryList.add(diary);
        }
        return diaryList;
    }

}
