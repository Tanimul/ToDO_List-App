package com.example.todo_list;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "work_table")  //Table name
public class Work {

    @PrimaryKey(autoGenerate = true)
    int id;
    String event_name;
    String date;
    String due_time;
    float rating;

    public Work(String event_name, String date, String due_time, float rating) {
        this.event_name = event_name;
        this.date = date;
        this.due_time = due_time;
        this.rating = rating;
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
