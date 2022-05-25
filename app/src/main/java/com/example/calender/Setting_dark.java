package com.example.calender;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calender.R;
import com.example.calender.ThemeUtil;

public class Setting_dark extends AppCompatActivity {
    public static final String TAG = "Setting_dark";
    Button mod_btn, mod_btn1;
    String themeColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.darkmode);

//        themeColor= ThemeUtil.modLoad(getApplicationContext());
//        ThemeUtil.applyTheme(themeColor);

        mod_btn = findViewById(R.id.light_btn);
        mod_btn1 = findViewById(R.id.dark_btn);
        mod_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themeColor = ThemeUtil.LIGHT_MODE;
                ThemeUtil.applyTheme(themeColor);
                ThemeUtil.modSave(getApplicationContext(), themeColor);
            }
        });
        mod_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themeColor = ThemeUtil.DARK_MODE;
                ThemeUtil.applyTheme(themeColor);
                ThemeUtil.modSave(getApplicationContext(), themeColor);
            }
        });
    }
}