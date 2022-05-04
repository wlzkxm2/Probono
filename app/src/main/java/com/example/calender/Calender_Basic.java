package com.example.calender;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;

// 리스트를 구성하는 소스
class scheduleNode{
    private int schedule_time;
    private String schedule_title;       // 데이터 저장변수
    private String schedule_subtitle;

    public scheduleNode link;       // 다르노드를 참조할 링크노드

    public scheduleNode(){
        this.schedule_time = 0;
        this.schedule_title = null;
    }

    public scheduleNode(int time, String title, String subtitle){
        this.schedule_time = time;
        this.schedule_title = title;
        this.schedule_subtitle = subtitle;
        this.link = null;
    }

    public scheduleNode(int time, String title, String subtitle, scheduleNode link){
        this.schedule_time = time;
        this.schedule_title = title;
        this.schedule_subtitle = subtitle;
        this.link = link;
    }

    

    public int getData(){
//        String timeSet = Integer.toString(schedule_time);
        return this.schedule_time;
    }

    public String getTitle(){
        return this.schedule_title;
    }

    public String getsubTitle(){
        return this.schedule_subtitle;
    }


}

public class Calender_Basic extends Activity  {

    private scheduleNode head;  // scheduleNode 타입의 인스턴수 변수

    public Calender_Basic() {
        head = null;
    }

    // Node 중간에 삽입
    public void insert_schedule_title(scheduleNode preNode,
                                      int time,
                                      String title,
                                      String subtitle) {
        scheduleNode newNode = new scheduleNode(time, title, subtitle);    // 새로운 노드생성
        newNode.link = preNode.link;        // preNode.link는 preNode의 노드 참조
        // preNode 가 newNode 를 가르키고 Newnode는 preNode의 다음노드 가르킴
        preNode.link = newNode;
    }

    // 노드 마지막에 삽입
    public void insert_schedule_title(int time, String title, String subtitle) {
        scheduleNode newNode = new scheduleNode(time, title, subtitle);

        if (head == null) {
            // head 노드가 null일때 head 참조
            this.head = newNode;
        } else {
            // head노드가 null 이 아닐때 다음 노드 참조
            scheduleNode tempNode = head;

            /*temp 노드의 다음이 null이 아닐때까지 다음 노드 참조
            while문이 모두 실행되면 tempNode는 가장 마지막 노드 참조
            */
            while (tempNode.link != null) {
                tempNode = tempNode.link;
            }

            tempNode.link = newNode;
        }
    }

    // Node 중간 삭제
    public void deleteNode(int time) {
//        String times = Integer.toString(time);
        scheduleNode preNode = head;
        scheduleNode tempNode = head.link;

        // 데이터가 preNode의 데이터와 일치하는 경우
        if (time == preNode.getData()) {
            head = preNode.link;
            preNode.link = null;
        } else {
            while (tempNode != null) {
                if (time == tempNode.getData()) {
                    if (tempNode.link == null) {
                        preNode = null;
                    } else {
                        preNode.link = tempNode.link;
                        tempNode.link = null;
                    }
                    break;
                } else {
                    preNode = tempNode;
                    tempNode = tempNode.link;
                }
            }
        }
    }

    public void deleteNode() {
        scheduleNode preNode;
        scheduleNode tempNode;

        // head 노드가 null이면 모든 노드가 삭제됫음을 확인
        if (head == null) {
            return;
        }

        // head 의 링크가 하나일경우 노드와에 연결을 끊음
        if (head.link == null) {
            head = null;
        } else {
            // head가 가르키는 영역을 preNode
            preNode = head;
            // head 다음 영역을 tempNode로 성정
            tempNode = head.link;

            // tempNode의 다음이 없을떄
            while (tempNode.link != null) {
                // preNode를 tempNode로 바꿈
                preNode = tempNode;
                tempNode = tempNode.link;
            }

            preNode.link = null;
        }
    }

    public scheduleNode searchNode(int time) {
        scheduleNode currentNode = this.head;

        while (currentNode != null) {
            if (time == currentNode.getData()) {
                return currentNode;
            } else {
                currentNode = currentNode.link;
            }
        }
        return currentNode;
    }

    public void printList() {
        scheduleNode currentNode = this.head;

        while (currentNode != null) {
            Log.v(TAG, Integer.toString(currentNode.getData()) + "시 "
                    + currentNode.getTitle() + " : 일정 "
                    + currentNode.getsubTitle() + " : 메모");
        }
    }

    public Calender_Dao calender_dao;

    // 에러 표시를 위한 태그
    private static final String TAG = "Calender_Basic";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE, d MMM yyyy");

    private MaterialCalendarView calender;

    TextView months_text, days_text, scaView_text;
    EditText inputSca_edit;

    Button inputdata_btn;

    TimePicker timePicker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calender_basic);

        scheduleNode schedulenode = new scheduleNode();

        final int[] _day = {0};
        final int[] _month = {0};
        final int[] _year = {0};

        //<editor-fold desc="기본 뷰 세팅 코드">
        calender = (MaterialCalendarView) findViewById(R.id.calendarView);

        months_text = (TextView) findViewById(R.id.MonthData_text);
        days_text = (TextView) findViewById(R.id.DayData_text);
        scaView_text = (TextView) findViewById(R.id.ScaView_text);

        inputSca_edit = (EditText) findViewById(R.id.InputSca_edit);

        inputdata_btn = (Button) findViewById(R.id.InputData_btn);

        timePicker = (TimePicker) findViewById(R.id.TimeSet_picker);
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
//        calender.setOnDateChangedListener(this);

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
                String time_ = "";
                String log = "";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                }

                time_ = Integer.toString(hour) + Integer.toString(minute);
                log = Integer.toString(_year[0]) + "년 " +
                        Integer.toString(_month[0]) + "월 " +
                        Integer.toString(_day[0]) + "일";

                Log.v(TAG,  time_ + " /");
                Log.v(TAG, log);

//                calender_db.setUid(((UidCode) getApplication()).getUserCode());
//                calender_db.set_years(0);
//                calender_db.set_month(0);
//                calender_db.set_day(0);
//                calender_db.set_day(0000);
//                calender_db.set_firstData(true);
//                calender_db.set_titles(null);
//                calender_db.set_subtitle(null);
//                calender_dao.insertAll(calender_db);

            }
        });
    }
}

//    @Override
//    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
//        List<Calender_DB> cheakDB = calender_dao.getAllData();  // 데이터를 저장하기 위해 저장된 모든 데이터 출력
//        scheduleNode schedulenode = new scheduleNode();
//
//        String month_t = "";
//        String week_t = "";
//
//        String title = "";
//        String subtitle = "";
//        int time = 0000;
//
//        //<editor-fold desc="누른 일자를 구하는 함수">
//        final int day = date.getDay();
//        final int month = date.getMonth();
//        final int year = date.getYear();
//        String week = new SimpleDateFormat("EE").format(date.getCalendar().getTime());
//
//        month_t = year + "년 " + month + "월 " + day + "일";
//        week_t = week;
//        //</editor-fold>
//
//        for (int i = 0; i <cheakDB.size(); i++) {
//            if (cheakDB.get(i)._firstData == false){
//                if (cheakDB.get(i)._years == year
//                        && cheakDB.get(i)._month == month
//                        && cheakDB.get(i)._day == day){
//                    title = cheakDB.get(i).get_titles();
//                    subtitle = cheakDB.get(i).get_subtitle();
//                    time = cheakDB.get(i).get_time();
//
//                    schedulenode.
//
//                }
//            }
//        }
//
//        months_text.setText(month_t);
//        days_text.setText(week_t);
//    }
//}

