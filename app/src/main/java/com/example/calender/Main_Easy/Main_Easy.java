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
import android.widget.Toast;

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
import com.example.calender.DataBase.Calender_DB;
import com.example.calender.DataBase.Calender_DBSet;
import com.example.calender.DataBase.Calender_Dao;
import com.example.calender.DataBase.UserDB;
import com.example.calender.DataBase.User_DBset;
import com.example.calender.DataBase.User_Dao;
import com.example.calender.Main_Basic.List_Item;
import com.example.calender.R;
import com.example.calender.StaticUidCode.UidCode;
import com.example.calender.UserProfile;
import com.example.calender.addschedule.AddSchedule;
import com.example.calender.addschedule.Custom_STT;
import com.example.calender.login;
import com.example.calender.setting.Setting_main_easy;
import com.example.calender.setting.Setting_notification;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

    //????????? ?????? ?????? ??????
    private FloatingActionButton floating_voice;

    // ?????? ????????? ????????? ?????? ?????? ??????
    private ImageButton nolist_add;
    private TextView nolist_add_text;

    //????????? ??????
    private TextView drawer_user_setting, drawer_calendar, drawer_game, drawer_setting,
            drawer_welcome, drawer_username, drawer_user_address, drawer_username_nogin;
    private ImageView drawer_user_img, drawer_user_img_nogin;
    Calender_Dao calender_dao;
    User_Dao user_dao;

    // ????????? ??????
    int dateEndY, dateEndM, dateEndD;
    int ddayValue = 0;

    // ?????? ????????? ?????? ?????? ??????
    Calendar calendar;
    int currentYear, currentMonth, currentDay;

    // Millisecond ????????? ??????(24 ??????)
    private final int ONE_DAY = 24 * 60 * 60 * 1000;

    //?????? ?????????
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

    // ?????? ?????? ??????????????? ????????????

    private Handler mHandler = new Handler();

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {

            Date rightNow = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat(
                    "k??? m???");
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

//    @Override
//    protected void onResume() {
//        MainTimerTask timerTask = new MainTimerTask();
//        mTimer.schedule(timerTask, 500, 3000);
//        super.onResume();
//    }

    // DatePickerDialog?????????, ????????? ??????, ????????? ????????? ?????? ????????? ?????? ????????? ????????? ?????????
    private DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//            edit_endDateBtn.setText(year + "??? " + (monthOfYear + 1) + "??? " + dayOfMonth + "???");
            ddayValue = ddayResult_int(dateEndY, dateEndM, dateEndD);
            d_day.setText(getDday(year, monthOfYear, dayOfMonth));
        }
    };

    // ????????? ????????? year, mMonthOfYear : ????????? ????????? MonthOfYear, mDayOfMonth : ????????? ????????? DayOfMonth
    private String getDday(int mYear, int mMonthOfYear, int mDayOfMonth) {
        // D-day ??????
        List<Calender_DB> mainactDB = calender_dao.loadMainData(1);
        long result = 0;


        final Calendar ddayCalendar = Calendar.getInstance();
        ddayCalendar.set(mYear, mMonthOfYear, mDayOfMonth);


        // D-day ??? ????????? ?????? millisecond ?????? ???????????? d-day ?????? today ??? ?????? ?????????.
        final long dday = ddayCalendar.getTimeInMillis() / ONE_DAY;
        final long today = Calendar.getInstance().getTimeInMillis() / ONE_DAY;

        calender_dao.MainActDayupdate(1, dday);
        // ?????? ????????? ??????
        Log.v("MainDays", "dday : " + dday + "\n" + "today : " + today);
        result = dday - today;

        // db??? ????????? ?????? ???????????? ??????
        calender_dao.MainActDayupdate(1, dday);



        // ?????? ??? d-day ??? ?????? ??????
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

    // ????????? ??? ??????
    public int onCalculatorDate(int dateEndY, int dateEndM, int dateEndD) {
        try {
            Calendar today = Calendar.getInstance(); //?????? ?????? ??????
            Calendar dday = Calendar.getInstance();

            //?????????, ????????? ????????? ??????
            Calendar calendar = Calendar.getInstance();
            int cyear = calendar.get(Calendar.YEAR);
            int cmonth = (calendar.get(Calendar.MONTH) + 1);
            int cday = calendar.get(Calendar.DAY_OF_MONTH);

            today.set(cyear, cmonth, cday);
            dday.set(dateEndY, dateEndM, dateEndD);// D-day??? ????????? ???????????????.

            long day = dday.getTimeInMillis() / 86400000;
            // ?????? ?????? ?????? ?????? ????????? ??????
            //( 1?????? ???(86400000 = 24?????? * 60??? * 60??? * 1000(1??????) ) )

            long tday = today.getTimeInMillis() / 86400000;
            long count = tday - day; // ?????? ???????????? dday ????????? ????????? ?????????.
            return (int) count; // ????????? ?????? + ?????????????????????.
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    // ????????? ??? ????????? ????????? ??????
    public int ddayResult_int(int dateEndY, int dateEndM, int dateEndD) {
        int result = 0;
        result = onCalculatorDate(dateEndY, dateEndM, dateEndD);
        return result;
    }


    // onResume ?????? ?????? ?????? ??????????????? ????????????

    private String getMonth() { //?????? ??? ????????????
        long now = System.currentTimeMillis(); // ?????? ????????? now ????????? ??????
        Date date = new Date(now); // ?????? ????????? date ???????????? ??????
        SimpleDateFormat dateFormat = new SimpleDateFormat("M");
        String getTime = dateFormat.format(date);
        return getTime;
    }

    private String getYear() { //?????? ?????? 4?????? ????????????
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        String getYear = dateFormat.format(date);

        return getYear;
    }

    private String getToday() { //?????? ?????? ????????????
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("d");
        String getToday = dateFormat.format(date);

        return getToday;
    }

    private void getDay() {
        days = new ArrayList<>();
        // ?????? ?????? ????????? ??????
        // 1,3,5,7,8,10,12???
        if (month.getText().toString().equals(jan) || month.getText().toString().equals(mar)
                || month.getText().toString().equals(may) || month.getText().toString().equals(jul)
                || month.getText().toString().equals(aus) || month.getText().toString().equals(oct)
                || month.getText().toString().equals(dec)) {   //31????????? ??????
            for (int i = 1; i <= 31; i++) {
                Main_Easy_Calendar_Day day = new Main_Easy_Calendar_Day();
                day.setDay(""+i);
                days.add(day);
                singleAdapter.SetMain_Easy_Calendar_Day(days);
//                str = i + "";
//                main_easy_calendar_adapter.setArrayData(str);
            }
        } else if (month.getText().toString().equals(feb)) // 2?????? ?????? ?????? ??????
        {
            if (Integer.parseInt(year) % 4 == 0) // ?????? ????????? 4??? ????????? 0??? ?????? ??????
            {   // 29????????? ??????
                for (int i = 1; i <= 29; i++) {
                    Main_Easy_Calendar_Day day = new Main_Easy_Calendar_Day();
                    day.setDay(""+i);
                    days.add(day);
                    singleAdapter.SetMain_Easy_Calendar_Day(days);
//                    str = i + "";
//                    main_easy_calendar_adapter.setArrayData(str);
                }
            } else // ????????? ?????????
            {   // 28????????? ??????
                for (int i = 1; i <= 28; i++) {
                    Main_Easy_Calendar_Day day = new Main_Easy_Calendar_Day();
                    day.setDay(""+i);
                    days.add(day);
                    singleAdapter.SetMain_Easy_Calendar_Day(days);
//                    str = i + "";
//                    main_easy_calendar_adapter.setArrayData(str);
                }
            }
        } else // ????????? 4,6,9,11???
        {   // 30????????? ??????
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

        //???????????????
        floating_voice = (FloatingActionButton) findViewById(R.id.main_easy_floating_voice);

        // ?????? ????????? ?????? ?????? ??????
        nolist_add = (ImageButton)findViewById(R.id.main_easy_nolist_add);
        nolist_add_text = (TextView) findViewById(R.id.main_easy_nolist_add_text);

        // ?????????
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

        // -----------------------------------------------------------DB ?????????
        // ????????? ????????? ????????? ??????
        if(userdata.get(0).getId() == null){ // ????????? ????????? ?????????
            drawer_welcome.setVisibility(View.GONE);
            drawer_username.setVisibility(View.GONE);
            drawer_user_address.setVisibility(View.GONE);
            drawer_user_img.setVisibility(View.GONE);
        } else {                                                // ????????? ?????????
            drawer_username_nogin.setVisibility(View.GONE); //  ???????????? ????????????
            drawer_user_img_nogin.setVisibility(View.GONE); //  ???????????? ????????? ????????? ??????
            drawer_welcome.setVisibility(View.INVISIBLE); // ????????????
            drawer_username.setVisibility(View.INVISIBLE); // ????????? ?????? ??????
            drawer_user_address.setVisibility(View.INVISIBLE); // ????????? ????????? ??????
            drawer_user_img.setVisibility(View.INVISIBLE); // ????????? ?????? ??????
        }


        //?????????, ????????? ????????? ??????
        calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = (calendar.get(Calendar.MONTH));
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        //????????? ?????? (ex: date picker)
        Locale.setDefault(Locale.KOREAN);

        // ????????? ???????????????
        d_day = (TextView) findViewById(R.id.main_easy_dday);
        d_day_text = (TextView) findViewById(R.id.main_easy_dday_text);

        // ?????? ?????? ?????? ?????? ??????
        next = (ImageButton) findViewById(R.id.main_easy_next);
        previous = (ImageButton) findViewById(R.id.main_easy_previous);

        now = (TextView) findViewById(R.id.main_easy_now);
//        now.setText(getTime()); // ?????? ??????
        MainTimerTask timerTask = new MainTimerTask();
        mTimer = new Timer();
        mTimer.schedule(timerTask, 500, 1000);

        month = (TextView) findViewById(R.id.main_easy_month);
        month.setText(getMonth()); // ?????? ??? month??? ??????. month.getText().toString()?????? ?????? ??? ????????? ???????????? ??? ??? ??????

        cl = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.main_easy_next:
                        a = Integer.parseInt(month.getText().toString());
                        b = a + 1;
                        if (b > 12) // 12????????? ?????? 1?????? ????????????
                        {
                            b = 1; // ????????? ?????? 1?????? ?????????
                            a = Integer.parseInt(year) + 1; // ?????? ????????? int??? ????????? +1
                            year = String.valueOf(a); // ?????? ???????????? ????????????
                        }
                        c = String.valueOf(b);
                        month.setText(c);
                        main_easy_calendar_adapter.removeArrayData();
                        getDay();
                        break;
                    case R.id.main_easy_previous:
                        a = Integer.parseInt(month.getText().toString());
                        b = a - 1;
                        if (b < 1) // 1????????? ?????? 12?????? ????????????
                        {
                            b = 12; // ????????? ?????? 1?????? ?????????
                            a = Integer.parseInt(year) - 1; // ?????? ????????? int??? ????????? -1
                            year = String.valueOf(a); // ?????? ???????????? ????????????=
                        }
                        c = String.valueOf(b);
                        month.setText(c);
                        main_easy_calendar_adapter.removeArrayData();
                        getDay();
                        break;
                    case R.id.main_easy_dday: // ????????? ??????
                        new DatePickerDialog(Main_Easy.this, endDateSetListener, (currentYear), (currentMonth), currentDay).show();
                        break;
                    case R.id.main_easy_dday_text: // ????????? ??????
                        final EditText edit_dday_text = new EditText(Main_Easy.this);
                        AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(Main_Easy.this, R.style.AlertDialogTheme));
                        dialog.setTitle("D-day??? ??????????????????");
                        dialog.setView(edit_dday_text);
                        dialog.setView(edit_dday_text);
                        edit_dday_text.setText(d_day_text.getText()); // D-day ??????

                        // ?????? ??????
                        dialog.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String getText = edit_dday_text.getText().toString();
                                d_day_text.setText(getText);
                            }
                        });

                        // ?????? ??????
                        dialog.setNegativeButton("??????",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        dialog.show();
                        break;
                    case R.id.main_easy_drawer_btn: // ????????? ??????
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout) ;
                        if (!drawer.isDrawerOpen(Gravity.LEFT)) {
                            drawer.openDrawer(Gravity.LEFT) ;
                        }
                        break;
                    case R.id.main_easy_user_setting:
                        List<UserDB> userdata = user_dao.getAllData();

                        // ?????? DB??? ????????? ????????? ???????????? ???????????? null ?????? ????????? ???????????? ????????? ?????????????????? ??????
                        if(userdata.get(0).getId() == null){
                            Intent intent = new Intent(Main_Easy.this, login.class);
                            startActivity(intent);

                        }else{
                            Intent userprofile = new Intent(Main_Easy.this, UserProfile.class);
                            startActivity(userprofile);
                            Log.v("login", "?????????");
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
                    case R.id.main_easy_game:
                        Toast.makeText(Main_Easy.this,"????????? ???????????????.",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.main_easy_floating_voice:
                        Custom_STT custom_stt = new Custom_STT(Main_Easy.this);
                        int inputday = ((UidCode) Main_Easy.this.getApplication()).getStatic_day();
                        custom_stt.show();
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
        drawer_game.setOnClickListener(cl);   // ?????? ?????????
        drawer_setting.setOnClickListener(cl);
        floating_voice.setOnClickListener(cl);

        calendar_recyclerView = (RecyclerView)findViewById(R.id.recycler_view_easy_calendar_day);
        calendar_recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)); // ?????? ?????????

        //?????? ?????????
        singleAdapter = new Single_Adapter(this,days);
        main_easy_calendar_adapter = new Main_Easy_Calendar_Adapter();
        getDay();
        LinearLayoutManager layoutManager = (LinearLayoutManager) calendar_recyclerView.getLayoutManager();
        layoutManager.scrollToPositionWithOffset(Integer.parseInt(today)-1, 270);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_easy);
        list_itemAdapter_easy = new List_ItemAdapter_Easy();
        recyclerView.setAdapter(list_itemAdapter_easy);

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        String YearData = yearFormat.format(currentTime);
        String monthData = monthFormat.format(currentTime);
        String dayData = dayFormat.format(currentTime);

        List<Calender_DB> calender_like_data = calender_dao.loadAllDataByYears(
                Integer.parseInt(YearData),
                Integer.parseInt(monthData),
                Integer.parseInt(dayData)
        );

        //?????? ?????????
        list_itemAdapter_easy.removeAllItem();

        // -------------------------------------------------------- DB ????????? ?????????
        if (calender_like_data.isEmpty()) {
            nolist_add.setVisibility(View.VISIBLE);
            nolist_add_text.setVisibility(View.VISIBLE);
            nolist_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent n = new Intent(Main_Easy.this, AddSchedule.class);
                    startActivity(n);
                }
            });

        } else {
            nolist_add.setVisibility(View.GONE);
            nolist_add_text.setVisibility(View.GONE);
            for (int i = 0; i < calender_like_data.size(); i++) {
                List_Item calList = new List_Item();
                String startTime = String.format("%04d", calender_like_data.get(i).getStart_time());
                String valueStartTime = startTime.substring(0,2) + " : " + startTime.substring(2, startTime.length());
                String EndTime = String.format("%04d", calender_like_data.get(i).getEnd_time());
                String valueEndTime = EndTime.substring(0,2) + " : " + EndTime.substring(2, EndTime.length());

                calList.setTime(valueStartTime + "~ \n" + valueEndTime);
                calList.setTitle(calender_like_data.get(i).get_titles());
                calList.setText(calender_like_data.get(i).get_subtitle());

                list_itemAdapter_easy.addItem(calList);
            }
        }





        //?????? ????????? ??????
//        for (int i = 0; i < listDB; i++) {
//            List_Item list_item = new List_Item();
//            list_item.setTime("14:00" + "-" + i); // ?????? ??????
//            list_item.setTitle("????????????" + "-" + i); // ?????? ??????
//            //????????? ??????
//            list_itemAdapter_easy.addItem(list_item);
//        }

        //??????
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
            case android.R.id.home:{ // ?????? ?????? ?????? ????????? ???
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private long backpressedTime = 0;

    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() > backpressedTime + 2000) {
            backpressedTime = System.currentTimeMillis();
            Toast.makeText(this, "\'??????\' ????????? ?????? ??? ???????????? ???????????????.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() <= backpressedTime + 2000) {

            moveTaskToBack(true); // ???????????? ?????????????????? ??????
            finishAndRemoveTask(); // ???????????? ?????? + ????????? ??????????????? ?????????

            System.exit(0);




        }

    }

}

