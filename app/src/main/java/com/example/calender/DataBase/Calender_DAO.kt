package com.example.calender.DataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface Calender_DAO {
    @Query("SELECT * FROM calender_db")
    fun getAll(): List<Calender_DB>

    @Query("SELECT * FROM calender_db WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<Calender_DB>

    @Query("SELECT * FROM calender_db WHERE day LIKE :days")
    fun findBySca(days: Int): Calender_DB

    @Insert
    fun insertAll(vararg ScaData: Calender_DB)

    @Delete
    fun delete(calenderDb: Calender_DB)
}