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

import com.example.calender.R;

import java.util.ArrayList;

public class Custom_STT extends Dialog {

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
            handle_message();
            StopRecord();


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

    void handle_message() {
//        String extractInteger = newText.replaceAll("[^0~9]","");
        String extractInteger = "";
        for(int i =0; i< newText.length(); i++){
            char ch = newText.charAt(i);
            if(48 <= ch && ch <=57){
                extractInteger +=ch;
            }
        }
        Log.d("HSH","" + extractInteger);
    }
}
