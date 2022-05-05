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

    // 데이터 삽입
    @Insert
    void insertAll(Calender_DB calender_db);

    @Update
    void updateData(Calender_DB calender_db);

    @Delete
    void delete(Calender_DB calender_db);

}
