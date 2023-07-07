package com.vision2020.database;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.vision2020.database.distress.DistressDao;
import com.vision2020.database.distress.DistressModal;
import com.vision2020.database.profile.ProfileDao;
import com.vision2020.database.profile.ProfileModal;

@Database(entities = {ProfileModal.class, DistressModal.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProfileDao orderDao();
    public abstract DistressDao distressDao();
}