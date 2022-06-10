package com.example.calender.Main_Easy;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.calender.Calendar_Easy.Calendar_Easy;
import com.example.calender.DataBase.Calender_DBSet;
import com.example.calender.DataBase.Calender_Dao;
import com.example.calender.DataBase.UserDB;
import com.example.calender.DataBase.User_DBset;
import com.example.calender.DataBase.User_Dao;
import com.example.calender.Main_Basic.List_Item;
import com.example.calender.R;
import com.example.calender.UserProfile;
import com.example.calender.login;
import com.example.calender.setting.Setting_main_easy;
import com.example.calender.setting.Setting_notification;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class Main_Easy extends AppCompatActivity {

    //드로어 버튼
    private TextView drawer_user_setting, drawer_calendar, drawer_game, drawer_setting,
            drawer_welcome, drawer_username, drawer_user_address, drawer_username_nogin;
    private ImageView drawer_user_img, drawer_user_img_nogin;
    Calender_Dao calender_dao;
    User_Dao user_dao;

    // 디데이 변수
    int dateEndY, dateEndM, dateEndD;
    int ddayValue = 0;

    // 현재 날짜를 알기 위해 사용
    Calendar calendar;
    int currentYear, currentMonth, currentDay;

    // Millisecond 형태의 하루(24 시간)
    private final int ONE_DAY = 24 * 60 * 60 * 1000;

    //싱글 어댑터
    private ArrayList<Main_Easy_Calendar_Day> days = new ArrayList<>();
    private Single_Adapter singleAdapter;
    private int checkedPosition = -1;
    private Timer mTimer;

    Main_Easy_Calendar_Adapter main_easy_calendar_adapter;
    private DrawerLayout drawerLayout;
    private View drawerView;
    NavigationView navigationView;
    RecyclerView recyclerView, calendar_recyclerView;
    String jan = "1", feb = "2", mar = "3", apr = "4", may = "5", jun = "6", jul = "7",
            aus = "8", sep = "9", oct = "10", nov = "1", dec = "12";

    int listDB = 10;
    private List_ItemAdapter_Easy list_itemAdapter_easy;
    TextView now, month, dday, dday_text,d_day,d_day_text;
    ImageButton next, previous, drawer_btn;
    View.OnClickListener cl;
    String str, c;
    int a, b;
    String year = getYear();
    String today=getToday();

    // 현재 시간 실시간으로 구해오기

    private Handler mHandler = new Handler();

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {

            Date rightNow = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat(
                    "h시 m분");
            String dateString = formatter.format(rightNow);
            now.setText(dateString);

        }
    };

    class MainTimerTask extends TimerTask {
        public void run() {
            mHandler.post(mUpdateTimeTask);
        }
    }
    @Override
    protected void onDestroy() {
        mTimer.cancel();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        mTimer.cancel();
        super.onPause();
    }

    @Override
    protected void onResume() {
        MainTimerTask timerTask = new MainTimerTask();
        mTimer.schedule(timerTask, 500, 3000);
        super.onResume();
    }

    // DatePickerDialog띄우기, 종료일 저장, 기존에 입력한 값이 있으면 해당 데이터 설정후 띄우기
    private DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//            edit_endDateBtn.setText(year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일");
            ddayValue = ddayResult_int(dateEndY, dateEndM, dateEndD);
            d_day.setText(getDday(year, monthOfYear, dayOfMonth));
        }
    };

    // 설정한 디데이 year, mMonthOfYear : 설정한 디데이 MonthOfYear, mDayOfMonth : 설정한 디데이 DayOfMonth
    private String getDday(int mYear, int mMonthOfYear, int mDayOfMonth) {
        // D-day 설정
        final Calendar ddayCalendar = Calendar.getInstance();
        ddayCalendar.set(mYear, mMonthOfYear, mDayOfMonth);

        // D-day 를 구하기 위해 millisecond 으로 환산하여 d-day 에서 today 의 차를 구한다.
        final long dday = ddayCalendar.getTimeInMillis() / ONE_DAY;
        final long today = Calendar.getInstance().getTimeInMillis() / ONE_DAY;
        long result = dday - today;

        // 출력 시 d-day 에 맞게 표시
        String strFormat;
        if (result > 0) {
            strFormat = "D-%d";
        } else if (result == 0) {
            strFormat = "Today!";
        } else {
            result *= -1;
            strFormat = "D+%d";
        }

        final String strCount = (String.format(strFormat, result));
        return strCount;
    }

    // 디데이 값 계산
    public int onCalculatorDate (int dateEndY, int dateEndM, int dateEndD) {
        try {
            Calendar today = Calendar.getInstance(); //현재 오늘 날짜
            Calendar dday = Calendar.getInstance();

            //시작일, 종료일 데이터 저장
            Calendar calendar = Calendar.getInstance();
            int cyear = calendar.get(Calendar.YEAR);
            int cmonth = (calendar.get(Calendar.MONTH) + 1);
            int cday = calendar.get(Calendar.DAY_OF_MONTH);

            today.set(cyear, cmonth, cday);
            dday.set(dateEndY, dateEndM, dateEndD);// D-day의 날짜를 입력합니다.

            long day = dday.getTimeInMillis() / 86400000;
            // 각각 날의 시간 값을 얻어온 다음
            //( 1일의 값(86400000 = 24시간 * 60분 * 60초 * 1000(1초값) ) )

            long tday = today.getTimeInMillis() / 86400000;
            long count = tday - day; // 오늘 날짜에서 dday 날짜를 빼주게 됩니다.
            return (int) count; // 날짜는 하루 + 시켜줘야합니다.
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    // 디데이 값 계산한 결과값 출력
    public int ddayResult_int(int dateEndY, int dateEndM, int dateEndD) {
        int result = 0;
        result = onCalculatorDate(dateEndY, dateEndM, dateEndD);
        return result;
    }


    // onResume 까지 현재 시간 실시간으로 구해오기

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
        days = new ArrayList<>();
        // 현재 월과 일치할 경우
        // 1,3,5,7,8,10,12월
        if (month.getText().toString().equals(jan) || month.getText().toString().equals(mar)
                || month.getText().toString().equals(may) || month.getText().toString().equals(jul)
                || month.getText().toString().equals(aus) || month.getText().toString().equals(oct)
                || month.getText().toString().equals(dec)) {   //31일까지 표시
            for (int i = 1; i <= 31; i++) {
                Main_Easy_Calendar_Day day = new Main_Easy_Calendar_Day();
                day.setDay(""+i);
                days.add(day);
                singleAdapter.SetMain_Easy_Calendar_Day(days);
//                str = i + "";
//                main_easy_calendar_adapter.setArrayData(str);
            }
        } else if (month.getText().toString().equals(feb)) // 2월인 경우 윤년 계산
        {
            if (Integer.parseInt(year) % 4 == 0) // 현재 년도를 4로 나눠서 0이 되면 윤년
            {   // 29일까지 표시
                for (int i = 1; i <= 29; i++) {
                    Main_Easy_Calendar_Day day = new Main_Easy_Calendar_Day();
                    day.setDay(""+i);
                    days.add(day);
                    singleAdapter.SetMain_Easy_Calendar_Day(days);
//                    str = i + "";
//                    main_easy_calendar_adapter.setArrayData(str);
                }
            } else // 윤년이 아니면
            {   // 28일까지 표시
                for (int i = 1; i <= 28; i++) {
                    Main_Easy_Calendar_Day day = new Main_Easy_Calendar_Day();
                    day.setDay(""+i);
                    days.add(day);
                    singleAdapter.SetMain_Easy_Calendar_Day(days);
//                    str = i + "";
//                    main_easy_calendar_adapter.setArrayData(str);
                }
            }
        } else // 나머지 4,6,9,11월
        {   // 30일까지 표시
            for (int i = 1; i <= 30; i++) {
                Main_Easy_Calendar_Day day = new Main_Easy_Calendar_Day();
                day.setDay(""+i);
                days.add(day);
                singleAdapter.SetMain_Easy_Calendar_Day(days);
//                str = i + "";
//                main_easy_calendar_adapter.setArrayData(str);
            }
        }

        int dayslen = days.size();
        Log.v("maineasy", Integer.toString(dayslen));
        calendar_recyclerView.setAdapter(singleAdapter);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_easy);

        // 드로어
        drawer_btn = (ImageButton) findViewById(R.id.main_easy_drawer_btn);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawerView);
        drawerLayout.setDrawerListener(listener);
        drawer_user_setting = (TextView) findViewById(R.id.main_easy_user_setting);
        drawer_calendar = (TextView) findViewById(R.id.main_easy_calendar);
        drawer_game = (TextView) findViewById(R.id.main_easy_game);
        drawer_setting = (TextView) findViewById(R.id.main_easy_setting);
        drawer_welcome = (TextView) findViewById(R.id.main_easy_drawer_welcome);
        drawer_username = (TextView) findViewById(R.id.main_easy_user_name);
        drawer_user_address = (TextView) findViewById(R.id.main_easy_user_address);
        drawer_user_img = (ImageView) findViewById(R.id.main_easy_user_img);
        drawer_username_nogin = (TextView) findViewById(R.id.main_easy_user_name_nogin);;
        drawer_user_img_nogin = (ImageView) findViewById(R.id.main_easy_user_img_nogin);

        Calender_DBSet dbController = Room.databaseBuilder(Main_Easy.this.getApplicationContext(), Calender_DBSet.class, "CalenderDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        User_DBset userdbController = Room.databaseBuilder(Main_Easy.this.getApplicationContext(), User_DBset.class, "UserInfoDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        calender_dao = dbController.calender_dao();
        user_dao = userdbController.user_dao();

        List<UserDB> userdata = user_dao.getAllData();

        // -----------------------------------------------------------DB 데이터
        // 드로어 사용자 프로필 부분
        if(userdata.get(0).getId() == null){ // 로그인 상태가 아닐시
            drawer_welcome.setVisibility(View.GONE);
            drawer_username.setVisibility(View.GONE);
            drawer_user_address.setVisibility(View.GONE);
            drawer_user_img.setVisibility(View.GONE);
        } else {                                                // 로그인 했을때
            drawer_username_nogin.setVisibility(View.GONE); //  로그아웃 상태일때
            drawer_user_img_nogin.setVisibility(View.GONE); //  표시되던 사진과 이미지 없앰
            drawer_welcome.setVisibility(View.INVISIBLE); // 환영인사
            drawer_username.setVisibility(View.INVISIBLE); // 사용자 이름 표시
            drawer_user_address.setVisibility(View.INVISIBLE); // 사용자 이메일 표시
            drawer_user_img.setVisibility(View.INVISIBLE); // 사용자 사진 표시
        }


        //시작일, 종료일 데이터 저장
        calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = (calendar.get(Calendar.MONTH));
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        //한국어 설정 (ex: date picker)
        Locale.setDefault(Locale.KOREAN);

        // 디데이 다이얼로그
        d_day = (TextView) findViewById(R.id.main_easy_dday);
        d_day_text = (TextView) findViewById(R.id.main_easy_dday_text);

        // 달력 주간 달력 이동 버튼
        next = (ImageButton) findViewById(R.id.main_easy_next);
        previous = (ImageButton) findViewById(R.id.main_easy_previous);

        now = (TextView) findViewById(R.id.main_easy_now);
//        now.setText(getTime()); // 현재 시간
        MainTimerTask timerTask = new MainTimerTask();
        mTimer = new Timer();
        mTimer.schedule(timerTask, 500, 1000);

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
                    case R.id.main_easy_dday: // 디데이 날짜
                        new DatePickerDialog(Main_Easy.this, endDateSetListener, (currentYear), (currentMonth), currentDay).show();
                        break;
                    case R.id.main_easy_dday_text: // 디데이 내용
                            final EditText edit_dday_text = new EditText(Main_Easy.this);
                            AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(Main_Easy.this, R.style.AlertDialogTheme));
                            dialog.setTitle("D-day를 설정해주세요");
                            dialog.setView(edit_dday_text);
                            dialog.setView(edit_dday_text);
                            edit_dday_text.setText(d_day_text.getText()); // D-day 내용

                            // 완료 버튼
                            dialog.setPositiveButton("완료", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String getText = edit_dday_text.getText().toString();
                                    d_day_text.setText(getText);
                                }
                            });

                            // 취소 버튼
                            dialog.setNegativeButton("취소",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            dialog.show();
                            break;
                    case R.id.main_easy_drawer_btn: // 드로어 버튼
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout) ;
                        if (!drawer.isDrawerOpen(Gravity.LEFT)) {
                            drawer.openDrawer(Gravity.LEFT) ;
                        }
                        break;
                    case R.id.main_easy_user_setting:
                        List<UserDB> userdata = user_dao.getAllData();

                        // 유저 DB를 불러온 다음에 데이터를 읽어와서 null 이면 로그인 페이지로 아니면 마이프로필로 이동
                        if(userdata.get(0).getId() == null){
                            Intent intent = new Intent(Main_Easy.this, login.class);
                            startActivity(intent);

                        }else{
                            Intent userprofile = new Intent(Main_Easy.this, UserProfile.class);
                            startActivity(userprofile);
                            Log.v("login", "동작함");
                        }
                        break;
                    case R.id.main_easy_calendar:
                        Intent j = new Intent(Main_Easy.this, Calendar_Easy.class);
                        startActivity(j);
                        break;
                    case R.id.main_easy_setting:
                        Intent k = new Intent(Main_Easy.this, Setting_main_easy.class);
                        startActivity(k);
                        break;
                        }
                }
        };
        next.setOnClickListener(cl);
        previous.setOnClickListener(cl);
        d_day.setOnClickListener(cl);
        d_day_text.setOnClickListener(cl);
        drawer_btn.setOnClickListener(cl);
        drawer_user_setting.setOnClickListener(cl);
        drawer_calendar.setOnClickListener(cl);
//        drawer_game.setOnClickListener(cl);   // 게임 미구현
        drawer_setting.setOnClickListener(cl);

        calendar_recyclerView = (RecyclerView)findViewById(R.id.recycler_view_easy_calendar_day);
        calendar_recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)); // 가로 스크롤

        //싱글 어댑터
        singleAdapter = new Single_Adapter(this,days);
        main_easy_calendar_adapter = new Main_Easy_Calendar_Adapter();
        getDay();
        LinearLayoutManager layoutManager = (LinearLayoutManager) calendar_recyclerView.getLayoutManager();
        layoutManager.scrollToPositionWithOffset(Integer.parseInt(today)-1, 270);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_easy);
        list_itemAdapter_easy = new List_ItemAdapter_Easy();
        recyclerView.setAdapter(list_itemAdapter_easy);

        //화면 클리어
        list_itemAdapter_easy.removeAllItem();

        // -------------------------------------------------------- DB 데이터 넣는곳
        //샘플 데이터 생성
        for (int i = 0; i < listDB; i++) {
            List_Item list_item = new List_Item();
            list_item.setTime("14:00" + "-" + i); // 일정 시간
            list_item.setTitle("과제하기" + "-" + i); // 일정 제목
            //데이터 등록
            list_itemAdapter_easy.addItem(list_item);
        }

        //적용
        list_itemAdapter_easy.notifyDataSetChanged();


    }

    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
        }

        @Override
        public void onDrawerStateChanged(int newState) {
        }
    };

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

