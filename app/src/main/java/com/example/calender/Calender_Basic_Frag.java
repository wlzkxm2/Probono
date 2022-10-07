package com.example.calender;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.calender.DataBase.Calender_DB;
import com.example.calender.DataBase.Calender_DBSet;
import com.example.calender.DataBase.Calender_Dao;
import com.example.calender.Main_Basic.List_Item;
import com.example.calender.Main_Basic.List_ItemAdapter;
import com.example.calender.calendarSource.Calendar_Basic_Scheduled;
import com.example.calender.StaticUidCode.UidCode;
import com.example.calender.addschedule.AddSchedule;
import com.example.calender.calendarSource.SaturdayDecorator;
import com.example.calender.calendarSource.SundayDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class Calender_Basic_Frag extends Fragment {

    // 일정 없는 날 일정추가버튼 표시
    private ImageButton nolist_add;
    private TextView nolist_add_text;

    public Calender_Dao calender_dao;       // 데이터 베이스 구축을 위한 객체

    // 에러 표시를 위한 태그
    private static final String TAG = "ErrorTag";
    // 시간 포멧을 위한 양식
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE, d, MMM,, yyyy");

    private MaterialCalendarView calendarView;  // calenderView 객체 생상

    TextView now;
    RecyclerView recyclerView;
    List_ItemAdapter list_itemAdapter;
    Button addcal_btn;

    public static Calender_Basic_Frag newInstance() {
        Calender_Basic_Frag Calender_Basic = new Calender_Basic_Frag();
        return Calender_Basic;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calender_basic, container, false);
        Log.v(TAG, "Frag 뷰입니다");

        // 일정 없는 날 일정추가버튼 표시
        nolist_add = (ImageButton) view.findViewById(R.id.calendar_basic_nolist_add);
        nolist_add_text = (TextView) view.findViewById(R.id.calendar_basic_nolist_add_text);

        // 날짜를 입력받기 위한 배열
        // 배열로 밖에 데이터를 못받기 때문에 배열로 선언
        final int[] _year = {0};
        final int[] _month = {0};
        final int[] _day = {0};
        final int[] appData = {0};  // DB 내에 있는 데이터의 사이즈

//        addcal_btn = (Button) view.findViewById(R.id.Addcal_btn);

        //<editor-fold desc="기본 뷰 세팅 코드">
        calendarView = (MaterialCalendarView) view.findViewById(R.id.calendarView);
        recyclerView = view.findViewById(R.id.recycler_view);
        list_itemAdapter = new List_ItemAdapter();
        recyclerView.setAdapter(list_itemAdapter);

        list_itemAdapter.removeAllItem();
        //</editor-fold desc="기본 뷰 세팅 코드">

        //<editor-fold desc="DB 기본 세팅 코드">
        Calender_DBSet dbController = Room.databaseBuilder(getActivity().getApplicationContext(), Calender_DBSet.class, "CalenderDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        calender_dao = dbController.calender_dao();

        List<Calender_DB> calender_dbs = calender_dao.getAllData();

        // 일정 있는 날에 빨간 점 표시
        for (int i = 0; i < calender_dbs.size(); i++){
            int calS_years = calender_dbs.get(i).getStart_years();
            int calS_months = calender_dbs.get(i).getStart_month();
            int calS_days = calender_dbs.get(i).getStart_day();

            int calE_years = calender_dbs.get(i).getEnd_years();
            int calE_months = calender_dbs.get(i).getEnd_month();
            int calE_days = calender_dbs.get(i).getEnd_day();

            calendarView.addDecorators(
                    new SundayDecorator(),
                    new SaturdayDecorator(),
                    new Calendar_Basic_Scheduled(Color.RED, Collections.singleton(CalendarDay.from(
                            calS_years,
                            calS_months-1,
                            calS_days)))
            );

        }

//        calendarView.addDecorators(
//                new SundayDecorator(),
//                new SaturdayDecorator(),
//                new Calendar_Basic_Scheduled(Color.RED, Collections.singleton(CalendarDay.from(
//                        2022,
//                        8,
//                        3)))
//        );

        //</editor-fold>
/*
        //<editor-fold desc="달력 꾸미기">
        // 주말엔 색 다르게 변경
        calendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator()
        );

        // 월 한글로
        calendarView.setTitleFormatter(
                new MonthArrayTitleFormatter(
                        getResources().getTextArray(R.array.custom_weekdays)
                )
        );

        //</editor-fold desc="달력 꾸미기">
*/

        //<editor-fold desc="캘린더에 일자가 눌렷을떄">
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Calender_Basic_Frag calender_basic_frag = new Calender_Basic_Frag();



                String month_t = "",
                        week_t = "",
                        title = "",
                        subtitle = "",
                        outputData = "";

                int time = 0000;      // 시간은 앞 두개 분은 뒤 두개
                                        // 예를 들어 12시 39분 일때 1239

                //<editor-fold desc="누른 일자를 구하는 함수">
                _day[0] = date.getDay();
                _month[0] = date.getMonth() + 1;
                _year[0] = date.getYear();
                String week = new SimpleDateFormat("EE")
                        .format(date.getCalendar().getTime());

                month_t = _year[0] + "년 " + _month[0] + "월 " + _day[0] + "일";
                week_t = week;

                ((UidCode) getActivity().getApplication()).setStatic_year(_year[0]);
                ((UidCode) getActivity().getApplication()).setStatic_month(_month[0]);
                ((UidCode) getActivity().getApplication()).setStatic_day(_day[0]);
                ((UidCode) getActivity().getApplication()).setWeek(week);
                //</editor-fold desc="누른 일자를 구하는 함수">

                // 데이터베이스 데이터 호출
                List<Calender_DB> calender_like_data = calender_dao.loadAllDataByYears(
                        _year[0],
                        _month[0],
                        _day[0]
                );
                appData[0] = calender_like_data.size();

//                Toast.makeText(getActivity().getApplication(), _year[0] + "-" + _month[0] + "-" + _day[0], Toast.LENGTH_SHORT).show();

                Log.v("HSH", Integer.toString(((UidCode) getActivity().getApplication()).getStatic_day()));

                list_itemAdapter.removeAllItem();

                if (calender_like_data.isEmpty()) { // 캘린더 눌러서 일정이 없을때
                    Log.v("일정이 없는 날입니다", Integer.toString(((UidCode) getActivity().getApplication()).getStatic_day()));
                    // 일정 없는 날 일정추가버튼 표시
                    nolist_add.setVisibility(View.VISIBLE);
                    nolist_add_text.setVisibility(View.VISIBLE);
                    nolist_add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent n = new Intent(getActivity(), AddSchedule.class);
                            n.putExtra("100",1);
                            startActivity(n);
                        }
                    });

                } else {
                    // 일정 있으면 추가버튼 없애기
//                    nolist_add.setVisibility(View.GONE);
//                    nolist_add_text.setVisibility(View.GONE);
                    for (int i = 0; i < calender_like_data.size(); i++) {
                        List_Item calList = new List_Item();
                        String startTime = String.format("%04d", calender_like_data.get(i).getStart_time());
                        String valueStartTime = startTime.substring(0,2) + " : " + startTime.substring(2, startTime.length());
                        String EndTime = String.format("%04d", calender_like_data.get(i).getEnd_time());
                        String valueEndTime = EndTime.substring(0,2) + " : " + EndTime.substring(2, EndTime.length());

                        calList.setTime(valueStartTime + "~ \n" + valueEndTime);
                        calList.setTitle(calender_like_data.get(i).get_titles());
                        calList.setText(calender_like_data.get(i).get_subtitle());

                        list_itemAdapter.addItem(calList);
//                        list_itemAdapter.addItem(calList); //두개 써있어서 하나 주석 해둠
                    }
                    list_itemAdapter.notifyDataSetChanged();
                    recyclerView.startLayoutAnimation();
                }
                list_itemAdapter.notifyDataSetChanged();
                recyclerView.startLayoutAnimation();


            }
        });
        //</editor-fold desc="캘린더에 일자가 눌렷을떄">

        list_itemAdapter.notifyDataSetChanged();
        recyclerView.startLayoutAnimation();

        return view;
    }

//    Bundle extra = getArguments();
//    if (extra != null) {
//        int data = extra.getInt("200");
//        if (data == 200) {
//
//        }
//    }

}
