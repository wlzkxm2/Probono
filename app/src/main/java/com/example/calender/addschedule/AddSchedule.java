package com.example.calender.addschedule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;


import androidx.annotation.Nullable;

import com.example.calender.R;

public class AddSchedule extends Activity {

    Button btn_save, btn_back;
    EditText et_title,et_memo;
    CheckBox repeatCheck;
    Intent intent;
    View.OnClickListener cl;
    boolean scheduleLoof = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_schedule);

        btn_back = findViewById(R.id.btn_negative);
        btn_save = findViewById(R.id.btn_positive);
        et_title = findViewById(R.id.et_Title);
        et_memo = findViewById(R.id.et_memo);
        repeatCheck = (CheckBox) findViewById(R.id.repeatCheck);
//        TextView booltest = findViewById(R.id.booltest);

        cl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_negative:
                        finish();
                        break;
                    case R.id.btn_positive:
//                        intent = new Intent();
//                        intent.putExtra("",et_title.getText().toString());
//                        intent.putExtra("",et_memo.getText().toString());
                        String title = et_title.getText().toString();
                        String subtitle = et_memo.getText().toString();
                        boolean scheduleLoof = repeatCheck.isChecked();
//                        booltest.setText(String.valueOf(scheduleLoof));
                        break;
                    default:
                        break;
                }
            }
        };
        btn_save.setOnClickListener(cl);
        btn_back.setOnClickListener(cl);
    }

}
