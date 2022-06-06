package com.example.calender;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import com.example.calender.DataBase.Calender_DBSet;
import com.example.calender.DataBase.Calender_Dao;
import com.example.calender.DataBase.UserDB;
import com.example.calender.DataBase.User_Dao;

import java.util.List;

public class UserProfile extends AppCompatActivity {

    Calender_Dao calender_dao;
    User_Dao user_dao;

    TextView userid, useremail, username,
            userage, userphonenum, useraddress;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userprofile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Calender_DBSet dbController = Room.databaseBuilder(getApplicationContext(), Calender_DBSet.class, "CalenderDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        calender_dao = dbController.calender_dao();
        user_dao = dbController.user_dao();

        userid = (TextView) findViewById(R.id.UserID_text);
        useremail = (TextView) findViewById(R.id.contextUserEmail_text);
        username = (TextView) findViewById(R.id.UserName_text);
        userage = (TextView) findViewById(R.id.UserAge_text);
        userphonenum = (TextView) findViewById(R.id.UserPhonNumber_text);
        useraddress = (TextView) findViewById(R.id.Useraddress_text);

        List<UserDB> callUserInfo = user_dao.getAllData();

        userid.setText(callUserInfo.get(0).getId().toString());
        useremail.setText(callUserInfo.get(0).getEmail().toString());
        userage.setText(Integer.toString(callUserInfo.get(0).getAge()));
        useraddress.setText(callUserInfo.get(0).getAddress().toString() + "\n" +
                callUserInfo.get(0).getAddressDetail().toString() + "\n" +
                callUserInfo.get(0).getZipcode().toString());


    }
}
