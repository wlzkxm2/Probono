package com.example.calender.DataBase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Calender_DB (
        @PrimaryKey val uid : Int,
        @ColumnInfo(name = "month") val months: Int?,
        @ColumnInfo(name = "day") val  days: Int?,
        @ColumnInfo(name = "ScaData") val data: String?
        )