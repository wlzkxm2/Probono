//package com.example.calender.addschedule;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.speech.RecognitionListener;
//import android.speech.RecognizerIntent;
//import android.speech.SpeechRecognizer;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//
//import com.example.calender.R;
//import com.jakewharton.rxbinding4.widget.RxTextView;
//
//import java.util.ArrayList;
//
//import io.reactivex.rxjava3.core.Observable;
//import io.reactivex.rxjava3.disposables.Disposable;
//
//public class CustomDialog_STT extends Activity {
//
//    Intent intent;
//    SpeechRecognizer speechRecognizer;
//    boolean recording = false;  //현재 녹음중인지 여부
//
//    TextView tv_stt;
//    Button btn_save;
//
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        tv_stt = findViewById(R.id.stt_text);
//        btn_save = findViewById(R.id.btn_Save);
//
//
//        // RecognizerIntent 객체 생성
//        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getPackageName());
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");
//
//
//        if(recording == false) {
//            StartRecord();
//            Toast.makeText(getApplicationContext(), "음성인식을 시작합니다.", Toast.LENGTH_SHORT).show();
//        }else if(recording == true){
//            StopRecord();
//        }
//
//        btn_save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//    }
//
//    RecognitionListener listener = new RecognitionListener() {
//        @Override
//        public void onReadyForSpeech(Bundle bundle) {
//
//        }
//
//        @Override
//        public void onBeginningOfSpeech() {
//            //사용자가 말하기 시작
//        }
//
//        @Override
//        public void onRmsChanged(float v) {
//
//        }
//
//        @Override
//        public void onBufferReceived(byte[] bytes) {
//
//        }
//
//        @Override
//        public void onEndOfSpeech() {
//            //사용자가 말을 멈추면 호출
//            //인식 결과에 따라 onError나 onResults가 호출됨
//
//            StopRecord();
//        }
//
//        @Override
//        public void onError(int error) {    //토스트 메세지로 에러 출력
//            String message;
//            switch (error) {
//                case SpeechRecognizer.ERROR_AUDIO:
//                    message = "오디오 에러";
//                    break;
//                case SpeechRecognizer.ERROR_CLIENT:
//                    //message = "클라이언트 에러";
//                    //speechRecognizer.stopListening()을 호출하면 발생하는 에러
//                    return; //토스트 메세지 출력 X
//                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
//                    message = "퍼미션 없음";
//                    break;
//                case SpeechRecognizer.ERROR_NETWORK:
//                    message = "네트워크 에러";
//                    break;
//                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
//                    message = "네트웍 타임아웃";
//                    break;
//                case SpeechRecognizer.ERROR_NO_MATCH:
//                    //message = "찾을 수 없음";
//                    //녹음을 오래하거나 speechRecognizer.stopListening()을 호출하면 발생하는 에러
//                    //speechRecognizer를 다시 생성하여 녹음 재개
//                    if (recording)
//                        StartRecord();
//                    return; //토스트 메세지 출력 X
//                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
//                    message = "RECOGNIZER가 바쁨";
//                    break;
//                case SpeechRecognizer.ERROR_SERVER:
//                    message = "서버가 이상함";
//                    break;
//                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
//                    message = "말하는 시간초과";
//                    break;
//                default:
//                    message = "알 수 없는 오류임";
//                    break;
//            }
//            Toast.makeText(getApplicationContext(), "에러가 발생하였습니다. : " + message, Toast.LENGTH_SHORT).show();
//        }
//
//        //인식 결과가 준비되면 호출
//        @Override
//        public void onResults(Bundle bundle) {
//            ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);	//인식 결과를 담은 ArrayList
//
//            //인식 결과
//            String newText="";
//            for (int i = 0; i < matches.size() ; i++) {
//                newText += matches.get(i);
//            }
//
//            tv_stt.setText(newText + " ");	//기존의 text에 인식 결과를 이어붙임
//            //speechRecognizer.startListening(intent);    //녹음버튼을 누를 때까지 계속 녹음해야 하므로 녹음 재개
//            StopRecord();
//
//
//        }
//
//        @Override
//        public void onPartialResults(Bundle bundle) {
//
//        }
//
//        @Override
//        public void onEvent(int i, Bundle bundle) {
//
//        }
//    };
//
//
//    void StartRecord() {
//        recording = true;
//        speechRecognizer=SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
//        speechRecognizer.setRecognitionListener(listener);
//        speechRecognizer.startListening(intent);
//
//    }
//
//    //녹음 중지
//    void StopRecord() {
//        recording = false;
//        speechRecognizer.stopListening();   //녹음 중지
//        Toast.makeText(getApplicationContext(), "음성 기록을 중지합니다.", Toast.LENGTH_SHORT).show();
//    }
//}
////--------------------------------------------stt 메모장------------------------------------------
////        int y = 0;
////                int m = 0;
////                int w = 0;
////                int d = 0;
////                int h = 0;
////
////                int checkYearWord = -1;
////                int checkMonthWord = -1;
////                int checkWeekWord = -1;
////                int checkDayWord = -1;
////
////                int saveYearData;
////                int saveMonthData;
////                int saveDateData;
////                int saveTimeData;
////
////
////                if (data.indexOf("년 뒤") > -1) {
////                checkYearWord = 1;
////                y = data.indexOf("년 뒤");
////                saveYearData = Integer.parseInt(yearFormat.format(currentTime)) + Integer.parseInt(data.substring(0,y));
////                Log.d("HSH", "년 뒤 변수 y값 =" + y);
////                Log.d("HSH", "년 뒤 저장값 =" + saveYearData);
////
////                } else if(data.indexOf("년") > -1) {
////                checkYearWord = 0;
////                y = data.indexOf("년");
////                saveYearData = Integer.parseInt(data.substring(0,y));
//////            Log.d("HSH", "년 뒤 =" + data.indexOf("년 뒤"));
////                }else{ // 현재 년도 반환
////                saveYearData = Integer.parseInt(yearFormat.format(currentTime));
////                }
////
////                if(data.indexOf("개월" ) > -1) {
////                m = data.indexOf("개월");
////                checkMonthWord = 1;
////                if(checkYearWord == 0){
////                saveMonthData = Integer.parseInt(data.substring(y + 1, m));
////                }else if(checkYearWord == 1){
////                saveMonthData = Integer.parseInt(data.substring(y + 2, m));
////                }else{
////                saveMonthData = Integer.parseInt(data.substring(0, m));
////                }
////
////                }else if(data.indexOf("월") > -1){
////                m = data.indexOf("월");
////                checkMonthWord = 0;
////                if(checkYearWord == 1){
////                saveMonthData = Integer.parseInt(data.substring(y + 1, m));
////                }else if(checkYearWord == 2){
////                saveMonthData = Integer.parseInt(data.substring(y + 2, m));
////                }else{
////                saveMonthData = Integer.parseInt(data.substring(0, m));
////                }
////
////                }else if(data.indexOf("달") > -1){
////                m = data.indexOf("달");
////                checkMonthWord = 0;
////                if(checkYearWord == 1){
////                saveMonthData = Integer.parseInt(data.substring(y + 1, m));
////                }else if(checkYearWord == 2){
////                saveMonthData = Integer.parseInt(data.substring(y + 2, m));
////                }else{
////                saveMonthData = Integer.parseInt(data.substring(0, m));
////                }
////
////                }else{//현재 달 반환
////                saveMonthData = Integer.parseInt(monthFormat.format(currentTime));
////                }
////
////                if(data.indexOf("주 후") > -1){
////                w = data.indexOf("주 후");
////                checkWeekWord = 1;
////                if(checkMonthWord == 0){
////                saveDateData = Integer.parseInt(data.substring(m+1 , w)) * 7;
////                saveDateData = Integer.parseInt(dateFormat.format(currentTime)) + saveDateData;
////                }else if(checkMonthWord == 1){
////                saveDateData = Integer.parseInt(data.substring(m+2 , w)) * 7;
////                saveDateData = Integer.parseInt(dateFormat.format(currentTime)) + saveDateData;
////                }else{
////                saveDateData = Integer.parseInt(dateFormat.format(currentTime));
////                }
////
////                }else if(data.indexOf("다음 주") > -1){
////                w = data.indexOf("다음 주");
////                checkWeekWord = 0;
////
////
////                }
////                if(data.indexOf("일 뒤") > -1){
////                d = data.indexOf("일 뒤");
////                if(checkWeekWord == 0){
////                saveDateData = Integer.parseInt(dateFormat.format(currentTime)) + Integer.parseInt(data.substring(w+1,d));
////                }else if (checkWeekWord == 1){
////                saveDateData = Integer.parseInt(dateFormat.format(currentTime)) + Integer.parseInt(data.substring(w+2,d));
////                }else{
////                saveDateData = Integer.parseInt(dateFormat.format(currentTime)) + Integer.parseInt(data.substring(0,d));
////                }
////                }else if(data.indexOf("요일") > -1){
////
////                }else if(data.indexOf("일") > -1) {
////
////                }else{//오늘 날짜 반환
////
////                }
////
////                if(data.indexOf("시")> -1){
////                h = data.indexOf("시");
////                saveTimeData = Integer.parseInt(dateFormat.format(currentTime)) + Integer.parseInt(data.substring(d+1,h));
////                }
////
////
////
////                }
