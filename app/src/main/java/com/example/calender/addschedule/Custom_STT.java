package com.example.calender.addschedule;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.example.calender.DataBase.Calender_DB;
import com.example.calender.DataBase.Calender_DBSet;
import com.example.calender.DataBase.Calender_Dao;
import com.example.calender.Main_Basic.Main_Basic_Frag;
import com.example.calender.R;
import com.example.calender.StaticUidCode.UidCode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.transform.Result;

public class Custom_STT extends Dialog {

    Calender_Dao calender_dao;

    Context context;
    Button btn_save, btn_restart;
    Switch sw_auto;
    TextView tv_stt;
    View.OnClickListener cl;
    Intent intent;
    SpeechRecognizer speechRecognizer;
    boolean recording = false;  //현재 녹음중인지
    String newText="";
    String STT_Result;
    LinearLayout sttBackground;

    public Custom_STT(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_stt);

        sttBackground = findViewById(R.id.sttBackground);
        tv_stt = findViewById(R.id.stt_text);
        btn_save = findViewById(R.id.btn_Save);
        btn_restart = findViewById(R.id.btn_Restart);
        sw_auto = findViewById(R.id.sw_auto);
        sw_auto.setChecked(false);

        // RecognizerIntent 객체 생성
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,context.getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");

        Calender_DBSet dbController = Room.databaseBuilder(context, Calender_DBSet.class, "CalenderDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        calender_dao = dbController.calender_dao();

        Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(recording == false) {
                        StartRecord();
                        Toast.makeText(context, "음성인식을 시작합니다.", Toast.LENGTH_SHORT).show();
                    }else if(recording == true){
                        StopRecord();
                    }
                }
            },1500);


        cl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.sw_auto:
                        if(sw_auto.isChecked()){
                            btn_save.setVisibility(View.INVISIBLE);
                            btn_restart.setVisibility(View.INVISIBLE);
                        }else{
                            btn_save.setVisibility(View.VISIBLE);
                            btn_restart.setVisibility(View.VISIBLE);
                        }
                        break;
                    case R.id.btn_Restart:
                        StopRecord();
                        tv_stt.setText(null);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(recording == false) {
                                    StartRecord();
                                    Toast.makeText(context, "음성인식을 시작합니다.", Toast.LENGTH_SHORT).show();
                                }else if(recording == true){
                                    StopRecord();
                                }
                            }
                        },1000);
                        break;
                    case R.id.btn_Save:
                        //TODO newText에 있는 값 DB로 보내기
                        dismiss();
                        break;
                }
            }
        };
        btn_save.setOnClickListener(cl);
        btn_restart.setOnClickListener(cl);
        sw_auto.setOnClickListener(cl);

    }



    RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {

        }

        @Override
        public void onBeginningOfSpeech() {
            //사용자가 말하기 시작
        }

        @Override
        public void onRmsChanged(float v) {

        }

        @Override
        public void onBufferReceived(byte[] bytes) {

        }

        @Override
        public void onEndOfSpeech() {
            //사용자가 말을 멈추면 호출
            //인식 결과에 따라 onError나 onResults가 호출됨
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(sw_auto.isChecked()){
                        StopRecord();
                        //TODO newText에 있는 값 DB로 보내기
                        dismiss();
                    }else{
                        StopRecord();
                    }
                }
            },1500);

        }

        @Override
        public void onError(int error) {    //토스트 메세지로 에러 출력
            String message;
            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "오디오 에러";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "클라이언트 에러";
                    //speechRecognizer.stopListening()을 호출하면 발생하는 에러
                    return; //토스트 메세지 출력 X
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "퍼미션 없음";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "네트워크 에러";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "네트웍 타임아웃";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    //message = "찾을 수 없음";
                    //녹음을 오래하거나 speechRecognizer.stopListening()을 호출하면 발생하는 에러
                    //speechRecognizer를 다시 생성하여 녹음 재개
                    if (recording)
                        StartRecord();
                    return; //토스트 메세지 출력 X
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RECOGNIZER가 바쁨";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "서버가 이상함";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "말하는 시간초과";
                    break;
                default:
                    message = "알 수 없는 오류";
                    break;
            }
            Toast.makeText(context, "에러가 발생하였습니다. : " + message, Toast.LENGTH_SHORT).show();
        }

        //인식 결과가 준비되면 호출
        @Override
        public void onResults(Bundle bundle) {
            ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);	//인식 결과를 담은 ArrayList

            //인식 결과

            for (int i = 0; i < matches.size() ; i++) {
                newText += matches.get(i);
            }

            tv_stt.setText(newText + " ");	//기존의 text에 인식 결과를 이어붙임
            Log.d("HSH",""+ newText);
            //speechRecognizer.startListening(intent);    //녹음버튼을 누를 때까지 계속 녹음해야 하므로 녹음 재개

//            Main_Basic_Frag ma = new Main_Basic_Frag();
            StopRecord();
//            ma.inputData(newText);

            Calendar cal = Calendar.getInstance();
            int sysYear = cal.get(Calendar.YEAR);
            int sysMonth = cal.get(Calendar.MONTH) +1;
            int sysDate = cal.get(Calendar.DAY_OF_MONTH);
            int sysHour = cal.get(Calendar.HOUR);
            int sysMinute = cal.get(Calendar.MINUTE);

            Log.d("HSH", "sysYear 값 : " + sysYear);

            //문장에서 특정 단어 위치값 저장 변수
            int y = 0;
            int m = 0;
            int w = 0;
            int d = 0;
            int h = 0;
            int mi = 0;
            int from = 0;
            int until = 0;
            int add = 0;

            // -1 = 없음, 0 = 1글자, 1 = 2글자
            int checkYearWord = -1;
            int checkMonthWord = -1;
            int checkWeekWord = -1;
            int checkDateWord = -1;
            int checkHourWord = -1;
            int checkMinuteWord = -1;
            int checkUntilWord = -1;

            //DB 넘길 변수들
            int saveYearData = sysYear;
            int saveMonthData = sysMonth;
            int saveDateData = sysDate;
            int saveHourData = sysHour;
            int saveMinuteData = sysMinute;
            String saveTitleData = "";
            int saveEndYearData = saveYearData;
            int saveEndMonthData = saveMonthData;
            int saveEndDateData = saveDateData;
            int saveEndHourData = saveHourData;
            int saveEndMinuteData = saveMinuteData;

            if (newText.indexOf("년 뒤") > -1) {
                checkYearWord = 1;
                y = newText.indexOf("년 뒤");
                saveYearData = sysYear + Integer.parseInt(newText.substring(0, y));
                Log.d("HSH", "년 뒤 변수 y값 =" + y);
                Log.d("HSH", "년 뒤 DB저장값 =" + saveYearData);

            }else if(newText.indexOf("년") > -1){
                checkYearWord = 0;
                y = newText.indexOf("년");
                saveYearData = Integer.parseInt(newText.substring(0, y));
                Log.d("HSH", "년, 변수 y값 =" + y);
                Log.d("HSH", "년, DB저장값 =" + saveYearData);
            }

            if (newText.indexOf("달 뒤") > -1){
                checkMonthWord = 1;
                m = newText.indexOf("달 뒤");
                if(checkYearWord == 1){
                    saveMonthData = sysMonth + Integer.parseInt(newText.substring(y+3, m));
                }else if(checkYearWord == 0){
                    saveMonthData = sysMonth + Integer.parseInt(newText.substring(y+2, m));
                }else{
                    saveMonthData = sysMonth + Integer.parseInt(newText.substring(y, m));
                }
            }else if(newText.indexOf("월") > -1){
                checkMonthWord = 0;
                m = newText.indexOf("월");

                if(checkYearWord == 1){
                    saveMonthData = Integer.parseInt(newText.substring(y+3, m));
                }else if(checkYearWord == 0){
                    saveMonthData = Integer.parseInt(newText.substring(y+2, m));
                }else{
                    saveMonthData = Integer.parseInt(newText.substring(y, m));
                }
                Log.d("HSH", "월, 변수 m값 =" + m);
                Log.d("HSH", "월, DB저장값 =" + saveMonthData);
            }

            if(newText.indexOf("일 뒤") > -1){
                checkDateWord = 1;
                d = newText.indexOf("일 뒤");

                if(checkMonthWord == 1){
                    saveDateData = sysDate + Integer.parseInt(newText.substring(m+3, d));
                }else if(checkMonthWord == 0){
                    saveDateData = sysDate +Integer.parseInt(newText.substring(m+2, d));
                }else{
                    saveDateData = sysDate + Integer.parseInt(newText.substring(m, d));
                }
                Log.d("HSH", "일 뒤, 변수 d값 =" + d);
                Log.d("HSH", "일 뒤, DB저장값 =" + saveDateData);
            }else if(newText.indexOf("요일") > -1){

            }else if(newText.indexOf("일") > -1){
                checkDateWord = 0;
                d = newText.indexOf("일");

                if(checkMonthWord == 1){
                    saveDateData = Integer.parseInt(newText.substring(m+3, d));
                }else if(checkMonthWord == 0){
                    saveDateData = Integer.parseInt(newText.substring(m+2, d));
                }else{
                    saveDateData = Integer.parseInt(newText.substring(m, d));
                }
                Log.d("HSH", "일, 변수 d값 =" + d);
                Log.d("HSH", "일, DB저장값 =" + saveDateData);
            }

            if(newText.indexOf("시간 뒤") > -1){
                h = newText.indexOf("시간 뒤");
                saveHourData = sysHour + Integer.parseInt(newText.substring(d, h));
                Log.d("HSH", "시간 뒤, 변수 h값 =" + h);
                Log.d("HSH", "시간 뒤, DB저장값 =" + saveHourData);
            }else if(newText.indexOf("시간") > -1){
                h = newText.indexOf("시간");
                checkHourWord = 1;
                saveHourData = sysHour + Integer.parseInt(newText.substring(d, h));
                Log.d("HSH", "시간, 변수 h값 =" + h);
                Log.d("HSH", "시간, DB저장값 =" + saveHourData);
            }else if(newText.indexOf("시") > -1){
                checkHourWord = 0;
                h = newText.indexOf("시");

                if(checkDateWord == 1){
                    saveHourData = Integer.parseInt(newText.substring(d+3, h));
                }else if(checkDateWord == 0){
                    saveHourData = Integer.parseInt(newText.substring(d+2, h));
                }else{
                    saveHourData = Integer.parseInt(newText.substring(d, h));
                }
                Log.d("HSH", "시, 변수 h값 =" + h);
                Log.d("HSH", "시, DB저장값 =" + saveHourData);
            }

            if(newText.indexOf("분 뒤") > -1){
                mi = newText.indexOf("분 뒤");
                checkMinuteWord = 1;

                if(checkHourWord == 1){
                    saveMinuteData = sysMinute + Integer.parseInt(newText.substring(h+3, mi));
                }else if(checkHourWord == 0){
                    saveMinuteData = sysMinute + Integer.parseInt(newText.substring(h+2, mi));
                }else{
                    saveMinuteData = sysMinute + Integer.parseInt(newText.substring(h, mi));
                }

                while(saveMinuteData >= 60){
                    saveMinuteData = saveMinuteData - 60;
                    saveHourData = saveHourData + 1;
                }
                Log.d("HSH", "분 뒤, saveMinuteData 값 =" + saveMinuteData);


                Log.d("HSH", "분 뒤, 변수 mi값 =" + mi);
                Log.d("HSH", "분 뒤, DB저장값 =" + saveHourData +": "+ saveMinuteData);
            }else if(newText.indexOf("분") > -1){
                mi = newText.indexOf("분");
                checkMinuteWord = 0;

                if(checkHourWord == 1){
                    saveMinuteData = Integer.parseInt(newText.substring(h+3, mi));
                }else if(checkHourWord == 0){
                    saveMinuteData = Integer.parseInt(newText.substring(h+2, mi));
                }else{
                    saveMinuteData = Integer.parseInt(newText.substring(h, mi));
                }

                Log.d("HSH", "분, 변수 mi값 =" + mi);
                Log.d("HSH", "분, DB저장값 =" + saveMinuteData);
            }

            if(newText.indexOf("부터") > -1){
                from = newText.indexOf("부터");

                if (newText.indexOf("년 뒤") > -1) {
                    checkYearWord = 1;
                    y = newText.indexOf("년 뒤");
                    saveEndYearData = sysYear + Integer.parseInt(newText.substring(from + 3, y));

                }else if(newText.indexOf("년") > -1){
                    checkYearWord = 0;
                    y = newText.indexOf("년");
                    saveEndYearData = Integer.parseInt(newText.substring(from + 3, y));

                }

                if (newText.indexOf("달 뒤") > -1){
                    checkMonthWord = 1;
                    m = newText.indexOf("달 뒤");
                    if(checkYearWord == 1){
                        saveEndMonthData = sysMonth + Integer.parseInt(newText.substring(y+3, m));
                    }else if(checkYearWord == 0){
                        saveEndMonthData = sysMonth + Integer.parseInt(newText.substring(y+2, m));
                    }else{
                        saveEndMonthData = sysMonth + Integer.parseInt(newText.substring(from + 3, m));
                    }
                }else if(newText.indexOf("월") > -1){
                    checkMonthWord = 0;
                    m = newText.indexOf("월");

                    if(checkYearWord == 1){
                        saveEndMonthData = Integer.parseInt(newText.substring(y+3, m));
                    }else if(checkYearWord == 0){
                        saveEndMonthData = Integer.parseInt(newText.substring(y+2, m));
                    }else{
                        saveEndMonthData = Integer.parseInt(newText.substring(from + 3, m));
                    }
                }

                if(newText.indexOf("일 뒤") > -1){
                    checkDateWord = 1;
                    d = newText.indexOf("일 뒤");

                    if(checkMonthWord == 1){
                        saveEndDateData = sysDate + Integer.parseInt(newText.substring(m+3, d));
                    }else if(checkMonthWord == 0){
                        saveEndDateData = sysDate +Integer.parseInt(newText.substring(m+2, d));
                    }else{
                        saveEndDateData = sysDate + Integer.parseInt(newText.substring(from+3, d));
                    }

                }else if(newText.indexOf("요일") > -1){

                }else if(newText.indexOf("일") > -1){
                    checkDateWord = 0;
                    d = newText.indexOf("일");

                    if(checkMonthWord == 1){
                        saveEndDateData = Integer.parseInt(newText.substring(m+3, d));
                    }else if(checkMonthWord == 0){
                        saveEndDateData = Integer.parseInt(newText.substring(m+2, d));
                    }else{
                        saveEndDateData = Integer.parseInt(newText.substring(from+3, d));
                    }
                }

                if(newText.indexOf("시간 뒤") > -1){
                    h = newText.indexOf("시간 뒤");
                    saveEndHourData = sysHour + Integer.parseInt(newText.substring(from+3, h));
                }else if(newText.indexOf("시간") > -1){
                    h = newText.indexOf("시간");
                    checkHourWord = 1;
                    saveEndHourData = sysHour + Integer.parseInt(newText.substring(from+3, h));
                }else if(newText.indexOf("시") > -1){
                    checkHourWord = 0;
                    h = newText.indexOf("시");

                    if(checkDateWord == 1){
                        saveEndHourData = Integer.parseInt(newText.substring(d+3, h));
                    }else if(checkDateWord == 0){
                        saveEndHourData = Integer.parseInt(newText.substring(d+2, h));
                    }else{
                        saveEndHourData = Integer.parseInt(newText.substring(from+3, h));
                    }

                }

                if(newText.indexOf("분 뒤") > -1){
                    mi = newText.indexOf("분 뒤");
                    checkMinuteWord = 1;

                    if(checkHourWord == 1){
                        saveEndMinuteData = sysMinute + Integer.parseInt(newText.substring(h+3, mi));
                    }else if(checkHourWord == 0){
                        saveEndMinuteData = sysMinute + Integer.parseInt(newText.substring(h+2, mi));
                    }else{
                        saveEndMinuteData = sysMinute + Integer.parseInt(newText.substring(from+3, mi));
                    }

                    while(saveEndMinuteData >= 60){
                        saveEndMinuteData = saveEndMinuteData - 60;
                        saveEndHourData = saveEndHourData + 1;
                    }

                }else if(newText.indexOf("분") > -1){
                    mi = newText.indexOf("분");
                    checkMinuteWord = 0;

                    if(checkHourWord == 1){
                        saveEndMinuteData = Integer.parseInt(newText.substring(h+3, mi));
                    }else if(checkHourWord == 0){
                        saveEndMinuteData = Integer.parseInt(newText.substring(h+2, mi));
                    }else{
                        saveEndMinuteData = Integer.parseInt(newText.substring(from+3, mi));
                    }

                }

            }
            else if(newText.indexOf("부터") == -1){
                saveEndYearData = saveYearData;
                saveEndMonthData = saveMonthData;
                saveEndDateData = saveDateData;
                saveEndHourData = saveHourData;
                saveEndMinuteData = saveMinuteData;
            }

            if(newText.indexOf("까지") > -1) {
                checkUntilWord = 1;
                until = newText.indexOf("까지");
            }else if(newText.indexOf("에") > -1){
                checkUntilWord = 0;
                until = newText.indexOf("에");
            }

            if(newText.indexOf("일정 추가해 줘") > -1){
                add = newText.indexOf("추가해 줘");
                if(checkUntilWord == 1){
                    saveTitleData = newText.substring(until+3, add);
                }else if(checkUntilWord == 0){
                    saveTitleData = newText.substring(until+2, add);

                }
            }else if(newText.indexOf("추가해 줘") > -1){
                add = newText.indexOf("추가해 줘");
                if(checkUntilWord == 1){
                    saveTitleData = newText.substring(until+3, add);
                }else if(checkUntilWord == 0){
                    saveTitleData = newText.substring(until+2, add);

                }
            }


            Log.d("HSH", "최종 DB 저장값 = " + saveYearData +"년 "+ saveMonthData +"월 "+ saveDateData+"일 "
                    + saveHourData+"시 "+ saveMinuteData + "분 " + "내용 : " + saveTitleData);

            Log.d("HSH", "최종 DB End 저장값 = " + saveEndYearData +"년 "+ saveEndMonthData +"월 "+ saveEndDateData+"일 "
                    + saveEndHourData+"시 "+ saveEndMinuteData + "분 ");

            String saveStartTime = Integer.toString(saveHourData) + Integer.toString(saveMinuteData);
            String saveEndTime = Integer.toString(saveEndHourData) + Integer.toString(saveEndMinuteData);

            Log.v("stt", "saveStartTime" + saveStartTime + "\n" + "saveEndTime" + saveEndTime);

            int saveStartTimeint = Integer.parseInt(saveStartTime);
            int saveEndTimeint = Integer.parseInt(saveEndTime);


            Calender_DB inputCalData = new Calender_DB();
            // 일정 시작일
            inputCalData.setStart_years(saveYearData);
            inputCalData.setStart_month(saveMonthData);
            inputCalData.setStart_day(saveDateData);
            inputCalData.setStart_time(saveStartTimeint);

            // 일정 마지막일
            inputCalData.setEnd_years(saveEndYearData);
            inputCalData.setEnd_month(saveEndMonthData);
            inputCalData.setEnd_day(saveEndDateData);
            inputCalData.setEnd_time(saveEndTimeint);

            // 일정 내용 추가
            inputCalData.set_titles(saveTitleData);
            inputCalData.set_subtitle("");

            // 입력한 일정을 DB에 추가
            calender_dao.insertAll(inputCalData);
        }

        @Override
        public void onPartialResults(Bundle bundle) {

        }

        @Override
        public void onEvent(int i, Bundle bundle) {

        }
    };


    void StartRecord() {
        recording = true;
        sttBackground.setBackgroundColor(Color.parseColor("#57E06B"));
        newText = "";
        speechRecognizer=SpeechRecognizer.createSpeechRecognizer(context);
        speechRecognizer.setRecognitionListener(listener);
        speechRecognizer.startListening(intent);

    }

    //녹음 중지
    void StopRecord() {
        recording = false;
        sttBackground.setBackgroundColor(Color.parseColor("#FF5232"));
        speechRecognizer.stopListening();   //녹음 중지
        Toast.makeText(context, "음성 기록을 중지합니다.", Toast.LENGTH_SHORT).show();
    }
}
