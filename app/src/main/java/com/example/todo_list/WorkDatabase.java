package com.example.todo_list;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


@Database(entities = {Work.class}, version = 1,exportSchema = false)
public abstract class WorkDatabase extends RoomDatabase {

    private static WorkDatabase instance;

    public abstract WorkDao workDao();

    public static synchronized WorkDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    WorkDatabase.class, "work_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomcallback)
                    .build();
        }

        return instance;
    }

    private static RoomDatabase.Callback roomcallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsynTask(instance).execute();

        }
    };


    private static class PopulateDbAsynTask extends AsyncTask<Void, Void, Void> {
        private WorkDao workDao;

        private PopulateDbAsynTask(WorkDatabase database) {
            workDao = database.workDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            workDao.insert(new Work("study3", "1-1-20", "11.pm", 3));
            workDao.insert(new Work("study2", "3-1-20", "9.am", 4));
            workDao.insert(new Work("study1", "3-1-20", "10.pm", 2));

            return null;
        }
    }

}
