package com.example.calender.setting;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.room.Room;

import com.example.calender.AlramManager.AlertReceiver;
import com.example.calender.DataBase.Calender_DB;
import com.example.calender.DataBase.Calender_DBSet;
import com.example.calender.DataBase.Calender_Dao;
import com.example.calender.DataBase.User_DBset;
import com.example.calender.DataBase.User_Dao;
import com.example.calender.Main_Basic.List_Item;
import com.example.calender.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Setting_notification extends AppCompatActivity {
    Button back;
    View.OnClickListener cl;
    Switch notisw;

    Calender_Dao calender_dao;
    User_Dao user_dao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_notification);
        //알림 설정 레이아웃을 표시

        Calender_DBSet dbController = Room.databaseBuilder(getApplicationContext(), Calender_DBSet.class, "CalenderDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        User_DBset userdbController = Room.databaseBuilder(getApplicationContext(), User_DBset.class, "UserInfoDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        calender_dao = dbController.calender_dao();
        user_dao = userdbController.user_dao();

        back = (Button) findViewById(R.id.back_noti);
        notisw=(Switch) findViewById(R.id.noti_switch) ;
        //(notisw).setChecked(true);
        (notisw).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //True이면 할 일
                    showNoti();
                }else{
                    //False이면 할 일
                    deleteNoti();
                }
            }
        });


        cl = new View.OnClickListener() {  //뒤로가기 버튼 누르면 화면 종료되도록 설정
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.back_noti:
                        finish();
                        break;
                }
            }
        };
        back.setOnClickListener(cl);

    }

    private void updateTimeText(Calendar c){
        String timeText = "Alarm set for : ";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());

        Toast.makeText(this, timeText, Toast.LENGTH_SHORT).show();
    }

    private void startAlarm(Calendar c){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        if(c.before((Calendar.getInstance()))){
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        //alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 1*60*1000 ,  pendingIntent);

    }

    private void cancelAlarm(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, "알람 취소", Toast.LENGTH_SHORT).show();
    }

    private void showNoti(){
        // 오늘의 시간을 받아옴
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        String YearData = yearFormat.format(currentTime);
        String monthData = monthFormat.format(currentTime);
        String dayData = dayFormat.format(currentTime);

        SimpleDateFormat hour = new SimpleDateFormat("HH", Locale.getDefault());
        SimpleDateFormat minute = new SimpleDateFormat("mm", Locale.getDefault());
        String hourstr = hour.format(currentTime);
        String minutestr = minute.format(currentTime);
        String Time = hourstr+minutestr;
        int nowTime = Integer.parseInt(Time);


        // 오늘 날짜 알람
        List<Calender_DB> calender_like_data = calender_dao.loadAllDataByYears(
                Integer.parseInt(YearData),
                Integer.parseInt(monthData),
                Integer.parseInt(dayData)

        );

        Log.v("showNoti", nowTime + "");

        for(int i = 0; i < calender_like_data.size(); i++){
            // 일정 시간을 출력
            int calStartTime = calender_like_data.get(i).getStart_time();
            String calStartTimestr = Integer.toString(calStartTime);
            // 시간
            String SHour = calStartTimestr.substring(0,2);
            // 분
            String Sminute = calStartTimestr.substring(2, calStartTimestr.length());

            // 캘린더 형으로 변환
            Calendar dbTime = Calendar.getInstance();
            dbTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(SHour));
            dbTime.set(Calendar.MINUTE, Integer.parseInt(Sminute));
            dbTime.set(Calendar.SECOND, 0);

            Calendar realTime = Calendar.getInstance();
            realTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourstr));
            realTime.set(Calendar.MINUTE, Integer.parseInt(minutestr) + 29);
            realTime.set(Calendar.SECOND, 0);


            // c.getTime()      현재 시간을 출력

            // 현재 시간보다 저장된 시간이 뒤일때
            if(realTime.before(dbTime)){
                Log.v("showNoti", "dbTime" + dbTime.getTime() + "");
                Log.v("showNoti", "realTime" + realTime.getTime() + "");
                Log.v("showNoti", "calender_like_data.get(i).get_titles()" + calender_like_data.get(i).get_titles() + "");

                Calendar setAlram = Calendar.getInstance();
                setAlram.set(Calendar.HOUR_OF_DAY, Integer.parseInt(SHour));
                setAlram.set(Calendar.MINUTE, Integer.parseInt(Sminute) - 30);
                setAlram.set(Calendar.SECOND, 0);

                updateTimeText(setAlram);
                startAlarm(setAlram);
                break;
            }
        }

    }

    private void deleteNoti(){
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.deleteNotificationChannel("1");
        }
//        NotificationManagerCompat.from(this).cancel(1);
    }
}