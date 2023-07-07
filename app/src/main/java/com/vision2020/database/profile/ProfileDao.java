package com.vision2020.database.profile;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ProfileDao {

    @Query("SELECT COUNT(*)  FROM ProfileModal where id=:Id")
    int checkData(int Id);

    @Query("SELECT * FROM ProfileModal")
    ProfileModal getProfileData();

    @Insert
    void insert(ProfileModal task);


    @Query("UPDATE ProfileModal SET data=:data WHERE id = :id")
    void update(String data, int id);
}