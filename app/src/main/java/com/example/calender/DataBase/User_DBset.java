package com.example.calender.DataBase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {UserDB.class}, version = 7)
public abstract class User_DBset extends RoomDatabase {
    public abstract User_Dao user_dao();
}
