package com.example.todo_list.Room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.todo_list.Interface.WorkDao;
import com.example.todo_list.Model.Work;


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

            workDao.insert(new Work("final project Work", "9/4/2020", "4:27 PM", 3,true));
            workDao.insert(new Work("think about Trigonous", "10/4/2020", "3:40 PM", 4,false));
            workDao.insert(new Work("think about Job", "9/1/2020", "10:00 PM", 2,true));
            workDao.insert(new Work("think about Life", "10/4/2020", "3:45 AM", 5,false));

            return null;
        }
    }

}
