package com.bit.viandermobile.repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.bit.viandermobile.daos.SessionDao;
import com.bit.viandermobile.database.AppDatabase;
import com.bit.viandermobile.entities.Session;

public class LocalRepository {

    private SessionDao sessionDao;
    private LiveData<Session> session;

    public LocalRepository(Application application){
        AppDatabase db = AppDatabase.getInstance(application);
        sessionDao = db.sessionDao();
        session = sessionDao.loadSession();
    }

    public LiveData<Session> getSession(){
        return session;
    }

    public void insert(Session session){
        AppDatabase.databaseWriteExecutor.execute(() -> sessionDao.insert(session));
    }

    public void delete(){
        AppDatabase.databaseWriteExecutor.execute(() -> sessionDao.delete());
    }

}