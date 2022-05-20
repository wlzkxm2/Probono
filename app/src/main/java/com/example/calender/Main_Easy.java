package com.example.calender;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Main_Easy extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    RecyclerView recyclerView;
    int listDB = 10;
    private List_ItemAdapter_Easy list_itemAdapter_easy;
    TextView now, mon;

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
        SimpleDateFormat dateFormat = new SimpleDateFormat("M월");
        String getTime = dateFormat.format(date);
        return getTime;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_easy);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 왼쪽 상단 버튼 만들기
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu); //왼쪽 상단 버튼 아이콘 지정

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView = (NavigationView)findViewById(R.id.navigation_view);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_easy);
        list_itemAdapter_easy = new List_ItemAdapter_Easy();
        recyclerView.setAdapter(list_itemAdapter_easy);

        now = (TextView) findViewById(R.id.main_easy_now);
        now.setText(getTime());

        mon = (TextView) findViewById(R.id.main_easy_month);
        mon.setText(getMonth());

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

//          일단 날짜를 구해와야함. 월 말고 날만 가져오면 됨
//        가져온 날짜를 구분하여 주말과 평일의 색을 다르게 설정 해야함
//        가져온 날짜에서 오늘 날짜를 구분하여 표시를 해줌
//        가져온 날짜 중에서 오늘 날짜를 찾아서 첫 화면에 표시해야함
//        가져온 날짜들을 좌우로 스와이핑하여 날짜를 바꿀 수 있음
//        바꾼 날짜마다 일정 화면이 있음
