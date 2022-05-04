package com.example.calender;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.room.Room;

import com.example.calender.DataBase.Calender_DB;
import com.example.calender.DataBase.Calender_DBSet;
import com.example.calender.DataBase.Calender_Dao;
import com.example.calender.calendarSource.SaturdayDecorator;
import com.example.calender.calendarSource.SundayDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter;
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;


public class Calender_Basic extends Activity implements OnDateSelectedListener{

    public Calender_Dao calender_dao;

    // 에러 표시를 위한 태그
    private  static final String TAG = "Calender_Basic";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE, d MMM yyyy");

    private MaterialCalendarView calender;

    TextView months_text, days_text, scaView_text;
    EditText inputSca_edit;

    Button inputdata_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calender_basic);

        //<editor-fold desc="기본 뷰 세팅 코드">
        calender = (MaterialCalendarView) findViewById(R.id.calendarView);

        months_text = (TextView) findViewById(R.id.MonthData_text);
        days_text = (TextView) findViewById(R.id.DayData_text);
        scaView_text = (TextView) findViewById(R.id.ScaView_text);

        inputSca_edit = (EditText) findViewById(R.id.InputSca_edit);

        inputdata_btn = (Button) findViewById(R.id.InputData_btn);
        //</editor-fold>

        //<editor-fold desc="DB 기본 세팅 코드">
        
        // 캘린더 데이터베이스 정의
        Calender_DBSet dbController = Room.databaseBuilder(getApplicationContext(), Calender_DBSet.class, "CalenderDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        calender_dao = dbController.calender_dao();

        List<Calender_DB> calender_dbs = calender_dao.getAllData();
        //</editor-fold>
        
        // 캘린더 함수
        // 주말엔 색 다르게 변경
        calender.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator()
        );

        // 월 한글로
        calender.setTitleFormatter(
                new MonthArrayTitleFormatter(
                        getResources().getTextArray(R.array.custom_months)
                )
        );
        // 요일 한글로
        calender.setWeekDayFormatter(
                new ArrayWeekDayFormatter(
                        getResources().getTextArray(R.array.custom_weekdays)
                )
        );
        calender.setOnDateChangedListener(this);

        inputdata_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //<editor-fold desc="캘린더 다수 선택시 선택한 일자 추출"
//                List<CalendarDay> days = calender.getSelectedDates();
//
//                String selectdays="";
//
//                for (int i = 0; i < days.size(); i++) {
//                    CalendarDay calendar = days.get(i);
//                    final int day = calendar.getDay();      // 월 출력
//                    final int month = calendar.getMonth();  // 일 출력
//                    final int year = calendar.getYear();    // 년 출력
//
//                    String week = new SimpleDateFormat("EE").format(calendar.getCalendar().getTime());
//                    String day_full = year + "년" + (month+1) + "월" + day + "일" + week + "요일";
//
//                    selectdays += (day_full + "\n");
//                }
//
//                Toast.makeText(Calender_Basic.this, selectdays, Toast.LENGTH_LONG).show();
                //</editor-fold>

            }
        });

    }


    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        String month_t = "";
        String week_t = "";

        final int day = date.getDay();
        final int month = date.getMonth();
        final int year = date.getYear();
        String week = new SimpleDateFormat("EE").format(date.getCalendar().getTime());

        month_t = year + "년 " + month + "월 " + day + "일";
        week_t = week;
        // 테스트 추가

        months_text.setText(month_t);
        days_text.setText(week_t);
    }
}

