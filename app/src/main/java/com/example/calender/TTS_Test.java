//package com.example.calender;
//
//import android.Manifest;
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.speech.RecognitionListener;
//import android.speech.RecognizerIntent;
//import android.speech.SpeechRecognizer;
//import android.speech.tts.TextToSpeech;
//import android.util.Log;
//import android.view.View;
//import android.view.Window;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import com.jakewharton.rxbinding4.widget.RxTextView;
//
//import java.util.ArrayList;
//import java.util.Locale;
//
//import io.reactivex.rxjava3.core.Observable;
//import io.reactivex.rxjava3.disposables.Disposable;
//
//public class TTS_Test extends Activity implements TextToSpeech.OnInitListener {
//
//    Intent intent;
//    SpeechRecognizer speechRecognizer;
//    final int PERMISSION = 1;	//permission 변수
//    boolean recording = false;  //현재 녹음중인지 여부
//    private Disposable disposable;
//    private TextToSpeech tts;
//    Button btn_TTS, btn_STT, btn_Dialog;
//    EditText text;
//    TextView tv_Result;
//    View.OnClickListener click;
//    Dialog dialog;
//    String getPakage;
//
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.tts_test);
//
//        CheckPermission();  //녹음 퍼미션 체크
//
//        tts = new TextToSpeech(this, this);
//        btn_TTS = findViewById(R.id.btn_TTS);
//        btn_STT = findViewById(R.id.btn_STT);
//        btn_Dialog = findViewById(R.id.btn_dialog);
//        text = findViewById(R.id.editText);
//        tv_Result = findViewById(R.id.tv_Result);
//
//        getPakage = getPackageName();
//
//
//        observeTextWatcher(text);
//
//        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
////        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
//        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPakage);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");   //한국어
//
//
//
//        click = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (v.getId()) {
//                    //녹음 버튼
//                    case R.id.btn_STT:
//                        tv_Result.setText(null);
//                        if (!recording) {   //녹음 시작
//                            StartRecord();
//                            Toast.makeText(getApplicationContext(), "음성인식을 시작합니다.", Toast.LENGTH_SHORT).show();
//                        } else {  //이미 녹음 중이면 녹음 중지
//                            StopRecord();
//                        }
//                        break;
//                    case R.id.btn_TTS:
//                        speakOut();
//                        break;
//                    case R.id.btn_dialog:
////                        showDialog();
//                        Intent intent = new Intent(getApplicationContext(), AddSchedule.class);
//                        startActivity(intent);
//                        break;
//                    default:
//                        break;
//                }
//            }
//        };
//        btn_TTS.setOnClickListener(click);
//        btn_STT.setOnClickListener(click);
//        btn_Dialog.setOnClickListener(click);
//    }
//
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
//                    message = "찾을 수 없음";
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
////            String originText =  tv_Result.getText().toString();//기존 text
//
//            //인식 결과
//            String newText="";
//            for (int i = 0; i < matches.size() ; i++) {
//                newText += matches.get(i);
//            }
//
////            tv_Result.setText(originText + newText + " ");	//기존의 text에 인식 결과를 이어붙임
//            tv_Result.setText(newText + " ");	//기존의 text에 인식 결과를 이어붙임
//            //speechRecognizer.startListening(intent);    //녹음버튼을 누를 때까지 계속 녹음해야 하므로 녹음 재개
//            StopRecord();
//            speakOut();
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
//    public void observeTextWatcher(TextView tv){
//        Observable ob = RxTextView.textChanges(tv);
//        disposable = ob.subscribe(text -> tv_Result.setText(" " + text.toString()));
//    }
//
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private void speakOut(){
//        CharSequence text = tv_Result.getText();
//        tts.setPitch((float)1.5); // 음성 톤 높이 지정
//        tts.setSpeechRate((float)1.5); // 음성 속도 지정
//
//        // 첫 번째 매개변수: 음성 출력을 할 텍스트
//        // 두 번째 매개변수: 1. TextToSpeech.QUEUE_FLUSH - 진행중인 음성 출력을 끊고 이번 TTS의 음성 출력
//        //                 2. TextToSpeech.QUEUE_ADD - 진행중인 음성 출력이 끝난 후에 이번 TTS의 음성 출력
//        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "id1");
//    }
//
//
//    @Override
//    public void onDestroy() {
//        if(tts!=null){ // 사용한 TTS객체 제거
//            tts.stop();
//            tts.shutdown();
//        }
//        disposable.dispose();
//        super.onDestroy();
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    public void onInit(int status) { // OnInitListener를 통해서 TTS 초기화
//        if(status == TextToSpeech.SUCCESS){
//            int result = tts.setLanguage(Locale.KOREA); // TTS언어 한국어로 설정
//
//            if(result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA){
//                Log.e("TTS", "This Language is not supported");
//            }else{
//                btn_TTS.setEnabled(true);
//                speakOut();// onInit에 음성출력할 텍스트를 넣어줌
//            }
//        }else{
//            Log.e("TTS", "Initialization Failed!");
//        }
//    }
//
////    public void showDialog() {
////       dialog.show();
////       Button btn_Save = dialog.findViewById(R.id.btn_Save);
////
////
////       btn_Save.setOnClickListener(new View.OnClickListener() {
////           @Override
////           public void onClick(View v) {
////               //기능추가
////            dialog.dismiss();
////           }
////
////       });
////      Button btn_Back = dialog.findViewById(R.id.btn_Back);
////       btn_Back.setOnClickListener(new View.OnClickListener() {
////           @Override
////           public void onClick(View v) {
////               dialog.dismiss();
////           }
////       });
////
////    }
//
//    void CheckPermission() {
//        //안드로이드 버전이 6.0 이상
//        if ( Build.VERSION.SDK_INT >= 23 ){
//            //인터넷이나 녹음 권한이 없으면 권한 요청
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED
//                    || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED ) {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.INTERNET,
//                                Manifest.permission.RECORD_AUDIO},PERMISSION);
//            }
//        }
//    }
//
//    //녹음 시작
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
//
//
//
