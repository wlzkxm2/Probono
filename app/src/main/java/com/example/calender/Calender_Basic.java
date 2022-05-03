package com.example.calender;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.room.Room;

import com.example.calender.DataBase.Calender_AppDB;
import com.example.calender.calendarSource.SaturdayDecorator;
import com.example.calender.calendarSource.SundayDecorator;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

public class Calender_Basic extends Activity {

    private MaterialCalendarView calender;

    TextView months_text, days_text, scaView_text;
    EditText inputSca_edit;

    Button inputdata_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calender_basic);

        calender = (MaterialCalendarView) findViewById(R.id.calendarView);

        months_text = (TextView) findViewById(R.id.MonthData_text);
        days_text = (TextView) findViewById(R.id.DayData_text);
        scaView_text = (TextView) findViewById(R.id.ScaView_text);

        inputSca_edit = (EditText) findViewById(R.id.InputSca_edit);

        inputdata_btn = (Button) findViewById(R.id.InputData_btn);

        Calender_AppDB cdb = Room.databaseBuilder(getApplicationContext(), Calender_AppDB.class, "CalenderDB").build();

        calender.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator()
        );

    }
}

