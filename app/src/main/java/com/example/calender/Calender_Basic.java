package com.example.calender;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

public class Calender_Basic extends Activity {

    private MaterialCalendarView calender;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calender_basic);

        calender = (MaterialCalendarView) findViewById(R.id.calendarView);

    }
}
