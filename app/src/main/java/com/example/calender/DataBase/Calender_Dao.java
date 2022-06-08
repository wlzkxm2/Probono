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


    // 선택한 날이 데이터 베이스 내부의 데이터의 사이에 있으면 참으로 반환
    @Query("select * from calender_db where start_years <= (:Years) or end_years >= (:Years) " +
            "and start_months <= (:Months) or end_months >= (:Months) " +
            "and start_days = (:Days) or end_days = (:Days)")
    List<Calender_DB> loadAllDataByYears(int Years, int Months, int Days);

    @Query("select * from calender_db where num in (:nums)")
    List<Calender_DB> loadMainData(int nums);

    // 첫번째 쿼리 즉 1번쨰 처음 실행여부를 확인하는 DB에 메인엑트 타이틀을 수정
    @Query("update Calender_DB set MainActTitle = :title " +       // 메인 엑트 타이틀 변경
            ", MainActDay = :day " +
            ", MainActDTitle = :Dtitle where num = :num")
    void MainActTitleAllupdate(int num, String title, long day, String Dtitle);

    @Query("update Calender_DB set MainActTitle = :title " +       // 메인 엑트 타이틀 변경
            "where num = :num")
    void MainActTitleupdate(int num, String title);

    @Query("update Calender_DB set MainActDay = :day " +       // 메인 엑트 시간 변경
            "where num = :num")
    void MainActDayupdate(int num, long day);

    @Query("update Calender_DB set MainActDTitle = :dtitle " +       // 메인 엑트 시간 변경
            "where num = :num")
    void MainActDtitleupdate(int num, String dtitle);

    // 데이터 삽입
    @Insert
    void insertAll(Calender_DB calender_db);

    @Update
    void updateData(Calender_DB calender_db);

    @Delete
    void delete(Calender_DB calender_db);

}
