package com.example.calender.setting;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calender.R;
import com.example.calender.ThemeUtil;

public class Setting_dark extends AppCompatActivity {
    public static final String TAG = "Setting_dark";
    Button mod_btn, mod_btn1,mod_btn2,darkback;
    String themeColor;
    View.OnClickListener cl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.darkmode);

        themeColor= ThemeUtil.modLoad(getApplicationContext());
        ThemeUtil.applyTheme(themeColor);

        mod_btn = findViewById(R.id.light_btn);
        mod_btn1 = findViewById(R.id.dark_btn);
        mod_btn2= findViewById(R.id.sys_btn);
        mod_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themeColor = ThemeUtil.LIGHT_MODE;
                ThemeUtil.applyTheme(themeColor);
                ThemeUtil.modSave(getApplicationContext(), themeColor);
                Toast.makeText(getApplicationContext(), "라이트모드로 적용됬습니다", Toast.LENGTH_SHORT).show();
            }
        });
        mod_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themeColor = ThemeUtil.DARK_MODE;
                ThemeUtil.applyTheme(themeColor);
                ThemeUtil.modSave(getApplicationContext(), themeColor);
                Toast.makeText(getApplicationContext(), "다크모드로 적용됬습니다", Toast.LENGTH_SHORT).show();
            }
        });
        mod_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themeColor = ThemeUtil.DEFAULT_MODE;
                ThemeUtil.applyTheme(themeColor);
                ThemeUtil.modSave(getApplicationContext(), themeColor);
                Toast.makeText(getApplicationContext(), "시스템 테마로 적용됬습니다", Toast.LENGTH_SHORT).show();
            }
        });
        darkback = (Button) findViewById(R.id.dark_back);


        cl = new View.OnClickListener() {  //뒤로가기 버튼 누르면 화면 종료되도록 설정
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.dark_back:
                        finish();
                        break;
                }
            }
        };
        darkback.setOnClickListener(cl);
    }
}