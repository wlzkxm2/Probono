package com.example.calender;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

public class login_register extends FragmentActivity {

    Button gaip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register);
//        알림 설정 레이아웃을 표시해준다

            gaip = (Button) findViewById(R.id.gaip);
        gaip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), login.class);
                Toast.makeText( login_register.this, "가입 완료되었습니다",Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

//         btn9 = (Button) findViewById(R.id.button9);
//        btn7.setOnClickListener(new View.OnClickListener() {
    }
}
