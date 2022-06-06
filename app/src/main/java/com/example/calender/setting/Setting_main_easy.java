package com.example.calender.setting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.calender.DataBase.Calender_DBSet;
import com.example.calender.DataBase.Calender_Dao;
import com.example.calender.DataBase.UserDB;
import com.example.calender.DataBase.User_Dao;
import com.example.calender.Main_Easy.Main_Easy;
import com.example.calender.Navigation;
import com.example.calender.R;
import com.example.calender.UserProfile;
import com.example.calender.login;

import java.util.List;

public class Setting_main_easy extends AppCompatActivity {
    ImageButton back;
    View.OnClickListener cl;
    Calender_Dao calender_dao;
    User_Dao user_dao;

    Button btntest1, btntest2, button3;      // 프래그먼트 전환을 위한 버튼
    ImageButton im1,im2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_main_easy);
        //알림 설정 레이아웃을 표시
        btntest1 = (Button) findViewById(R.id.account_settingbtn);
        btntest2 = (Button) findViewById(R.id.notificationsetting_btn);
        button3 = (Button) findViewById(R.id.darkmodesetting_btn);
        im1=(ImageButton) findViewById(R.id.normalbtn);
        im2=(ImageButton) findViewById(R.id.backbutton);

        Calender_DBSet dbController = Room.databaseBuilder(getApplicationContext(), Calender_DBSet.class, "CalenderDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        calender_dao = dbController.calender_dao();
        user_dao = dbController.user_dao();


        im1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Navigation.class);
                startActivity(intent);
            }
        });
        im2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Main_Easy.class);
                startActivity(intent);
            }
            public void onBackPressed() {
                //super.onBackPressed();
            }
        });
        btntest1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                List<UserDB> userdata = user_dao.getAllData();

                // 유저 DB를 불러온 다음에 데이터를 읽어와서 null 이면 로그인 페이지로 아니면 마이프로필로 이동ㅅ
                if(userdata.get(0).getId() == null){
                    Intent intent = new Intent(getApplicationContext(), login.class);
                    startActivity(intent);

                }else{
                    Intent userprofile = new Intent(getApplicationContext(), UserProfile.class);
                    startActivity(userprofile);
                    Log.v("login", "동작함");
                }
            }
        });

        btntest2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Setting_notification.class);
                startActivity(intent);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Setting_dark.class);
                startActivity(intent);
            }
        });
    }
}
