package com.example.calender.setting;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.room.Room;

import com.example.calender.DataBase.Calender_DB;
import com.example.calender.DataBase.Calender_DBSet;
import com.example.calender.DataBase.Calender_Dao;
import com.example.calender.DataBase.UserDB;
import com.example.calender.DataBase.User_DBset;
import com.example.calender.DataBase.User_Dao;
import com.example.calender.R;

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

    private void showNoti(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");

        builder.setSmallIcon(R.mipmap.ic_launcher);

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

        List<Calender_DB> calender_like_data = calender_dao.loadAllDataByYears(
                Integer.parseInt(YearData),
                Integer.parseInt(monthData),
                Integer.parseInt(dayData)
        );

        Calender_DB calender_db = new Calender_DB();
        calender_db.setStart_time(nowTime);

        for(int i = 1; i < calender_like_data.size(); i++){
            if(calender_like_data.get(i).getStart_time() >= calender_db.getStart_time()){
                calender_db.set_titles(calender_like_data.get(i).get_titles());
                calender_db.set_subtitle(calender_like_data.get(i).get_subtitle());
                calender_db.setStart_time(calender_like_data.get(i).getStart_time());
                calender_db.setEnd_time(calender_like_data.get(i).getEnd_time());

                Log.v("notification", calender_like_data.get(i).get_titles());
                Log.v("notification", "calender_db.set_titles" + calender_db.get_titles());

            }
        }



        Log.v("notification", "calender_db.set_titles" + calender_db.get_titles());

        String startTime = String.format("%04d", calender_db.getStart_time());
        String valueStartTime = startTime.substring(0,2) + " : " + startTime.substring(2, startTime.length());
        String EndTime = String.format("%04d", calender_db.getEnd_time());
        String valueEndTime = EndTime.substring(0,2) + " : " + EndTime.substring(2, EndTime.length());

//        builder.setContentTitle("오늘의 일정");
        if(calender_db.get_titles() == "null"){
            builder.setContentText(valueStartTime + " ~ " + valueEndTime + "\n" + calender_db.get_titles() + "\n" + calender_db.get_subtitle());
        }else{
            builder.setContentText("오늘 일정이 없습니다");
        }

        builder.setContentTitle("오늘의 일정");
//        builder.setContentText(calender_db.get_titles().toString());

        builder.setColor(Color.RED);
        // 사용자가 탭을 클릭하면 자동 제거
        builder.setAutoCancel(true);

        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }

        // id값은
        // 정의해야하는 각 알림의 고유한 int값
        notificationManager.notify(1, builder.build());
    }

    private void deleteNoti(){
        NotificationManagerCompat.from(this).cancel(1);
    }
}