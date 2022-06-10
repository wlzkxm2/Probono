package com.example.calender.DataBase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserDB {
    @PrimaryKey(autoGenerate = true)
    public int code;

    @ColumnInfo(name = "userID")
    public String id;

    @ColumnInfo(name = "userPassword")
    public String pw;

    @ColumnInfo(name = "userEmail")
    public String email;

    @ColumnInfo(name = "userName")
    public String name;

    @ColumnInfo(name = "userAge")
    public int age;

    @ColumnInfo(name = "userPhonenumber")
    public String phonenumber;

    @ColumnInfo(name = "userAddress")
    public String address;

    @ColumnInfo(name = "userAddressDetail")
    public String addressDetail;

    @ColumnInfo(name = "userZIPCode")
    public String zipcode;


    public UserDB(String id, String pw,
                  String email, String name,
                  int age, String phonenumber,
                  String address, String addressDetail, String zipcode){
        this.id = id;
        this.pw = pw;
        this.email = email;
        this.name = name;
        this.age = age;
        this.phonenumber = phonenumber;
        this.address = address;
        this.addressDetail = addressDetail;
        this.zipcode = zipcode;

    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
