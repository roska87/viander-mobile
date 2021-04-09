package com.bit.viandermobile.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.bit.viandermobile.entities.Session;

@Dao
public interface SessionDao {

    @Query("SELECT * FROM session LIMIT 1")
    LiveData<Session> loadSession();

    @Insert
    void insert(Session session);

    @Query("DELETE FROM session")
    void delete();

}