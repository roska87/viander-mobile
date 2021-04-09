package com.bit.viandermobile.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.bit.viandermobile.daos.SessionDao;
import com.bit.viandermobile.entities.Session;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Session.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract SessionDao sessionDao();

    private static volatile AppDatabase instance;

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    public static AppDatabase getInstance(final Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "viander")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}