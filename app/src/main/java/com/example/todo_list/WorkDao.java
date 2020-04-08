package com.example.todo_list;


import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WorkDao {

    @Insert
    void insert(Work work);  //insert single data

    @Update
    void update(Work work);  //update single data

    @Delete
    void delete(Work work);  //delete single data

    @Query("DELETE FROM work_table")
    void Deleteallwork();   //delete all work

    @Query("SELECT * FROM work_table ORDER BY rating DESC")
    LiveData<List<Work>> getallworks();    //showing data




}
