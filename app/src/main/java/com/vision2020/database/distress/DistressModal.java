package com.vision2020.database.distress;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vision2020.data.response.DistressExpData;
import com.vision2020.data.response.ProfileData;

import java.io.Serializable;

@Entity
public class DistressModal {

    @PrimaryKey(autoGenerate = true)
    private int p_id;

    @ColumnInfo(name = "expDate")
    private String date;

    @ColumnInfo(name = "data")
    private String data;

    public DistressModal() {
    }

    public DistressModal(int p_id, String date, String data) {
        this.p_id = p_id;
        this.date = date;
        this.data = data;
    }

    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static class RoomConverter {
        @TypeConverter
        public static DistressExpData restoreList(String listOfString) {

            return new Gson().fromJson(listOfString, new TypeToken<DistressExpData>() {
            }.getType());
        }

        @TypeConverter
        public static String saveList(DistressExpData listOfString) {
            return new Gson().toJson(listOfString);
        }

    }


}