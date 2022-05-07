package com.example.calender;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Setting_dark extends AppCompatActivity{
    public static final String TAG="MainActivity";
    Button mod_btn,mod_btn1;
    String themeColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.darkmode);

        themeColor=ThemeUtil.modLoad(getApplicationContext());
        ThemeUtil.applyTheme(themeColor);
        //버튼 찾아주기
        mod_btn=findViewById(R.id.light_btn);
        mod_btn1=findViewById(R.id.dark_btn);
        //라이트모드 버튼을 눌렀을대
        mod_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themeColor =ThemeUtil.LIGHT_MODE;
                ThemeUtil.applyTheme(themeColor);
                ThemeUtil.modSave(getApplicationContext(),themeColor);
            }
        });
        //다크모드 버튼을 눌렀을때
        mod_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themeColor =ThemeUtil.DARK_MODE;
                ThemeUtil.applyTheme(themeColor);
                ThemeUtil.modSave(getApplicationContext(),themeColor);
            }
        });
    }
}