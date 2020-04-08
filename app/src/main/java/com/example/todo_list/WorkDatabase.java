package com.example.todo_list;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


@Database(entities = {Work.class}, version = 2,exportSchema = false)
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

            workDao.insert(new Work("final project Work", "1-1-20", "11.00 pm", 3,true));
            workDao.insert(new Work("think about Trigonous", "3-1-20", "9.30 am", 4,false));
            workDao.insert(new Work("think about Job", "3-1-20", "10.00 pm", 2,true));
            workDao.insert(new Work("think about Life", "4-1-20", "12.00 pm", 5,false));

            return null;
        }
    }

}
