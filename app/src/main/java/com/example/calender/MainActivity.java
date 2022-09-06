package com.example.calender;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.example.calender.DataBase.Calender_DB;
import com.example.calender.DataBase.Calender_DBSet;
import com.example.calender.DataBase.Calender_Dao;
import com.example.calender.DataBase.UserDB;
import com.example.calender.DataBase.User_DBset;
import com.example.calender.DataBase.User_Dao;
import com.example.calender.Permission.Permission;
import com.example.calender.Main_Easy.Main_Easy;
import com.example.calender.StaticUidCode.UidCode;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Calender_Dao calender_dao;
    User_Dao user_dao;
    String themeColor;
    private  static final String TAG = "MainActivity";

    Button ttsbtn, calbtn;

    Intent i;

    View.OnClickListener cl;

    ImageView giflogo;

    final int PERMISSION = 1;	//permission 변수

    private Permission permission;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        permissionCheck();
        themeColor= ThemeUtil.modLoad(getApplicationContext());
        ThemeUtil.applyTheme(themeColor);
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //<editor-fold desc="DB 기본 세팅 코드">

        // 캘린더 데이터베이스 정의
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

        giflogo = (ImageView) findViewById(R.id.gifimage);
        Glide.with(this).load(R.raw.logo_gif).into(giflogo);

//        List<Calender_DB> calender_dbs = calender_dao.getAllData();
//        List<UserDB> userdb = user_dao.getAllData();        // 유저 데이터베이스


        //</editor-fold>

//        ttsbtn = findViewById(R.id.tts_test);
//        calbtn = findViewById(R.id.calendartestbtn);
//        easybtn = findViewById(R.id.calendar_easy_testbtn);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, Navigation.class);
                startActivity(intent);
            }
        }, 2500);

        //<editor-fold desc="메인 엑티비티 버튼 스위치 문">
//        cl = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (v.getId()){
//                    case R.id.tts_test:
//                        Intent i = new Intent(getApplicationContext(), TTS_Test.class);
//                        startActivity(i);
//                        break;
//
//                    case R.id.calendartestbtn:
//                        Intent j = new Intent(getApplicationContext(), Navigation.class);
//                        startActivity(j);
//                        break;
//
//                    case R.id.calendar_easy_testbtn:
//                        Intent k = new Intent(getApplicationContext(), Main_Easy.class);
//                        startActivity(k);
//                        break;
//                }
//            }
//        };
        //</editor-fold>

//        ttsbtn.setOnClickListener(cl);
//        calbtn.setOnClickListener(cl);

        //<editor-fold desc="앱 최초 실행시 유저 기본 코드를 제공한 후 데이터베이스에 삽입">
        SharedPreferences pref = getSharedPreferences("isFirstStart", Activity.MODE_PRIVATE);
        boolean firstStart = pref.getBoolean("isFirst", false);

        if (firstStart == false){
            // 앱 최초 실행시
            Log.d(TAG, "First Start");

            int uid = (int) (Math.random() * 100000);   // 무작위 난수 대입
            Log.d(TAG, "userId : " + uid);
            ((UidCode) getApplication()).setUserCode(uid);
            Log.d(TAG, "Get User ID : " + ((UidCode) getApplication()).getUserCode());

            // 데이터베이스에 최초 데이터 출력
            Calender_DB calender_db = new Calender_DB();
            calender_db.setUid(uid);
            calender_db.setStart_years(0);
            calender_db.setStart_month(0);
            calender_db.setStart_day(0);
            calender_db.setStart_day(0000);
            calender_db.setEnd_years(0);
            calender_db.setEnd_month(0);
            calender_db.setEnd_day(0);
            calender_db.setEnd_day(0000);
            calender_db.set_firstData(true);
            calender_db.set_titles(null);
            calender_db.set_subtitle(null);
            calender_db.set_mainActTitle("제목을 추가해보세요");
            calender_db.set_mainActTime(0);
            calender_db.set_mainActDTitle("목표를 추가해보세요");
            calender_dao.insertAll(calender_db);

            // 로그인한 사용자의 정보를 받기위한 데이터값
            UserDB userDB = new UserDB(null, null, null, null, 0, "00000", null, null, null);
            user_dao.insertAll(userDB);

            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isFirst", true);
            editor.commit();
        }else{
            int uid = 0;
            List<Calender_DB> getUid = calender_dao.getAllData();
            for (int j = 0; j < getUid.size(); j++) {
                if (getUid.get(j)._firstData == true){
                    uid = getUid.get(j).getUid();
                    break;
                }
            }
            ((UidCode) getApplication()).setUserCode(uid);
            Log.d(TAG, "Not FirstStart : " + ((UidCode) getApplication()).getUserCode());
        }
        //</editor-fold>

    }

//    void permissionCheck() {
//        // PermissionSupport.java 클래스 객체 생성
//        permission = new Permission(this, this);
//
//        // 권한 체크 후 리턴이 false로 들어오면
//        if (!permission.checkPermission()){
//            //권한 요청
//            permission.requestPermission();
//        }
//    }
//    // Request Permission에 대한 결과 값 받아와
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        //여기서도 리턴이 false로 들어온다면 (사용자가 권한 허용 거부)
//        if (!permission.permissionResult(requestCode, permissions, grantResults)) {
//            // 다시 permission 요청
//            permission.requestPermission();
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }


}