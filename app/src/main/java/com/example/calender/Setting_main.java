package com.example.calender;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class Setting_main extends FragmentActivity{

    Setting_account settingaccount_frag; // 프래그먼트 호출을 위한 객체 생성
    Setting_notification settingnotification_frag ;
    Setting_dark settingdark_frag;

    Button btntest1, btntest2,button3;      // 프래그먼트 전환을 위한 버튼

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_main);        // 레이아웃 재세팅후 이부분 수정바랍니다
        Log.v("Frag", "설정화면 실행");

        settingaccount_frag = new Setting_account(); // 객체 할당
        settingnotification_frag = new Setting_notification();
        settingdark_frag= new Setting_dark();

        btntest1 = (Button) findViewById(R.id.account_settingbtn);
        btntest2 = (Button) findViewById(R.id.notificationsetting_btn);
        button3  = (Button) findViewById(R.id.darkmodesetting_btn);

        btntest1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_main, settingaccount_frag)
                        .commit();
                // maintestlayout 에 있는 fragment_main 이라는 프래그먼트뷰에
                // 이름으로 그 이름 프래그먼트에 해당 프래그먼트 호출;
            }
        });

        btntest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_main, settingnotification_frag)
                        .commit();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_main,settingdark_frag)
                        .commit();
            }
        });
    }
}
