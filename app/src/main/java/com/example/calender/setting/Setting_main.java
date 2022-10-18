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

import com.example.calender.DataBase.Calender_DBSet;
import com.example.calender.DataBase.Calender_Dao;
import com.example.calender.DataBase.UserDB;
import com.example.calender.DataBase.User_DBset;
import com.example.calender.DataBase.User_Dao;
import com.example.calender.R;
import com.example.calender.UserProfile;
import com.example.calender.login;

import java.util.List;


public class Setting_main extends Fragment {

    Calender_Dao calender_dao;
    User_Dao user_dao;

    Button userAccountSet_btn, noficationSet_btn, appThemeSet_btn,gme_btn;      // 프래그먼트 전환을 위한 버튼
    ImageButton defaultTheme, easyTheme;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_main, container, false);
        Log.v("Frag", "설정화면 실행");


        userAccountSet_btn = view.findViewById(R.id.account_settingbtn);
        noficationSet_btn = view.findViewById(R.id.notificationsetting_btn);
        appThemeSet_btn = view.findViewById(R.id.darkmodesetting_btn);
        defaultTheme =view.findViewById(R.id.normalbtn);
        easyTheme =view.findViewById(R.id.easybtn);
        gme_btn=view.findViewById(R.id.game_btn);

        Calender_DBSet dbController = Room.databaseBuilder(getActivity().getApplicationContext(), Calender_DBSet.class, "CalenderDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        User_DBset userdbController = Room.databaseBuilder(getActivity().getApplicationContext(), User_DBset.class, "UserInfoDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        calender_dao = dbController.calender_dao();
        user_dao = userdbController.user_dao();


        easyTheme.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Setting_main_easy.class);
                startActivity(intent);
            }
        });
        
        // 유저 어카운트를 눌렀을때 생기는 이벤트
        userAccountSet_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                List<UserDB> userdata = user_dao.getAllData();

                // 유저 DB를 불러온 다음에 데이터를 읽어와서 null 이면 로그인 페이지로 아니면 마이프로필로 이동
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

        noficationSet_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Setting_notification.class);
                startActivity(intent);
            }
        });

        appThemeSet_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Setting_dark.class);
                startActivity(intent);
            }
        });
        gme_btn.setOnClickListener(new View.OnClickListener() {
        //게임 버튼을 눌렀을떄 작동 되는 부분
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Setting_dark.class);
                startActivity(intent);
            }
        });
        return view;
    }


}