package com.vision2020.database.profile;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vision2020.data.response.ProfileData;

import java.io.Serializable;

@Entity
public class ProfileModal {

    @PrimaryKey(autoGenerate = true)
    private int p_id;

    @ColumnInfo(name = "id")
    private String workoutId;

    @ColumnInfo(name = "data")
    private String data;

    public ProfileModal() {
    }

    public ProfileModal(int p_id, String workoutId, String data) {
        this.p_id = p_id;
        this.workoutId = workoutId;
        this.data = data;
    }

    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }

    public String getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(String workoutId) {
        this.workoutId = workoutId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static class RoomConverter {
        @TypeConverter
        public static ProfileData restoreList(String listOfString) {

            return new Gson().fromJson(listOfString, new TypeToken<ProfileData>() {
            }.getType());
        }

        @TypeConverter
        public static String saveList(ProfileData listOfString) {
            return new Gson().toJson(listOfString);
        }

    }


}