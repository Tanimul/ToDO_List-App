package com.example.todo_list.Room;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.todo_list.Interface.WorkDao;
import com.example.todo_list.Model.Work;

import java.util.List;

public class WorkRepository {

    private WorkDao workDao;
    private LiveData<List<Work>> allworkes;

    public WorkRepository(Application application) {
        WorkDatabase database = WorkDatabase.getInstance(application);
        workDao = database.workDao();
        allworkes = workDao.getallworks();

    }

    public void insert(Work work) {
        new InsertWorkAsyntask(workDao).execute(work);
    }


    public void update(Work work) {
        new UpdateWorkAsyntask(workDao).execute(work);
    }

    public void delete(Work work) {
        new DeleteWorkAsyntask(workDao).execute(work);
    }

    public void deleteallwork() {
        new DeleteAllWorkAsyntask(workDao).execute();
    }

    public LiveData<List<Work>> getAllworkes() {
        return allworkes;
    }

    private static class InsertWorkAsyntask extends AsyncTask<Work, Void, Void> {
        private WorkDao workDao;

        public InsertWorkAsyntask(WorkDao workDao) {
            this.workDao = workDao;
        }

        @Override
        protected Void doInBackground(Work... works) {
            workDao.insert(works[0]);
            return null;
        }
    }

    private static class UpdateWorkAsyntask extends AsyncTask<Work, Void, Void> {
        private WorkDao workDao;

        public UpdateWorkAsyntask(WorkDao workDao) {
            this.workDao = workDao;
        }

        @Override
        protected Void doInBackground(Work... works) {
            workDao.update(works[0]);
            return null;
        }
    }

    private static class DeleteWorkAsyntask extends AsyncTask<Work, Void, Void> {
        private WorkDao workDao;

        public DeleteWorkAsyntask(WorkDao workDao) {
            this.workDao = workDao;
        }

        @Override
        protected Void doInBackground(Work... works) {
            workDao.delete(works[0]);
            return null;
        }
    }

    private static class DeleteAllWorkAsyntask extends AsyncTask<Void, Void, Void> {
        private WorkDao workDao;

        public DeleteAllWorkAsyntask(WorkDao workDao) {
            this.workDao = workDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            workDao.Deleteallwork();
            return null;
        }
    }

}
