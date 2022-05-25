package com.example.calender;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.calender.R;

public class Setting_notification extends AppCompatActivity {
    ImageButton back;
    View.OnClickListener cl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_notification);
        //알림 설정 레이아웃을 표시

        //back = (ImageButton) findViewById(R.id.back_notif);


//        cl = new View.OnClickListener() {  //뒤로가기 버튼 누르면 화면 종료되도록 설정
//            @Override
//            public void onClick(View view) {
//                switch (view.getId()) {
//                    case R.id.back_notif:
//                        finish();
//                        break;
//                }
//            }
//        };
//        back.setOnClickListener(cl);

    }
}