package com.example.calender.DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface User_Dao {

    @Query("select * from userdb")
    List<UserDB> getAllData();

    @Query("select * from UserDB where userID in (:userIds)")
    List<UserDB> FindUserData(String userIds);

    @Insert
    void insertAll(UserDB userDB);

    @Update
    void updateuser(UserDB userDB);

    @Delete
    void delete(UserDB userDB);

    // 데이터 수정을 위한 쿼리
    @Query("update UserDB set userID = :id " +
            ", userPassword = :pw " +
            ", userAddress = :address " +
            ", userAddressDetail = :addressdetail " +
            ", userZIPCode = :zipcode " +
            ", userEmail = :email " +
            ", userAge = :age where code = :codes")
    void update(int codes,
                String id, String pw,
                String address, String addressdetail,
                String zipcode, String email,
                int age);
}
