package com.example.calender.setting;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.calender.R;

public class Setting_notification extends AppCompatActivity {
    Button back;
    View.OnClickListener cl;
    Switch notisw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_notification);
        //알림 설정 레이아웃을 표시

        back = (Button) findViewById(R.id.back_noti);
        notisw=(Switch) findViewById(R.id.noti_switch) ;
        //(notisw).setChecked(true);
        (notisw).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //True이면 할 일
                }else{
                    //False이면 할 일
                }
            }
        });


        cl = new View.OnClickListener() {  //뒤로가기 버튼 누르면 화면 종료되도록 설정
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.back_noti:
                        finish();
                        break;
                }
            }
        };
        back.setOnClickListener(cl);

    }
}