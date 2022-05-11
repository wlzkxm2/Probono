package com.example.calender;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class Setting_main extends Fragment {

    Setting_account settingaccount_frag; // 프래그먼트 호출을 위한 객체 생성
    Setting_notification settingnotification_frag;
    Setting_dark settingdark_frag;

    Button btntest1, btntest2, button3;      // 프래그먼트 전환을 위한 버튼

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_main, container, false);
        Log.v("Frag", "설정화면 실행");

        settingaccount_frag = new Setting_account(); // 객체 할당
        settingnotification_frag = new Setting_notification();
        settingdark_frag = new Setting_dark();

        btntest1 = view.findViewById(R.id.account_settingbtn);
        btntest2 = view.findViewById(R.id.notificationsetting_btn);
        button3 = view.findViewById(R.id.darkmodesetting_btn);

        btntest1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_main, settingaccount_frag)
                        .commit();
                // maintestlayout 에 있는 fragment_main 이라는 프래그먼트뷰에
                // 이름으로 그 이름 프래그먼트에 해당 프래그먼트 호출;
            }
        });

        btntest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_main, settingnotification_frag)
                        .commit();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_main, settingdark_frag)
                        .commit();
            }
        });
        return view;
    }
}