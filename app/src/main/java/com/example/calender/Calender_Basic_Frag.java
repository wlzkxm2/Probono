package com.example.calender;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.calender.StaticUidCode.UidCode;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Calender_Basic_Frag extends Fragment {

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

        // 날짜를 입력받기 위한 배열
        // 배열로 밖에 데이터를 못받기 때문에 배열로 선언
        final int[] _year = {0};
        final int[] _month = {0};
        final int[] _day = {0};
        final int[] appData = {0};  // DB 내에 있는 데이터의 사이즈

        addcal_btn = (Button) view.findViewById(R.id.Addcal_btn);

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

                Toast.makeText(getActivity().getApplication(), _year[0] + "-" + _month[0] + "-" + _day[0], Toast.LENGTH_SHORT).show();

                list_itemAdapter.removeAllItem();

                for (int i = 0; i < calender_like_data.size(); i++) {
                    List_Item calList = new List_Item();
                    calList.setTime(Integer.toString(calender_like_data.get(i).get_time()));
                    calList.setTitle(calender_like_data.get(i).get_titles());
                    calList.setText(calender_like_data.get(i).get_subtitle());

                    list_itemAdapter.addItem(calList);
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

}
