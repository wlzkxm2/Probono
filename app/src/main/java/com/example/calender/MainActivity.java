package com.example.calender;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import com.example.calender.DataBase.Calender_DB;
import com.example.calender.DataBase.Calender_DBSet;
import com.example.calender.DataBase.Calender_Dao;
import com.example.calender.StaticUidCode.UidCode;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Calender_Dao calender_dao;

    private  static final String TAG = "MainActivity";

    Button ttsbtn, calbtn;

    Intent i;

    View.OnClickListener cl;

    final int PERMISSION = 1;	//permission 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CheckPermission();

        //<editor-fold desc="DB 기본 세팅 코드">

        // 캘린더 데이터베이스 정의
        Calender_DBSet dbController = Room.databaseBuilder(getApplicationContext(), Calender_DBSet.class, "CalenderDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        calender_dao = dbController.calender_dao();

        List<Calender_DB> calender_dbs = calender_dao.getAllData();
        //</editor-fold>

        ttsbtn = findViewById(R.id.tts_test);
        calbtn = findViewById(R.id.calendartestbtn);

        //<editor-fold desc="메인 엑티비티 버튼 스위치 문">
        cl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.tts_test:
                        Intent i = new Intent(getApplicationContext(), TTS_Test.class);
                        startActivity(i);
                        break;

                    case R.id.calendartestbtn:
                        Intent j = new Intent(getApplicationContext(), Navigation.class);
                        startActivity(j);
                        break;
                }
            }
        };
        //</editor-fold>

        ttsbtn.setOnClickListener(cl);
        calbtn.setOnClickListener(cl);

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
            calender_db.set_years(0);
            calender_db.set_month(0);
            calender_db.set_day(0);
            calender_db.set_day(0000);
            calender_db.set_firstData(true);
            calender_db.set_titles(null);
            calender_db.set_subtitle(null);
            calender_dao.insertAll(calender_db);

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

    void CheckPermission() {
        //안드로이드 버전이 6.0 이상
        if ( Build.VERSION.SDK_INT >= 23 ){
            Log.d("haan","버전 체크");
            //인터넷이나 녹음 권한이 없으면 권한 요청
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED ) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET,
                                Manifest.permission.RECORD_AUDIO},PERMISSION);
                                Log.d("haan","권한 체크");
            }
        }
    }
}