package com.example.calender;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button ttsbtn, calbtn;

    Intent i;

    View.OnClickListener cl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ttsbtn = findViewById(R.id.tts_test);
        calbtn = findViewById(R.id.calendartestbtn);

        cl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.tts_test:
                        i = new Intent(getApplicationContext(), TTS_Test.class);
                        startActivity(i);
                        break;

                    case R.id.calendartestbtn:
                        i = new Intent(getApplicationContext(), Calender_Basic.class);
                        startActivity(i);
                        break;
                }
            }
        };

        ttsbtn.setOnClickListener(cl);

    }
}