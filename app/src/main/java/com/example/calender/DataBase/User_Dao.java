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

    @Query("update UserDB set UserID = null" +
            ", userPassword = null " +
            ", userEmail = null " +
            ", userName = null " +
            ", userPhonenumber = 00000 " +
            ", userAge = 0" +
            ", userAddress = null " +
            ", userAddressDetail = null " +
            ", userZIPCode = null where code = :codes")
    void deleteAll(int codes);


    // 데이터 수정을 위한 쿼리
    @Query("update UserDB set userID = :id " +
            ", userPassword = :pw " +
            ", userEmail = :email " +
            ", userName = :name " +
            ", userPhonenumber = :phone " +
            ", userAge = :age" +
            ", userAddress = :address " +
            ", userAddressDetail = :addressdetail " +
            ", userZIPCode = :zipcode where code = :codes")
    void update(int codes,
                String id, String pw,
                String email, String name,
                String phone, int age,
                String address, String addressdetail,
                String zipcode);
}
