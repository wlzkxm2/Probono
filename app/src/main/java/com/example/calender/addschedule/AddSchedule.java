package com.example.calender.addschedule;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;
import android.widget.TimePicker;


import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calender.R;
import com.example.calender.StaticUidCode.UidCode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddSchedule extends Activity {

    Button btn_save, btn_back;
    EditText et_title,et_memo;
    CheckBox allDayCheck;
    View.OnClickListener cl;
    TextView startDate, endDate, startTime, endTime;
    int startYears,startMonths,startDays, endYears,endMonths,endDays,startHour,startMinute,endHour,endMinute;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_schedule);

        btn_back = findViewById(R.id.btn_negative);
        btn_save = findViewById(R.id.btn_positive);
        et_title = findViewById(R.id.et_Title);
        et_memo = findViewById(R.id.et_memo);
        startDate = findViewById(R.id.startDate);
        endDate = findViewById(R.id.endDate);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        allDayCheck = (CheckBox) findViewById(R.id.allDayCheck);

//      TODO 초기 시간값 설정해주기 : start time = 00:00 / end time = 23:59

        startYears = ((UidCode) getApplication()).getStatic_year();
        startMonths = ((UidCode) getApplication()).getStatic_month();
        startDays = ((UidCode) getApplication()).getStatic_day();
        endYears = ((UidCode) getApplication()).getStatic_year();
        endMonths = ((UidCode) getApplication()).getStatic_month();
        endDays = ((UidCode) getApplication()).getStatic_day();
        startDate.setText(startYears + "년" + startMonths + "월" + startDays + "일");
        endDate.setText(endYears + "년" + endMonths + "월" + endDays + "일");
        

        cl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_negative:
                        finish();
                        break;
                    case R.id.btn_positive:
                        int saveStartYears = startYears;
                        int saveStartMonths = startMonths;
                        int saveStartDays = startDays;
                        int saveEndYears = startYears;
                        int saveEndMonths = endMonths;
                        int saveEndDays = endDays;
                        int saveStartTime =  (startHour * 100) + (startMinute);
                        int saveEndTime = (endHour * 100) + (endMinute);
                        String title = et_title.getText().toString();
                        String subtitle = et_memo.getText().toString();
                        boolean scheduleLoof = allDayCheck.isChecked();
                        if(allDayCheck.isChecked()){
                            saveStartTime = 0;
                            saveEndTime = 2359;
                        }

                        Log.d("MyTag",String.valueOf(saveStartYears) + "년" + String.valueOf(saveStartMonths) + "월" + String.valueOf(saveStartDays) + "일");
                        Log.d("MyTag",String.valueOf(saveEndYears) + "년" + String.valueOf(saveEndMonths) + "월" + String.valueOf(saveEndDays) + "일");
                        Log.d("MyTag",saveStartTime + " 부터 " +saveEndTime + " 까지");
                        Log.d("MyTag",title + " / " +subtitle);

                        break;
                    case R.id.allDayCheck:
                        if (allDayCheck.isChecked()){
                            startTime.setVisibility(View.GONE);
                            endTime.setVisibility(View.GONE);
                        }else{
                            startTime.setVisibility(View.VISIBLE);
                            endTime.setVisibility(View.VISIBLE);
                        }
                        break;
                    case R.id.startDate:
                        showStartDate();
                        break;
                    case R.id.endDate:
                        showEndDate();
                        break;
                    case R.id.startTime:
                        showStartTime();
                        break;
                    case R.id.endTime:
                        showEndTime();
                        break;
                    default:
                        break;
                }
            }
        };
        btn_save.setOnClickListener(cl);
        btn_back.setOnClickListener(cl);
        startDate.setOnClickListener(cl);
        endDate.setOnClickListener(cl);
        startTime.setOnClickListener(cl);
        endTime.setOnClickListener(cl);
        allDayCheck.setOnClickListener(cl);

    }

    public void showStartTime() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                startHour = hourOfDay;
                startMinute = minute;
                startTime.setText(startHour + "시" + startMinute + "분");
            }
        },0,0,true);
        timePickerDialog.show();
    }

    public void showEndTime() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                endHour = hourOfDay;
                endMinute = minute;
                endTime.setText(endHour + "시" + endMinute + "분");
            }
        },0,0,true);
        timePickerDialog.show();
    }
//TODO endDate 가 StartDate보다 이전 날짜일 경우 startDate = endDate 구현하기
    public void showEndDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                endYears = year;
                endMonths = month+1;
                endDays = dayOfMonth;
                endDate.setText(year + "년 " + (month + 1)+ "월 " + dayOfMonth + "일 ");

            }
        },((UidCode) getApplication()).getStatic_year(),((UidCode) getApplication()).getStatic_month()-1,((UidCode) getApplication()).getStatic_day());
        datePickerDialog.show();
    }

    public void showStartDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                startYears = year;
                startMonths = month+1;
                startDays = dayOfMonth;
                startDate.setText(year + "년 " + (month + 1)+ "월 " + dayOfMonth + "일 ");
            }
        },((UidCode) getApplication()).getStatic_year(),((UidCode) getApplication()).getStatic_month()-1,((UidCode) getApplication()).getStatic_day());
        datePickerDialog.show();
    }

}
