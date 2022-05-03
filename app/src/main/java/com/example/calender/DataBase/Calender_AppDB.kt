package com.example.calender.DataBase

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Calender_DB::class], version = 1)
abstract class Calender_AppDB : RoomDatabase() {
    abstract fun calenderDao(): Calender_DAO
}