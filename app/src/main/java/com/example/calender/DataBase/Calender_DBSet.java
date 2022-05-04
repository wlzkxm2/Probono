package com.example.calender.DataBase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Calender_DB.class}, version = 4)
public abstract class Calender_DBSet extends RoomDatabase {
    public abstract Calender_Dao calender_dao();
}
