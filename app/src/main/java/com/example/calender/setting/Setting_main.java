package com.example.calender.setting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.calender.DataBase.Calender_DBSet;
import com.example.calender.DataBase.Calender_Dao;
import com.example.calender.DataBase.UserDB;
import com.example.calender.DataBase.User_Dao;
import com.example.calender.R;
import com.example.calender.UserProfile;
import com.example.calender.login;

import java.util.List;


public class Setting_main extends Fragment {

    Calender_Dao calender_dao;
    User_Dao user_dao;

    Button btntest1, btntest2, button3;      // 프래그먼트 전환을 위한 버튼
    ImageButton im1,im2;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_main, container, false);
        Log.v("Frag", "설정화면 실행");


        btntest1 = view.findViewById(R.id.account_settingbtn);
        btntest2 = view.findViewById(R.id.notificationsetting_btn);
        button3 = view.findViewById(R.id.darkmodesetting_btn);
        im1=view.findViewById(R.id.normalbtn);
        im2=view.findViewById(R.id.easybtn);

        Calender_DBSet dbController = Room.databaseBuilder(getActivity().getApplicationContext(), Calender_DBSet.class, "CalenderDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        calender_dao = dbController.calender_dao();
        user_dao = dbController.user_dao();


        im2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Setting_main_easy.class);
                startActivity(intent);
            }
        });
        btntest1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                List<UserDB> userdata = user_dao.getAllData();

                // 유저 DB를 불러온 다음에 데이터를 읽어와서 null 이면 로그인 페이지로 아니면 마이프로필로 이동ㅅ
                if(userdata.get(0).getId() == null){
                    Intent intent = new Intent(getActivity(), login.class);
                    startActivity(intent);

                }else{
                    Intent userprofile = new Intent(getActivity(), UserProfile.class);
                    startActivity(userprofile);
                    Log.v("login", "동작함");
                }
            }
        });

        btntest2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Setting_notification.class);
                startActivity(intent);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Setting_dark.class);
                startActivity(intent);
            }
        });

        return view;
    }
}