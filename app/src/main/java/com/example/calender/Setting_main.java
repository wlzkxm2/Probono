package com.example.calender;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

public class Setting_main extends AppCompatActivity {
    Button btn1,btn2,btn3,btn4;
    RadioButton rbtn1,rbtn2;
    View.OnClickListener cl;
    AlertDialog.Builder dia;
    DialogInterface.OnClickListener ok,not;
    View infoview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_main);
//        버튼 파인드 해줌
        btn1=(Button) findViewById(R.id.account_settingbtn);
        btn2=(Button) findViewById(R.id.notificationsetting_btn);
        btn3=(Button) findViewById(R.id.darkmodesetting_btn);
        btn4=(Button) findViewById(R.id.logout_btn);
        rbtn1=(RadioButton) findViewById(R.id.normalbtn);
        rbtn2=(RadioButton) findViewById(R.id.easybtn);

        /*btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Setting_account.class);
                startActivity(intent);
            }
        });//버튼이 눌렸을때 해당 클래스로 가는 명령어이다
        btn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Setting_notification.class);
                startActivity(intent);
            }
        });//버튼이 눌렸을때 해당 클래스로 가는 명령어이다
        btn3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Setting_dark.class);
                startActivity(intent);
            }
        });//버튼이 눌렸을때 해당 클래스로 가는 명령어이다 */
    }
}
