package com.example.calender.Main_Easy;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calender.Main_Basic.List_Item;
import com.example.calender.R;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Main_Easy extends AppCompatActivity {

    //싱글 어댑터
    private ArrayList<Main_Easy_Calendar_Day> days = new ArrayList<>();
    private Single_Adapter singleAdapter;

    Main_Easy_Calendar_Adapter main_easy_calendar_adapter;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    RecyclerView recyclerView, calendar_recyclerView;
    String jan = "1", feb = "2", mar = "3", apr = "4", may = "5", jun = "6", jul = "7", aus = "8", sep = "9", oct = "10", nov = "1", dec = "12";

    int listDB = 10;
    private List_ItemAdapter_Easy list_itemAdapter_easy;
    TextView now, month;
    ImageButton next, previous;
    View.OnClickListener cl;
    String str, c;
    int a, b;
    String year = getYear();
    String today=getToday();

    private String getTime() { //현재 시간 가져오기
        long now = System.currentTimeMillis(); // 현재 시간을 now 변수에 넣음
        Date date = new Date(now); // 현재 시간을 date 형식으로 변환
        SimpleDateFormat dateFormat = new SimpleDateFormat("h시 m분");
        String getTime = dateFormat.format(date);
        return getTime;
    }

    private String getMonth() { //현재 월 가져오기
        long now = System.currentTimeMillis(); // 현재 시간을 now 변수에 넣음
        Date date = new Date(now); // 현재 시간을 date 형식으로 변환
        SimpleDateFormat dateFormat = new SimpleDateFormat("M");
        String getTime = dateFormat.format(date);
        return getTime;
    }

    private String getYear() { //현재 년도 4자리 가져오기
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        String getYear = dateFormat.format(date);

        return getYear;
    }

    private String getToday() { //오늘 날짜 가져오기
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("d");
        String getToday = dateFormat.format(date);

        return getToday;
    }

    private void getDay() {
        // 현재 월과 일치할 경우
        // 1,3,5,7,8,10,12월
        if (month.getText().toString().equals(jan) || month.getText().toString().equals(mar)
                || month.getText().toString().equals(may) || month.getText().toString().equals(jul)
                || month.getText().toString().equals(aus) || month.getText().toString().equals(oct)
                || month.getText().toString().equals(dec)) {   //31일까지 표시
            for (int i = 1; i <= 31; i++) {
                str = i + "";
                main_easy_calendar_adapter.setArrayData(str);
            }
        } else if (month.getText().toString().equals(feb)) // 2월인 경우 윤년 계산
        {
            if (Integer.parseInt(year) % 4 == 0) // 현재 년도를 4로 나눠서 0이 되면 윤년
            {   // 29일까지 표시
                for (int i = 1; i <= 29; i++) {
                    str = i + "";
                    main_easy_calendar_adapter.setArrayData(str);
                }
            } else // 윤년이 아니면
            {   // 28일까지 표시
                for (int i = 1; i <= 28; i++) {
                    str = i + "";
                    main_easy_calendar_adapter.setArrayData(str);
                }
            }
        } else // 나머지 4,6,9,11월
        {   // 30일까지 표시
            for (int i = 1; i <= 30; i++) {
                str = i + "";
                main_easy_calendar_adapter.setArrayData(str);
            }
        }

        calendar_recyclerView.setAdapter(main_easy_calendar_adapter);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_easy);

        next = (ImageButton) findViewById(R.id.main_easy_next);
        previous = (ImageButton) findViewById(R.id.main_easy_previous);

        now = (TextView) findViewById(R.id.main_easy_now);
        now.setText(getTime());

        month = (TextView) findViewById(R.id.main_easy_month);
        month.setText(getMonth()); // 현재 월 month에 저장. month.getText().toString()으로 현재 월 스트링 타입으로 쓸 수 있음

        cl = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.main_easy_next:
                        a = Integer.parseInt(month.getText().toString());
                        b = a + 1;
                        if (b > 12) // 12월에서 내년 1월로 넘어갈때
                        {
                            b = 1; // 표시할 월을 1월로 바꾸고
                            a = Integer.parseInt(year) + 1; // 현재 년도를 int로 바꿔서 +1
                            year = String.valueOf(a); // 다시 문자열로 돌려놓음
                        }
                        c = String.valueOf(b);
                        month.setText(c);
                        main_easy_calendar_adapter.removeArrayData();
                        getDay();
                        break;
                    case R.id.main_easy_previous:
                        a = Integer.parseInt(month.getText().toString());
                        b = a - 1;
                        if (b < 1) // 1월에서 작년 12월로 넘어갈때
                        {
                            b = 12; // 표시할 월을 1월로 바꾸고
                            a = Integer.parseInt(year) - 1; // 현재 년도를 int로 바꿔서 -1
                            year = String.valueOf(a); // 다시 문자열로 돌려놓음=
                        }
                        c = String.valueOf(b);
                        month.setText(c);
                        main_easy_calendar_adapter.removeArrayData();
                        getDay();
                        break;
                }
            }
        };
        next.setOnClickListener(cl);
        previous.setOnClickListener(cl);

        calendar_recyclerView = (RecyclerView)findViewById(R.id.recycler_view_easy_calendar_day);
        calendar_recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)) ; // 가로 스크롤

        //싱글 어댑터
        singleAdapter = new Single_Adapter(this,days);
        calendar_recyclerView.setAdapter(singleAdapter);

        main_easy_calendar_adapter = new Main_Easy_Calendar_Adapter();
        calendar_recyclerView.scrollToPosition(Integer.parseInt(today)-1); // 첫 실행시 오늘날짜 기준으로 보여줌
        getDay();

//        toolbar = (Toolbar)findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("");
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 왼쪽 상단 버튼 만들기
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu); //왼쪽 상단 버튼 아이콘 지정
//
//        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
//        navigationView = (NavigationView)findViewById(R.id.navigation_view);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_easy);
        list_itemAdapter_easy = new List_ItemAdapter_Easy();
        recyclerView.setAdapter(list_itemAdapter_easy);

        //화면 클리어
        list_itemAdapter_easy.removeAllItem();
        //샘플 데이터 생성
        for (int i = 0; i < listDB; i++) {
            List_Item list_item = new List_Item();
            list_item.setTime("14:00" + "-" + i);
            list_item.setTitle("과제하기" + "-" + i);
            //데이터 등록
            list_itemAdapter_easy.addItem(list_item);
        }

        //적용
        list_itemAdapter_easy.notifyDataSetChanged();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ // 왼쪽 상단 버튼 눌렀을 때
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() { //뒤로가기 했을 때
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}

