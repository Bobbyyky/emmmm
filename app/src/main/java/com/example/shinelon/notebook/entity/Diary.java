package com.example.shinelon.notebook.entity;

import java.io.Serializable;

/**
 * Created by Shinelon on 2019/3/9.
 */

public class Diary implements Serializable{
    private int diaryID;
    private String author;
    private String title;
    private String date;
    private String address;
    private String uri;
    private String content;

    public int getDiaryID(){
        return diaryID;
    }

    public void setDiaryID(int diaryID){
        this.diaryID = diaryID;
    }

    public String getAuthor(){
        return author;
    }

    public void setAuthor(String author){
        this.author = author;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String string){
        this.title = title;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getUri(){
        return uri;
    }

    public void setUri(String uri){
        this.uri = uri;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }

}
