package com.example.calender.DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface Calender_Dao {
    @Query("select * from calender_db")
    List<Calender_DB> getAllData();

    @Query("select * from calender_db where UserIDCode in (:userIds)")
    List<Calender_DB> loadAllDataByIDs(int[] userIds);

    @Query("select * from calender_db where start_years <= (:Years) or end_years >= (:Years) " +
            "and start_months <= (:Months) or end_months >= (:Months) " +
            "and start_days = (:Days) or end_days = (:Days)")
    List<Calender_DB> loadAllDataByYears(int Years, int Months, int Days);

    // 데이터 삽입
    @Insert
    void insertAll(Calender_DB calender_db);

    @Update
    void updateData(Calender_DB calender_db);

    @Delete
    void delete(Calender_DB calender_db);

}
