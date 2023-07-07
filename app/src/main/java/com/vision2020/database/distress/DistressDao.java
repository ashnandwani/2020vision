package com.vision2020.database.distress;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.vision2020.data.response.DistressExpData;


@Dao
public interface DistressDao {

    @Query("SELECT COUNT(*)  FROM DistressModal where expDate=:Id")
    int checkData(String Id);
/*
    @Query("SELECT * FROM DistressModal")
    DistressExpData getDSData();*/

    @Insert
    void insert(DistressModal task);

    @Query("UPDATE DistressModal SET data=:data WHERE expDate = :id")
    void update(String data, String id);
}