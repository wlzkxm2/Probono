package com.example.calender.Main_Basic;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.text.Layout;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.calender.DataBase.Calender_DB;
import com.example.calender.DataBase.Calender_DBSet;
import com.example.calender.DataBase.Calender_Dao;
import com.example.calender.DataBase.User_DBset;
import com.example.calender.DataBase.User_Dao;
import com.example.calender.MainActivity;
import com.example.calender.Main_Easy.List_ItemAdapter_Easy;
import com.example.calender.Main_Easy.Main_Easy;
import com.example.calender.Navigation;
import com.example.calender.R;
import com.example.calender.StaticUidCode.UidCode;
import com.example.calender.addschedule.Custom_STT;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static android.speech.tts.TextToSpeech.ERROR;

public class Main_Basic_Frag extends Fragment implements View.OnClickListener, TextToSpeech.OnInitListener {

    String refresh = "0";

    int startYears = 0,startMonths = 0,startDays = 0, endYears = 0,endMonths = 0,endDays = 0,
            startMinute = 0,endHour = 0,endMinute = 0, startHour = 0, startDate = 0;
    int startLoaclHour = 0, startLoaclMinute = 0, endLoaclHour = 0, endLoaclMinute = 0;

    int radioState = 0;// 0파,1빨,2초

    // TTS 버튼
    private ImageButton main_basic_TTS_btn;

    boolean threadstart = false;

    Calender_Dao calender_dao;
    User_Dao user_dao;
    long dbinputTime = 0;
    String dbinputTitle = "";
    String dbinputDtitle = "";

    // 일정이 없는날 등록하는 버튼
    ImageButton nolist_add;
    TextView nolist_add_text;

    // 디데이 변수
    int dateEndY, dateEndM, dateEndD;
    int ddayValue = 0;

    // 현재 날짜를 알기 위해 사용
    Calendar calendar;
    int currentYear, currentMonth, currentDay;

    // Millisecond 형태의 하루(24 시간)
    private final int ONE_DAY = 24 * 60 * 60 * 1000;

    // TTS
    private TextToSpeech tts;
    private ImageButton btn_Tts;

    Context context;
    Main_Basic mainbasic;       // 프래그럼트 매니저가 있음
    private Timer mTimer;
    //플로팅 버튼
    private Context mContext;
    private FloatingActionButton floating_main, floating_edit, floating_voice;
    private Animation floating_open, floating_close;
    private boolean isFabOpen = false;

    ImageView add_schedule_dot;
    ImageButton add_schedule, edit_title;
    TextView now, add_schedule_txt, maintitle_txt, d_day, d_day_text;
    RecyclerView recyclerView;
    List_ItemAdapter list_itemAdapter;
    int listDB = 10;

    int lastVisibleItemPositions;
    int itemTotalCounts;

    Animation fade_in, fade_out;

    View.OnClickListener cl;

    View itemLayout;

    // 현재 시간 실시간으로 구해오기

    private Handler mHandler = new Handler();

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {

            Date rightNow = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat(
                    "M월 d일");
            String dateString = formatter.format(rightNow);
            now.setText(dateString);

        }
    };

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS){
            int ttsResult = tts.setLanguage(Locale.KOREA); // TTS언어 한국어로 설정

            if(ttsResult == TextToSpeech.LANG_NOT_SUPPORTED || ttsResult == TextToSpeech.LANG_MISSING_DATA){
                Log.e("TTS", "This Language is not supported");
            }else{

                speakOut();// onInit에 음성출력할 텍스트를 넣어줌
            }
        }else{
            Log.e("TTS", "Initialization Failed!");
        }
    }

    private void speakOut() {
        //CharSequence text = 여기다가_읽어줄_값_넣어주세요.getText();
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
        String text = "";
        for(int i = 0; i < calender_like_data.size(); i++){
            text += calender_like_data.get(i).get_titles().toString();
            Log.v("tts", calender_like_data.get(i).get_titles());

            if(calender_like_data.size() - 1 == i)
                text += ",일정이 있습니다,";
            else
                text += "일정과, ";
        }
        tts.setPitch((float)1.3); // 음성 톤 높이 지정
        tts.setSpeechRate((float)1.3); // 음성 속도 지정
        // 첫 번째 매개변수: 음성 출력을 할 텍스트
        // 두 번째 매개변수: 1. TextToSpeech.QUEUE_FLUSH - 진행중인 음성 출력을 끊고 이번 TTS의 음성 출력
        //                 2. TextToSpeech.QUEUE_ADD - 진행중인 음성 출력이 끝난 후에 이번 TTS의 음성 출력
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }


    class MainTimerTask extends TimerTask {
        public void run() {
            mHandler.post(mUpdateTimeTask);
        }
    }

    @Override
    public void onDestroy() {
        if(tts!=null){ // 사용한 TTS객체 제거
            tts.stop();
            tts.shutdown();
        }
        mTimer.cancel();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        mTimer.cancel();
        super.onPause();
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
        List<Calender_DB> mainactDB = calender_dao.loadMainData(1);
        long result = 0;


        final Calendar ddayCalendar = Calendar.getInstance();
        ddayCalendar.set(mYear, mMonthOfYear, mDayOfMonth);


        // D-day 를 구하기 위해 millisecond 으로 환산하여 d-day 에서 today 의 차를 구한다.
        final long dday = ddayCalendar.getTimeInMillis() / ONE_DAY;
        final long today = Calendar.getInstance().getTimeInMillis() / ONE_DAY;

        calender_dao.MainActDayupdate(1, dday);
        // 현재 시간을 출력
        //Log.v("MainDays", "dday : " + dday + "\n" + "today : " + today);
        result = dday - today;

        // db에 지정한 시간 데이터를 저장
        calender_dao.MainActDayupdate(1, dday);



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
    public int onCalculatorDate(int dateEndY, int dateEndM, int dateEndD) {
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


    public static Main_Basic_Frag newInstance() {
        Main_Basic_Frag Main_Basic = new Main_Basic_Frag();
        return Main_Basic;
    }

    private void refresh() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    private void edit(){
        Date today = new Date();
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH시mm분");

        Date currentTime = Calendar.getInstance().getTime();
//                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
//                SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
//                SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
//                SimpleDateFormat timeFormat = new SimpleDateFormat("HH시mm분");
        String YearData = yearFormat.format(currentTime);
        String monthData = monthFormat.format(currentTime);
        String dayData = dateFormat.format(currentTime);
//                LocalTime now = LocalTime.now();

        startYears = ((UidCode) getActivity().getApplication()).getStatic_year();
        startMonths = ((UidCode) getActivity().getApplication()).getStatic_month();
        startDays = ((UidCode) getActivity().getApplication()).getStatic_day();
        endYears = ((UidCode) getActivity().getApplication()).getStatic_year();
        endMonths = ((UidCode) getActivity().getApplication()).getStatic_month();
        endDays = ((UidCode) getActivity().getApplication()).getStatic_day();
//                startLoaclHour = now.getHour();
//                startLoaclMinute = now.getMinute();
//                endLoaclHour = now.getHour();
//                endLoaclMinute = now.getMinute();

                List<Calender_DB> calender_like_data = calender_dao.loadAllDataByYears(
                        Integer.parseInt(YearData),
                        Integer.parseInt(monthData),
                        Integer.parseInt(dayData)
                );

        AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogTheme));
        LayoutInflater inflater= getLayoutInflater();
        View view = inflater.inflate(R.layout.add_schedule_basic, null);
        dialog.setView(view);

        final EditText schedule_title = (EditText) view.findViewById(R.id.add_schedule_basic_title_ed);
        final TextView schedule_start_day = (TextView) view.findViewById(R.id.add_schedule_basic_start_day_ed);
        final TextView schedule_end_day = (TextView) view.findViewById(R.id.add_schedule_basic_end_day_ed);
        final TextView schedule_start_time = (TextView) view.findViewById(R.id.add_schedule_basic_start_time_ed);
        final TextView schedule_end_time = (TextView) view.findViewById(R.id.add_schedule_basic_end_time_ed);
        final EditText schedule_text = (EditText) view.findViewById(R.id.add_schedule_basic_text_ed);
        final CheckBox allDayCheck = (CheckBox) view.findViewById(R.id.add_schedule_basic_allday);
        final TextView dot = (TextView) view.findViewById(R.id.add_schedule_basic_dot);
        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);

        if(startYears < 2000 || endYears < 2000){
            startYears = Integer.parseInt(yearFormat.format(today));
            endYears = Integer.parseInt(yearFormat.format(today));
        }
        if(startMonths < 1 || endMonths < 1){
            startMonths = Integer.parseInt(monthFormat.format(today));
            endMonths = Integer.parseInt(monthFormat.format(today));
        }
        if(startDays < 1 || endDays < 1){
            startDays = Integer.parseInt(dateFormat.format(today));
            endDays = Integer.parseInt(dateFormat.format(today));
        }

//                String startTime = String.format("%04d", calender_like_data.get(pos).getStart_time());
//                String valueStartTime = startTime.substring(0,2) + " : " + startTime.substring(2, startTime.length());
//                String EndTime = String.format("%04d", calender_like_data.get(pos).getEnd_time());
//                String valueEndTime = EndTime.substring(0,2) + " : " + EndTime.substring(2, EndTime.length());

//                schedule_title.setText(calender_like_data.get(pos).get_titles());
        //번역
        schedule_start_day.setText(R.string.start_date);
        schedule_end_day.setText(R.string.end_date);
        schedule_start_time.setText(R.string.start_time);
        schedule_end_time.setText(R.string.end_time);
//                schedule_text.setText(calender_like_data.get(pos).get_subtitle());

//                final int startScheduleHour=Integer.parseInt(startTime.substring(0,2)), startScheduleMinute=Integer.parseInt(startTime.substring(2, startTime.length()));
//                final int endScheduleHour=Integer.parseInt(EndTime.substring(0,2)), endScheduleMinute=Integer.parseInt(EndTime.substring(2, startTime.length()));


        GregorianCalendar gregorianCalendar = new GregorianCalendar();

        // 일정시간 하루종일 체크
        allDayCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allDayCheck.isChecked()){
                    schedule_start_time.setVisibility(View.GONE);
                    schedule_end_time.setVisibility(View.GONE);
                    dot.setVisibility(View.GONE);
                }else{
                    schedule_start_time.setVisibility(View.VISIBLE);
                    schedule_end_time.setVisibility(View.VISIBLE);
                    dot.setVisibility(View.VISIBLE);
                }
            }
        });


        // 일정 시작 날짜
        schedule_start_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int s_year, int s_month, int s_dayOfMonth) {
                        startYears = s_year;
                        startMonths = s_month+1;
                        startDays = s_dayOfMonth;
                        schedule_start_day.setText(s_year + "년 " + (s_month + 1)+ "월 " + s_dayOfMonth + "일 ");
                    }
                },startYears,startMonths-1,startDays);

//                       // 시작날짜 설정(확인) 버튼
//                        datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE,"설정", new DialogInterface.OnClickListener(){
//                            @Override public void onClick(DialogInterface dialogInterface, int i) {
//
//                            }
//                        });
//
//                        // 시작날짜 취소 버튼
//                        datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE,"취소", new DialogInterface.OnClickListener(){
//                            @Override public void onClick(DialogInterface dialogInterface, int i) {
//
//                            }
//                        });
                datePickerDialog.show();
            }
        });

        // 일정 종료 날짜
        schedule_end_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int e_year, int e_month, int e_dayOfMonth) {
                        endYears = e_year;
                        endMonths = e_month+1;
                        endDays = e_dayOfMonth;
                        schedule_end_day.setText(e_year + "년 " + (e_month + 1)+ "월 " + e_dayOfMonth + "일 ");
                    }
                },endYears,endMonths-1,endDays);

//                        // 종료날짜 설정(확인) 버튼
//                        datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE,"설정", new DialogInterface.OnClickListener(){
//                            @Override public void onClick(DialogInterface dialogInterface, int i) {
//
//                            }
//                        });
//
//                        // 종료날짜 취소 버튼
//                        datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE,"취소", new DialogInterface.OnClickListener(){
//                            @Override public void onClick(DialogInterface dialogInterface, int i) {
//
//                            }
//                        });
                datePickerDialog.show();
            }
        });


        //일정 시작 시간
        schedule_start_time.setOnClickListener(new View.OnClickListener() { // 일정 시작 시간 타임피커
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog
                        (getActivity(), android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int shour, int sminute) {
                                startHour = shour;
                                startMinute = sminute;
                                String startScheduleHour = String.format("%02d",shour);
                                String startScheduleMinute = String.format("%02d",sminute);
                                schedule_start_time.setText(startScheduleHour + " : " + startScheduleMinute);
                            }
                        },startHour, startMinute, true);

                // 타임피커 설정(확인) 버튼
                timePickerDialog.setButton(TimePickerDialog.BUTTON_POSITIVE, "설정", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                // 타임피커 취소 버튼
                timePickerDialog.setButton(TimePickerDialog.BUTTON_NEGATIVE, "취소", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 타임피커 다이얼로그 뒷배경 여백 투명하게
                timePickerDialog.show();
            }
        });

        //일정 종료 시간
        schedule_end_time.setOnClickListener(new View.OnClickListener() { // 일정 끝나는 시간 타임피커
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog
                        (getActivity(), android.R.style.Theme_Holo_Light_Dialog,new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int ehour, int eminute) {
                                endHour = ehour;
                                endMinute = eminute;
                                String endScheduleHour = String.format("%02d",ehour);
                                String endScheduleMinute = String.format("%02d",eminute);
                                schedule_end_time.setText(endScheduleHour + " : " + endScheduleMinute);
                            }
                        },endHour, endMinute, true);

                // 타임피커 설정(확인) 버튼
                timePickerDialog.setButton(TimePickerDialog.BUTTON_POSITIVE, "설정", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                // 타임피커 취소 버튼
                timePickerDialog.setButton(TimePickerDialog.BUTTON_NEGATIVE, "취소", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 타임피커 다이얼로그 뒷배경 여백 투명하게
                timePickerDialog.show();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbtn_blue){
                    radioState = 0;
                }else if(checkedId == R.id.rbtn_red){
                    radioState = 1;
                }else if(checkedId == R.id.rbtn_green){
                    radioState = 2;
                }
            }
        });

        // 저장 버튼
        dialog.setPositiveButton("등록", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


                int saveStartYears = startYears;
                int saveStartMonths = startMonths;
                int saveStartDays = startDays;
                int saveEndYears = endYears;
                int saveEndMonths = endMonths;
                int saveEndDays = endDays;
                int saveStartTime =  (startHour * 100) + (startMinute);
                int saveEndTime = (endHour * 100) + (endMinute);
                int saveRadioState = radioState;
                String title = schedule_title.getText().toString();
                String subtitle = schedule_text.getText().toString();
                boolean scheduleLoof = allDayCheck.isChecked();
                if(allDayCheck.isChecked()){
                    saveStartTime = 0;
                    saveEndTime = 2359;
                }


                // 그대로 데이터베이스에 연동하면됨
                // for 시작날~끝난날까지 DB삽입
//                Log.d("MyTag",String.valueOf(saveStartYears) + "년" + String.valueOf(saveStartMonths) + "월" + String.valueOf(saveStartDays) + "일");
//                Log.d("MyTag",String.valueOf(saveEndYears) + "년" + String.valueOf(saveEndMonths) + "월" + String.valueOf(saveEndDays) + "일");
//                Log.d("MyTag",saveStartTime + " 부터 " +saveEndTime + " 까지");
//                Log.d("MyTag",title + " / " +subtitle);

                Calender_DB inputCalData = new Calender_DB();
                // 일정 시작일
                inputCalData.setStart_years(saveStartYears);
                inputCalData.setStart_month(saveStartMonths);
                inputCalData.setStart_day(saveStartDays);
                inputCalData.setStart_time(saveStartTime);

                // 일정 마지막일
                inputCalData.setEnd_years(saveEndYears);
                inputCalData.setEnd_month(saveEndMonths);
                inputCalData.setEnd_day(saveEndDays);
                inputCalData.setEnd_time(saveEndTime);

                // 일정 내용 추가
                inputCalData.set_titles(title);
                inputCalData.set_subtitle(subtitle);

                //일정 카테고리 색상 추가
                inputCalData.set_calanderCategory(saveRadioState);

                // 입력한 일정을 DB에 추가
                calender_dao.insertAll(inputCalData);
                refresh();

                nolist_add.setVisibility(View.GONE);
                nolist_add_text.setVisibility(View.GONE);

                Log.v("일정 등록", "일정 날짜 : "+startDays+" ~ "+endDays);
                Log.v("일정 등록", "일정 등록 시간 : "+startHour+":"+startMinute+" ~ "+endHour+":"+endMinute);
                Log.v("일정 등록", "일정 제목 : "+title);
                Log.v("일정 등록", "일정 내용 : "+subtitle);

                reloadrecyclerview(YearData,monthData,dayData);
            }
        });

        // 취소 버튼
        dialog.setNegativeButton(R.string.cancel ,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_main_basic, container, false);

        // TTS 버튼
        main_basic_TTS_btn = view.findViewById(R.id.main_easy_tts1);

        Calender_DBSet dbController = Room.databaseBuilder(getActivity().getApplicationContext(), Calender_DBSet.class, "CalenderDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        User_DBset userdbController = Room.databaseBuilder(getActivity().getApplicationContext(), User_DBset.class, "UserInfoDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        user_dao = userdbController.user_dao();

        calender_dao = dbController.calender_dao();

        // 메인 DB호출
        List<Calender_DB> Maindata = calender_dao.getAllData();

        //일정 없는날 등록하는 버튼
        nolist_add = view.findViewById(R.id.main_basic_nolist_add);
        nolist_add_text = view.findViewById(R.id.main_basic_nolist_add_text);

        //시작일, 종료일 데이터 저장
        calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = (calendar.get(Calendar.MONTH));
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        //한국어 설정 (ex: date picker)
        Locale.setDefault(Locale.KOREAN);

        // 디데이 다이얼로그
        d_day = (TextView) view.findViewById(R.id.main_basic_dday);
        d_day_text = (TextView) view.findViewById(R.id.main_basic_dday_text);


//        List<Calender_DB> d_day_DB = calender_dao.loadMainData(1);
//        if (d_day_DB.get(0).get_mainActTime() == 0) {
//            d_day.setText("D-Day");
//        }
//
//        Log.v("디데이 설정 값",d_day_DB.get(0).get_mainActTime()+"");



//        Log.v("mainflag", "Maindata : " + Maindata.get(0).get_mainActDTitle());
//        d_day_text.setText("default");
        if(!Maindata.get(0).get_mainActDTitle().isEmpty())
            d_day_text.setText(Maindata.get(0)._mainActDTitle);
        d_day.setOnClickListener(this);
        d_day_text.setOnClickListener(this);

        // 타이틀 다이얼로그
//        edit_title = (ImageButton) view.findViewById(R.id.edit_button);
//        edit_title.setOnClickListener(this);
        maintitle_txt = (TextView) view.findViewById(R.id.new_main_basic_title);

        if(Maindata.get(0).get_mainActTitle() == null){
            maintitle_txt.setText(""); // 초기 제목
        }else{
            maintitle_txt.setText(Maindata.get(0).get_mainActTitle());
        }

        // 현재 시간
        now = view.findViewById(R.id.main_basic_now);
//        now.setText(getTime());
        Main_Basic_Frag.MainTimerTask timerTask = new Main_Basic_Frag.MainTimerTask();
        mTimer = new Timer();
        mTimer.schedule(timerTask, 500, 1000);

        fade_in = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),
                R.anim.fade_in);

        fade_out = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),
                R.anim.fade_out);

        //리스트 밑 일정 등록 버튼
//        add_schedule = view.findViewById(R.id.add_schedule);
//        add_schedule_txt = view.findViewById(R.id.add_schedule_txt);
//        add_schedule_dot = view.findViewById(R.id.add_schedule_dot);

        //플로팅 버튼
        mContext = getActivity().getApplicationContext();

        floating_open = AnimationUtils.loadAnimation(mContext, R.anim.floating_open);
        floating_close = AnimationUtils.loadAnimation(mContext, R.anim.floating_close);

        floating_main = view.findViewById(R.id.floating_main);
        floating_edit = view.findViewById(R.id.floating_edit);
        floating_voice = view.findViewById(R.id.floating_voice);

        floating_main.setOnClickListener(this);
        floating_edit.setOnClickListener(this);
        floating_voice.setOnClickListener(this);

        recyclerView = view.findViewById(R.id.recycler_view_easy);

        list_itemAdapter = new List_ItemAdapter();
        recyclerView.setAdapter(list_itemAdapter);

        D_dayfirsySet();

        d_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), endDateSetListener, (currentYear), (currentMonth), currentDay).show();
//                DatePickerDialog.Builder dialog = new DatePickerDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogTheme));
            }
        });

        //화면 클리어
//        list_itemAdapter.removeAllItem();

        // -------------------------------------------------------- DB 데이터 넣는곳
        //샘플 데이터 생성

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


        int appData = calender_like_data.size();


        // 일정 리스트 눌러서 뜨는 다이얼로그
        list_itemAdapter.setOnItemClickListener(new List_ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(View v, int pos) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogTheme));
                LayoutInflater inflater= getLayoutInflater();
                View view = inflater.inflate(R.layout.schedule_basic, null);
                dialog.setView(view);

                final EditText schedule_title = (EditText) view.findViewById(R.id.schedule_basic_title_ed);
                final TextView schedule_start_time = (TextView) view.findViewById(R.id.schedule_basic_start_time_ed);
                final TextView schedule_end_time = (TextView) view.findViewById(R.id.schedule_basic_end_time_ed);
                final EditText schedule_text = (EditText) view.findViewById(R.id.schedule_basic_text_ed);


                List<Calender_DB> calender_like_data = calender_dao.loadAllDataByYears(
                        Integer.parseInt(YearData),
                        Integer.parseInt(monthData),
                        Integer.parseInt(dayData)
                );

                String startTime = String.format("%04d", calender_like_data.get(pos).getStart_time());
                String valueStartTime = startTime.substring(0,2) + " : " + startTime.substring(2, startTime.length());
                String EndTime = String.format("%04d", calender_like_data.get(pos).getEnd_time());
                String valueEndTime = EndTime.substring(0,2) + " : " + EndTime.substring(2, EndTime.length());

                schedule_title.setText(calender_like_data.get(pos).get_titles());
                schedule_start_time.setText(valueStartTime);
                schedule_end_time.setText(valueEndTime);
                schedule_text.setText(calender_like_data.get(pos).get_subtitle());



                final int startHour=Integer.parseInt(startTime.substring(0,2)), startMinute=Integer.parseInt(startTime.substring(2, startTime.length()));
                final int endHour=Integer.parseInt(EndTime.substring(0,2)), endMinute=Integer.parseInt(EndTime.substring(2, startTime.length()));

                schedule_start_time.setOnClickListener(new View.OnClickListener() { // 일정 시작 시간 타임피커
                    @Override
                    public void onClick(View view) {
                        TimePickerDialog timePickerDialog = new TimePickerDialog
                                (getActivity(), android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hour, int minute) {
                                        String startHour = String.format("%02d",hour);
                                        String startMinute = String.format("%02d",minute);
                                        schedule_start_time.setText(startHour + " : " + startMinute);
                                    }
                                },startHour, startMinute, true);

                        // 타임피커 설정(확인) 버튼
                        timePickerDialog.setButton(TimePickerDialog.BUTTON_POSITIVE, "설정", new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        // 타임피커 취소 버튼
                        timePickerDialog.setButton(TimePickerDialog.BUTTON_NEGATIVE, "취소", new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 타임피커 다이얼로그 뒷배경 여백 투명하게
                        timePickerDialog.show();
                    }
                });

                schedule_end_time.setOnClickListener(new View.OnClickListener() { // 일정 끝나는 시간 타임피커
                    @Override
                    public void onClick(View view) {
                        TimePickerDialog timePickerDialog = new TimePickerDialog
                                (getActivity(), android.R.style.Theme_Holo_Light_Dialog,new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hour, int minute) {
                                        String endHour = String.format("%02d",hour);
                                        String endMinute = String.format("%02d",minute);
                                        schedule_end_time.setText(endHour + " : " + endMinute);
                                    }
                                },endHour, endMinute, true);

                        // 타임피커 설정(확인) 버튼
                        timePickerDialog.setButton(TimePickerDialog.BUTTON_POSITIVE, "설정", new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        // 타임피커 취소 버튼
                        timePickerDialog.setButton(TimePickerDialog.BUTTON_NEGATIVE, "취소", new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 타임피커 다이얼로그 뒷배경 여백 투명하게
                        timePickerDialog.show();
                    }
                });

                // 저장 버튼
                dialog.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        List<Calender_DB> loadDb = calender_dao.getAllData();
                        Calender_DB calender_db = new Calender_DB();

                        int scheduleKey = calender_like_data.get(pos).getNum();
                        Log.v("선택한 일정의 일정시작 시간",startHour+startMinute+"");
                        Log.v("선택한 일정의 num",calender_like_data.get(pos).getNum()+"");

                        for (int i = 0; i < loadDb.size(); i++){
                            if (scheduleKey == loadDb.get(i).getNum()) {
                                Log.v("12",loadDb.get(i).getStart_time()+"");
                                calender_dao.UpdateThisScadule(calender_like_data.get(pos).getNum(),
                                                                schedule_title.getText().toString(),
                                                                schedule_text.getText().toString(),
                                        schedule_start_time.getText().toString().substring(0,2)+
                                                schedule_start_time.getText().toString().substring(5,7),
                                        schedule_end_time.getText().toString().substring(0,2)+
                                                schedule_end_time.getText().toString().substring(5,7));

                            }
                        }
                        reloadrecyclerview(YearData,monthData,dayData);
                    }
                });

                // 삭제 버튼
                dialog.setNegativeButton("삭제",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        calender_dao.deleteCalendar(calender_like_data.get(pos).getNum());
                        reloadrecyclerview(YearData,monthData,dayData);
                        Toast.makeText(getActivity().getApplicationContext(), "calender_like_data.get(pos).getNum() : " + calender_like_data.get(pos).getNum(), Toast.LENGTH_SHORT).show();

                    }
                });
                Log.v("일정 순서","현재 클릭한 일정 순번 : "+(pos+1)+"번째");
                dialog.show();
            }
        });

        if (calender_like_data.isEmpty()) {
            nolist_add.setVisibility(View.VISIBLE);
            nolist_add_text.setVisibility(View.VISIBLE);
            nolist_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    edit();
                    recyclerView.invalidate();
                    recyclerView.getAdapter().notifyDataSetChanged();
//                    Intent n = new Intent(getActivity(), AddSchedule.class);
//                    startActivity(n);
                }
            });

        } else {
            nolist_add.setVisibility(View.GONE);
            nolist_add_text.setVisibility(View.GONE);
            Toast.makeText(getActivity().getApplicationContext(), "data ari", Toast.LENGTH_SHORT).show();
//            Toast.makeText(getActivity().getApplicationContext(), calender_like_data.size(), Toast.LENGTH_SHORT).show();
            for (int i = 0; i < calender_like_data.size(); i++) {
                List_Item calList = new List_Item();
                String startTime = String.format("%04d", calender_like_data.get(i).getStart_time());
                String valueStartTime = startTime.substring(0,2) + ":" + startTime.substring(2, startTime.length());
                String EndTime = String.format("%04d", calender_like_data.get(i).getEnd_time());
                String valueEndTime = EndTime.substring(0,2) + ":" + EndTime.substring(2, EndTime.length());

                calList.setTime(valueStartTime + "~" + valueEndTime);
                calList.setTitle(calender_like_data.get(i).get_titles());
                calList.setText(calender_like_data.get(i).get_subtitle());
                calList.setBackgroundcolor(calender_like_data.get(i).get_calanderCategory());


//                Toast.makeText(getActivity().getApplicationContext(), calender_like_data.get(i).get_titles(), Toast.LENGTH_SHORT).show();

                list_itemAdapter.addItem(calList);
            }
            list_itemAdapter.notifyDataSetChanged();
            recyclerView.startLayoutAnimation();
        }

        list_itemAdapter.notifyDataSetChanged();
        recyclerView.startLayoutAnimation();

        Log.v("체크",getArguments()+"");

        Bundle check = this.getArguments();

        // 이쪽이다
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int now_like_data = calender_like_data.size();
                while (true){


                    try {

                        
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

//                    try {
//
//                        if (now_like_data != calender_like_data.size()){
//                            Log.v("체크","데이터 받음 : "+refresh);
//                            reloadrecyclerview(YearData,monthData,dayData);
//                            now_like_data = calender_like_data.size();
//                        } else {
//                            Log.v("체크","리스트 갯수"+calender_like_data.size());
//                            Log.v("체크","필ㅇ요없음"+ now_like_data +"/"+calender_like_data.size());
//                            Log.v("체크",list_itemAdapter.getItemCount()+"");
//
//                        }
//                        Thread.sleep(3000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }


//                    if (check != null) {
//                        //넘어온 메시지 변수에 담기
//                        refresh = getArguments().getString("message");
//                        Log.v("체크","데이터 받음 : "+refresh);
//                        reloadrecyclerview(YearData,monthData,dayData);
//                    } else {
//
//                   }

//                    if (refresh == "0") {
//                        Log.v("체크","데이터 받은거 없음. 리프레시 안함 ㅅㄱ");
//                    } else if (getArguments().getString("message") == "1") {
//                        //넘어온 메시지 변수에 담기
//                        refresh = getArguments().getString("message");
//                        Log.v("체크","데이터 받음 : "+refresh);
//                        reloadrecyclerview(YearData,monthData,dayData);
//                    }

                }
            }
        });

        thread.start();


//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                if (refresh == "0") {
//                    Log.v("체크","데이터 받은거 없음. 리프레시 안함 ㅅㄱ");
//                } else if (getArguments().getString("message") == "1") {
//                    //넘어온 메시지 변수에 담기
//                    refresh = getArguments().getString("message");
//                    Log.v("체크","데이터 받음 : "+refresh);
//                    reloadrecyclerview(YearData,monthData,dayData);
//                }
//            }
//        }, 2500);

        // 아이템 클릭 이벤트
        /*
        list_itemAdapter.setOnitemClickListener(new List_ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                // 해당 아이템의 포지션 정보를 제공
                Toast.makeText(getActivity().getApplication(), "test : " + pos, Toast.LENGTH_SHORT).show();
            }
        });
        */

        list_itemAdapter.setOnitemLongClickListener(new List_ItemAdapter.OnItemLongClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
//                Toast.makeText(getActivity().getApplicationContext(), "LongClick : " + pos, Toast.LENGTH_SHORT).show();

                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("");        // 다이얼로그 타이틀
                dialog.setMessage("정말로 해당 일정을 삭제하시겠습니까?");
                dialog.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        calender_dao.deleteCalendar(calender_like_data.get(pos).getNum());
                        reloadrecyclerview(YearData,monthData,dayData);

                        Toast.makeText(getActivity().getApplicationContext(), "calender_like_data.get(pos).getNum() : " + calender_like_data.get(pos).getNum(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity().getApplicationContext(), "삭제확인", Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity().getApplicationContext(), "취소", Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.show();

            }
        });
        



//        for (int i = 0; i < listDB; i++) {
//            List_Item list_item = new List_Item();
//            list_item.setTime("14:00" + "-" + i); // 시간
//            list_item.setTitle("과제하기" + "-" + i); // 일정 제목
//            list_item.setText("그치만 하기 싫은걸" + "-" + i); // 일정 내용
//            //데이터 등록
//            list_itemAdapter.addItem(list_item);
//        }
        //적용
//        list_itemAdapter.notifyDataSetChanged();

        tts = new TextToSpeech(getActivity().getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != ERROR){
                    tts.setLanguage(Locale.KOREA);
                }
            }
        });

        main_basic_TTS_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("main", "TTS 버튼");

                speakOut();
            }
        });


        //리스트 밑 일정 추가 버튼 나타나기
        recyclerView.startLayoutAnimation();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

//                lastVisibleItemPositions = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
//                itemTotalCounts = recyclerView.getAdapter().getItemCount() - 1;
//                if (lastVisibleItemPositions == itemTotalCounts) { // 마지막 아이템 자리일때
////                    add_schedule.setVisibility(View.VISIBLE); //화면에 보이게 한다.
////                    add_schedule_dot.setVisibility(View.VISIBLE); //화면에 보이게 한다.
////                    add_schedule_txt.setVisibility(View.VISIBLE); //화면에 보이게 한다.
////                    add_schedule.startAnimation(fade_in);         //서서히 보이는 애니메이션
////                    add_schedule_txt.startAnimation(fade_in);         //서서히 보이는 애니메이션
////                    add_schedule_dot.startAnimation(fade_in);         //서서히 보이는 애니메이션
//
//                    add_schedule.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            edit();
//                            recyclerView.invalidate();
//                            reloadrecyclerview(YearData,monthData,dayData);
//                        }
//                    });
//
//                } else if (lastVisibleItemPositions != itemTotalCounts) {
////                    add_schedule.setVisibility(View.GONE); //화면에 안보이게 한다.
////                    add_schedule_dot.setVisibility(View.GONE); //화면에 안보이게 한다.
////                    add_schedule_txt.setVisibility(View.GONE); //화면에 안보이게 한다.
////                    add_schedule.startAnimation(fade_out);
////                    add_schedule_dot.startAnimation(fade_out);
////                    add_schedule_txt.startAnimation(fade_out);
//
//                }
            }
        });
        return view;
    }

    //플로팅버튼 스위치
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floating_main:
                toggleFab();
                break;
            case R.id.floating_edit:
                toggleFab();
                edit();
                recyclerView.invalidate();
                break;
            case R.id.floating_voice:
                    toggleFab();
//                Toast.makeText(this, "일정 음성 등록 팝업", Toast.LENGTH_SHORT).show();
                    Custom_STT custom_stt = new Custom_STT(getActivity());
                    custom_stt.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    //테마 라운드 처리기능
                    int inputday = ((UidCode) getActivity().getApplication()).getStatic_day();
                    custom_stt.show();
//                Toast.makeText(getActivity(),"일정 음성 등록 팝업",Toast.LENGTH_SHORT).show();
                break;
        }

        // -------------------------------------------------------- DB 데이터 넣는곳
        // 로그인 후, 타이틀 제목 변경한 내용 DB에 넣어놔야 함
        // String타입으로 받으면 될듯

        // 타이틀 제목 변경 다이얼
        if (v.equals(edit_title)) {
            final EditText edit_title = new EditText(this.getActivity());
            AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogTheme));
            dialog.setTitle("제목을 입력해주세요"); // 다이얼 제목
            dialog.setView(edit_title); // 제목 입력하는 에딧 텍스트 표시. 여기에 입력한 내용 DB에 들어가야함
            edit_title.setText(maintitle_txt.getText()); // 위 표시한 에딧 내용에 현재 적용되있는 제목 넣어놓음(처음이라면 초기 제목)

            // 완료 버튼
            dialog.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String getTitle = edit_title.getText().toString();
                    maintitle_txt.setText(getTitle);
                    calender_dao.MainActTitleupdate(1, getTitle);
                }
            });

            // 취소 버튼
            dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            dialog.show();
        }

        // -------------------------------------------------------- DB 데이터 넣는곳
        // ---------------------------- 미완성
        // D-day 변경 다이얼


        // 내용
        if (v.equals(d_day_text)) {
//            final EditText edit_dday = new EditText(this.getActivity());
            final EditText edit_dday_text = new EditText(this.getActivity());
            AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogTheme));
            //getString(R.string.스트링 입력)
            dialog.setTitle("목표 D-day 일정을 입력해주세요");
            dialog.setView(edit_dday_text);
            dialog.setView(edit_dday_text);
//            edit_dday.setText("날짜"); // D-day 날짜
            edit_dday_text.setText(d_day_text.getText()); // D-day 내용

            // 완료 버튼
            dialog.setPositiveButton("완료", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
//                    String getDday = edit_dday.getText().toString();
                    String getText = edit_dday_text.getText().toString();
                    d_day_text.setText(getText);
                    // 데이터베이스에 입력한 값을 업데이트
                    calender_dao.MainActDtitleupdate(1, getText);
                }
            });

            // 취소 버튼
            dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            dialog.show();
        }

    }


    //플로팅버튼 동작
    private void toggleFab() {
        if (isFabOpen) {
            floating_main.setImageResource(R.drawable.ic_more);
            floating_edit.startAnimation(floating_close);
            floating_voice.startAnimation(floating_close);
            floating_edit.setClickable(false);
            floating_voice.setClickable(false);
            isFabOpen = false;
        } else {
            floating_main.setImageResource(R.drawable.ic_close);
            floating_edit.startAnimation(floating_open);
            floating_voice.startAnimation(floating_open);
            floating_edit.setClickable(true);
            floating_voice.setClickable(true);
            isFabOpen = true;
        }
    }

    void D_dayfirsySet(){
        List<Calender_DB> mainactDB = calender_dao.getAllData();
        // db에 만약 시간데이터가 있다면
        final Calendar ddayCalendar = Calendar.getInstance();

        final long dday = mainactDB.get(0).get_mainActTime();
        final long today = Calendar.getInstance().getTimeInMillis() / ONE_DAY;
        Log.v("MainDays", "dday : " + dday + "\n" + "today : " + today);
        long result = dday - today;

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
        Log.v("MainDays", strCount);

        List<Calender_DB> d_day_DB = calender_dao.loadMainData(1);
        if (d_day_DB.get(0).get_mainActTime() == 0) {
            d_day.setText("D-Day");
        } else {
            d_day.setText(strCount);
        }
        //디데이데이
    }

    public void inputData(String data) {
//        Date currentTime = Calendar.getInstance().getTime();
//        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
//        SimpleDateFormat monthFormat = new SimpleDateFormat("mm", Locale.getDefault());
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd", Locale.getDefault());

        Calendar cal = Calendar.getInstance();
        int sysYear = cal.get(Calendar.YEAR);
        int sysMonth = cal.get(Calendar.MONTH) +1;
        int sysDate = cal.get(Calendar.DAY_OF_MONTH);
        int sysHour = cal.get(Calendar.HOUR);
        int sysMinute = cal.get(Calendar.MINUTE);

        Log.d("HSH", "sysYear 값 : " + sysYear);


        Calender_DB inputCalData = new Calender_DB();
        // 일정 시작일
        inputCalData.setStart_years(00);
        inputCalData.setStart_month(00);
        inputCalData.setStart_day(00);
        inputCalData.setStart_time(00);

        // 일정 마지막일
        inputCalData.setEnd_years(00);
        inputCalData.setEnd_month(00);
        inputCalData.setEnd_day(00);
        inputCalData.setEnd_time(00);

        // 일정 내용 추가
        inputCalData.set_titles("saveTitleData");
        inputCalData.set_subtitle("");

        // 입력한 일정을 DB에 추가
        calender_dao.insertAll(inputCalData);

//        Log.v("stt", );

    }


    private void reloadrecyclerview(String YearData, String monthData, String dayData) {

        List<Calender_DB> calender_like_data = calender_dao.loadAllDataByYears(
                Integer.parseInt(YearData),
                Integer.parseInt(monthData),
                Integer.parseInt(dayData)
        );

        list_itemAdapter.removeAllItem();


        for (int i = 0; i < calender_like_data.size(); i++) {
            List_Item calList = new List_Item();
            String startTime = String.format("%04d", calender_like_data.get(i).getStart_time());
            String valueStartTime = startTime.substring(0,2) + " : " + startTime.substring(2, startTime.length());
            String EndTime = String.format("%04d", calender_like_data.get(i).getEnd_time());
            String valueEndTime = EndTime.substring(0,2) + " : " + EndTime.substring(2, EndTime.length());
            int radioState = calender_like_data.get(i).get_calanderCategory();



            calList.setTime(valueStartTime + "~ \n" + valueEndTime);
            calList.setTitle(calender_like_data.get(i).get_titles());
            calList.setText(calender_like_data.get(i).get_subtitle());
            calList.setBackgroundcolor(calender_like_data.get(i).get_calanderCategory());



            list_itemAdapter.addItem(calList);
//                        list_itemAdapter.addItem(calList); //두개 써있어서 하나 주석 해둠
        }
        list_itemAdapter.notifyDataSetChanged();
        recyclerView.startLayoutAnimation();


    }

}
