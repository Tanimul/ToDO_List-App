package com.example.todo_list;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "work_table")

public class Work {

    @PrimaryKey(autoGenerate = true)
    int id;
    String event_name;
    String date;
    String due_time;
    boolean com;
    float rating;


    public Work(String event_name, String date, String due_time, float rating,boolean com) {
        this.event_name = event_name;
        this.date = date;
        this.due_time = due_time;
        this.rating = rating;
        this.com=com;
    }


    public boolean isCom() {
        return com;
    }

    public void setCom(boolean com) {
        this.com = com;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public String getDate() {
        return date;
    }

    public String getDue_time() {
        return due_time;
    }

    public float getRating() {
        return rating;
    }


}
