package com.example.calender;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.calender.DataBase.Calender_DB;
import com.example.calender.DataBase.Calender_DBSet;
import com.example.calender.DataBase.Calender_Dao;
import com.example.calender.StaticUidCode.UidCode;
import com.example.calender.calendarSource.SaturdayDecorator;
import com.example.calender.calendarSource.SundayDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter;
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter;

import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class Calender_Basic extends Activity  {


    public Calender_Dao calender_dao;

    // 에러 표시를 위한 태그
    private static final String TAG = "Calender_Basic";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE, d MMM yyyy");

    private MaterialCalendarView calender;

    TextView now;
    RecyclerView recyclerView;
    List_ItemAdapter list_itemAdapter;
    Button addcal_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calender_basic);

        Log.v("Frag", "온크리에이트 뷰ㅇ입니다.");

        final int[] _day = {0};
        final int[] _month = {0};
        final int[] _year = {0};

        final int[] appData = {0}; // ??

        addcal_btn = (Button) findViewById(R.id.Addcal_btn);

        //<editor-fold desc="기본 뷰 세팅 코드">
        calender = (MaterialCalendarView) findViewById(R.id.calendarView);
        recyclerView = findViewById(R.id.recycler_view);
        list_itemAdapter = new List_ItemAdapter();
        recyclerView.setAdapter(list_itemAdapter);

        list_itemAdapter.removeAllItem();

        //<editor-fold desc="DB 기본 세팅 코드">

        // 캘린더 데이터베이스 정의
        Calender_DBSet dbController = Room.databaseBuilder(getApplicationContext(), Calender_DBSet.class, "CalenderDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        calender_dao = dbController.calender_dao();

        List<Calender_DB> calender_dbs = calender_dao.getAllData();
        //</editor-fold>

        //<editor-fold desc=" 달력 꾸미기"
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
        // </editor-fold>

        calender.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(MaterialCalendarView materialCalendarView, CalendarDay date, boolean b) {
                Calender_Basic schedulenode = new Calender_Basic();

                String month_t = "",
                        week_t = "",
                        title = "",
                        subtitle = "",
                        outputData = "";

                int time = 0000;

                //<editor-fold desc="누른 일자를 구하는 함수">
                final int day = date.getDay();
                final int month = date.getMonth();
                final int year = date.getYear();
                String week = new SimpleDateFormat("EE").format(date.getCalendar().getTime());

                month_t = year + "년 " + month + "월 " + day + "일";
                week_t = week;
                _day[0] = day;
                _month[0] = month+1;
                _year[0] = year;

                // <editor-fold desc="일자 데이터를 전역변수에 전송">
                ((UidCode) getApplication()).setStatic_year(_year[0]);
                ((UidCode) getApplication()).setStatic_month(_month[0]);
                ((UidCode) getApplication()).setStatic_day(_day[0]);
                ((UidCode) getApplication()).setWeek(week);
                // </editor-fold>

                List<Calender_DB> calender_like_years = calender_dao.loadAllDataByYears(_year[0], _month[0], _day[0]);
                appData[0] = calender_like_years.size();
                Toast.makeText(getApplication(), _year[0] + "-" + _month[0] + "-" + _day[0], Toast.LENGTH_SHORT).show();

                list_itemAdapter.removeAllItem();

                for (int i = 0; i < calender_like_years.size(); i++) {
                    List_Item calList = new List_Item();
                    calList.setTime(Integer.toString(calender_like_years.get(i).get_time()));
                    calList.setTitle(calender_like_years.get(i).get_titles());
                    calList.setTitle(calender_like_years.get(i).get_subtitle());

                    list_itemAdapter.addItem(calList);
                }
                list_itemAdapter.notifyDataSetChanged();
                recyclerView.startLayoutAnimation();

            }
        });

        addcal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputdata();
            }
        });
        list_itemAdapter.notifyDataSetChanged();
        recyclerView.startLayoutAnimation();

//        calender.setOnDateChangedListener(this);
/*
        calender.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                List<Calender_DB> cheakDB = calender_dao.getAllData();  // 데이터를 저장하기 위해 저장된 모든 데이터 출력
                Calender_Basic schedulenode = new Calender_Basic();

                String month_t = "";
                String week_t = "";

                String title = "";
                String subtitle = "";

                String outputData = "";
                int time = 0000;

                //<editor-fold desc="누른 일자를 구하는 함수">
                final int day = date.getDay();
                final int month = date.getMonth();
                final int year = date.getYear();
                _day[0] = day;
                _month[0] = month;
                _year[0] = year;

                ((UidCode) getApplication()).setStatic_year(_year[0]);
                ((UidCode) getApplication()).setStatic_month(_month[0]);
                ((UidCode) getApplication()).setStatic_day(_day[0]);

                String week = new SimpleDateFormat("EE").format(date.getCalendar().getTime());

                month_t = year + "년 " + month + "월 " + day + "일";
                week_t = week;
                //</editor-fold>

                for (int i = 0; i < cheakDB.size(); i++) {
                    if (cheakDB.get(i)._firstData == false) {
                        if (cheakDB.get(i)._years == year
                                && cheakDB.get(i)._month == month
                                && cheakDB.get(i)._day == day) {
                            title = cheakDB.get(i).get_titles();
                            subtitle = cheakDB.get(i).get_subtitle();
                            time = cheakDB.get(i).get_time();

                            outputData += "시간 : " + cheakDB.get(i).get_time() +
                                    " 일정 : " + cheakDB.get(i).get_titles() +
                                    " 세부일정 : " + cheakDB.get(i).get_subtitle() +
                                    "\n";

                        }
                    }
                }
                months_text.setText(month_t);
                days_text.setText(week_t);

                scaView_text.setText(outputData);
            }
        });

        inputdata_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calender_DB calender_db = new Calender_DB();

                int hour = 0, minute = 0;
                int year, month, day;
                int time_;
                int uid = ((UidCode) getApplication()).getUserCode();

                String log = "";        // 년월일
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                }

                // 전역 변수에 저장된 변수를 불러오기
                year = ((UidCode) getApplication()).getStatic_year();
                month = ((UidCode) getApplication()).getStatic_month();
                day = ((UidCode) getApplication()).getStatic_day();
                time_ = (hour*100) + minute;

                Log.v(TAG,  time_ + " /");
//                Log.v(TAG, log);



                calender_db.setUid(uid+1);
                calender_db.set_years(year);
                calender_db.set_month(month);
                calender_db.set_day(day);
                calender_db.set_time(time_);
                calender_db.set_firstData(false);
                calender_db.set_titles(inputSca_edit.getText().toString());
                calender_db.set_subtitle(null);
                calender_dao.insertAll(calender_db);

            }
        });
        */
    }

    public void inputdata(){
        // 데이터베이스에 최초 데이터 출력

        for (int i = 0; i < 10; i++) {
            Calender_DB calender_db = new Calender_DB();
            calender_db.setUid(((UidCode) getApplication()).getUserCode());
            calender_db.set_years(2022);
            calender_db.set_month(5);
            calender_db.set_day(6);
            calender_db.set_time(10 + i);
            calender_db.set_firstData(true);
            calender_db.set_titles(Integer.toString(i));
            calender_db.set_subtitle(Integer.toString(i) + " " + Integer.toString(i));
            calender_dao.insertAll(calender_db);
        }

    }


}

