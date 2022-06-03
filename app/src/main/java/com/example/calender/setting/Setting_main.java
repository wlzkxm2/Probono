package com.example.calender.setting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.calender.R;
import com.example.calender.login;


public class Setting_main extends Fragment {

    Setting_account settingaccount_frag; // 프래그먼트 호출을 위한 객체 생성
    Setting_notification settingnotification_frag;
    Setting_dark settingdark_frag;

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
                Intent intent = new Intent(getActivity(), login.class);
                startActivity(intent);
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