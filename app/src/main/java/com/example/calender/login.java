package com.example.calender;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class login extends AppCompatActivity {
    Button loginbtn,registerbtn;
    EditText idkan,passkan;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //알림 설정 레이아웃을 표시해준다
        loginbtn = (Button) findViewById(R.id.login_btn);
        registerbtn = (Button) findViewById(R.id.register_btn);
        idkan=(EditText)findViewById(R.id.emailkan);
        passkan=(EditText)findViewById(R.id.password_kan);
        loginbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });//버튼을 누르면 메인화면으로 가는 명령어
        registerbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), login_register.class);
                startActivity(intent);
            }
        });//버튼을 누르면 메인화면으로 가는 명령어
    }
}