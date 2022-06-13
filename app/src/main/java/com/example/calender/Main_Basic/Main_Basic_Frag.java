package com.example.calender.Main_Basic;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.calender.DataBase.Calender_DB;
import com.example.calender.DataBase.Calender_DBSet;
import com.example.calender.DataBase.Calender_Dao;
import com.example.calender.DataBase.User_DBset;
import com.example.calender.DataBase.User_Dao;
import com.example.calender.Main_Easy.Main_Easy;
import com.example.calender.R;
import com.example.calender.StaticUidCode.UidCode;
import com.example.calender.addschedule.AddSchedule;
import com.example.calender.addschedule.Custom_STT;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class Main_Basic_Frag extends Fragment implements View.OnClickListener {

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

    // 현재 시간 실시간으로 구해오기

    private Handler mHandler = new Handler();

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {

            Date rightNow = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat(
                    "M월 d일 h시 m분");
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
    public void onDestroy() {
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
        Log.v("MainDays", "dday : " + dday + "\n" + "today : " + today);
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_basic, container, false);

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

        Log.v("mainflag", "Maindata : " + Maindata.get(0).get_mainActDTitle());
//        d_day_text.setText("default");
        if(!Maindata.get(0).get_mainActDTitle().isEmpty())
            d_day_text.setText(Maindata.get(0)._mainActDTitle);

        d_day.setOnClickListener(this);
        d_day_text.setOnClickListener(this);

        // 타이틀 다이얼로그
        edit_title = (ImageButton) view.findViewById(R.id.edit_button);
        edit_title.setOnClickListener(this);
        maintitle_txt = (TextView) view.findViewById(R.id.main_basic_title);

        if(Maindata.get(0).get_mainActTitle() == null){
            maintitle_txt.setText("Title을 설정해주세요"); // 초기 제목
        }else
            maintitle_txt.setText(Maindata.get(0).get_mainActTitle());


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
        add_schedule = view.findViewById(R.id.add_schedule);
        add_schedule_txt = view.findViewById(R.id.add_schedule_txt);
        add_schedule_dot = view.findViewById(R.id.add_schedule_dot);

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

        recyclerView = view.findViewById(R.id.recycler_view);

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
        list_itemAdapter.removeAllItem();

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

        Toast.makeText(getActivity().getApplication(), YearData + "-" + monthData + "-" + dayData, Toast.LENGTH_SHORT).show();

        Log.v("HSH", Integer.toString(((UidCode) getActivity().getApplication()).getStatic_day()));

        list_itemAdapter.removeAllItem();

        if (calender_like_data.isEmpty()) {
            nolist_add.setVisibility(View.VISIBLE);
            nolist_add_text.setVisibility(View.VISIBLE);
            nolist_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent n = new Intent(getActivity(), AddSchedule.class);
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

                list_itemAdapter.addItem(calList);
            }
        }

        list_itemAdapter.notifyDataSetChanged();
        recyclerView.startLayoutAnimation();

//        for (int i = 0; i < listDB; i++) {
//            List_Item list_item = new List_Item();
//            list_item.setTime("14:00" + "-" + i); // 시간
//            list_item.setTitle("과제하기" + "-" + i); // 일정 제목
//            list_item.setText("그치만 하기 싫은걸" + "-" + i); // 일정 내용
//            //데이터 등록
//            list_itemAdapter.addItem(list_item);
//        }
        //적용
        list_itemAdapter.notifyDataSetChanged();

        //리스트 밑 일정 추가 버튼 나타나기
        recyclerView.startLayoutAnimation();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                lastVisibleItemPositions = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                itemTotalCounts = recyclerView.getAdapter().getItemCount() - 1;
                if (lastVisibleItemPositions == itemTotalCounts) { // 마지막 아이템 자리일때
                    add_schedule.setVisibility(View.VISIBLE); //화면에 보이게 한다.
                    add_schedule_dot.setVisibility(View.VISIBLE); //화면에 보이게 한다.
                    add_schedule_txt.setVisibility(View.VISIBLE); //화면에 보이게 한다.
//                    add_schedule.startAnimation(fade_in);         //서서히 보이는 애니메이션
//                    add_schedule_txt.startAnimation(fade_in);         //서서히 보이는 애니메이션
//                    add_schedule_dot.startAnimation(fade_in);         //서서히 보이는 애니메이션

                    add_schedule.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent n = new Intent(getActivity(), AddSchedule.class);
                            startActivity(n);
                        }
                    });

                } else if (lastVisibleItemPositions != itemTotalCounts) {
                    add_schedule.setVisibility(View.GONE); //화면에 안보이게 한다.
                    add_schedule_dot.setVisibility(View.GONE); //화면에 안보이게 한다.
                    add_schedule_txt.setVisibility(View.GONE); //화면에 안보이게 한다.
//                    add_schedule.startAnimation(fade_out);
//                    add_schedule_dot.startAnimation(fade_out);
//                    add_schedule_txt.startAnimation(fade_out);

                }
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
//                Toast.makeText(this, "일정 상세 등록 팝업", Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "일정 상세 등록 팝업", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(), AddSchedule.class);

                startActivity(i);
                break;
            case R.id.floating_voice:
                toggleFab();
//                Toast.makeText(this, "일정 음성 등록 팝업", Toast.LENGTH_SHORT).show();
                Custom_STT custom_stt = new Custom_STT(getActivity());
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
            dialog.setPositiveButton("완료", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String getTitle = edit_title.getText().toString();
                    maintitle_txt.setText(getTitle);
                    calender_dao.MainActTitleupdate(1, getTitle);
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

        // -------------------------------------------------------- DB 데이터 넣는곳
        // ---------------------------- 미완성
        // D-day 변경 다이얼


        // 내용
        if (v.equals(d_day_text)) {
//            final EditText edit_dday = new EditText(this.getActivity());
            final EditText edit_dday_text = new EditText(this.getActivity());
            AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogTheme));
            dialog.setTitle("D-day를 설정해주세요");
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

        d_day.setText(strCount);
    }

    public void inputData(String data) {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("mm", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd", Locale.getDefault());


//        int y = data.indexOf("년뒤");
//        int MonthCheck = data.indexOf("달");
//        int MonthCheck2 = data.indexOf("월");
//        int WeakCheck = data.indexOf("주");
//        int DayCheck = data.indexOf("내일");
//
//
//        int YY = Integer.parseInt(data.substring(0,y));
//        String M = data.substring(0,MonthCheck+1);
//        String M2 = data.substring(0,MonthCheck2+1);
//        String W = data.substring(0,WeakCheck+1);
//        String D = data.substring(0,DayCheck+2);

//        Log.d("HSH" , "초기 Day값 = " + M + M2 + W + D );
        //boolean 으로 년 뒤 T/F 체크해서 개월 / 월 / 달 조건문 돌리기 why? : (년뒤 뒤부터, 월까지) 추출해야하니까
        int y = 0;
        int m = 0;
        int w = 0;
        int d = 0;
        int t = 0;
        int checkYearWord = -1;
        int checkMonthWord = -1;
        int checkWeekWord = -1;
        int saveYearData;
        int saveMonthData;
        int saveDateData;
        int saveTimeData;


        if (data.indexOf("년 뒤") > -1) {
            checkYearWord = 1;
            y = data.indexOf("년 뒤");
            saveYearData = Integer.parseInt(yearFormat.format(currentTime)) + Integer.parseInt(data.substring(0,y));
//            Log.d("HSH", "년 뒤 =" + y);
//            Log.d("HSH", "년 뒤 =" + saveYearData);

        } else if(data.indexOf("년") > -1) {
            checkYearWord = 0;
            y = data.indexOf("년");
            saveYearData = Integer.parseInt(data.substring(0,y));
//            Log.d("HSH", "년 뒤 =" + data.indexOf("년 뒤"));
        }else{ // 현재 년도 반환
            saveYearData = Integer.parseInt(yearFormat.format(currentTime));
        }

        if(data.indexOf("개월" ) > -1) {
            m = data.indexOf("개월");
            checkMonthWord = 1;
            if(checkYearWord == 0){
                saveMonthData = Integer.parseInt(data.substring(y + 1, m));
            }else if(checkYearWord == 1){
                saveMonthData = Integer.parseInt(data.substring(y + 2, m));
            }else{
                saveMonthData = Integer.parseInt(data.substring(0, m));
            }

        }else if(data.indexOf("월") > -1){
            m = data.indexOf("월");
            checkMonthWord = 0;
            if(checkYearWord == 1){
                saveMonthData = Integer.parseInt(data.substring(y + 1, m));
            }else if(checkYearWord == 2){
                saveMonthData = Integer.parseInt(data.substring(y + 2, m));
            }else{
                saveMonthData = Integer.parseInt(data.substring(0, m));
            }

        }else if(data.indexOf("달") > -1){
            m = data.indexOf("달");
            checkMonthWord = 0;
            if(checkYearWord == 1){
                saveMonthData = Integer.parseInt(data.substring(y + 1, m));
            }else if(checkYearWord == 2){
                saveMonthData = Integer.parseInt(data.substring(y + 2, m));
            }else{
                saveMonthData = Integer.parseInt(data.substring(0, m));
            }

        }else{//현재 달 반환
            saveMonthData = Integer.parseInt(monthFormat.format(currentTime));
        }

        if(data.indexOf("주 후") > -1){
            w = data.indexOf("주 후");
            checkWeekWord = 1;
            if(checkMonthWord == 0){
                saveDateData = Integer.parseInt(data.substring(m+1 , w)) * 7;
                saveDateData = Integer.parseInt(dateFormat.format(currentTime)) + saveDateData;
            }else if(checkMonthWord == 1){
                saveDateData = Integer.parseInt(data.substring(m+2 , w)) * 7;
                saveDateData = Integer.parseInt(dateFormat.format(currentTime)) + saveDateData;
            }else{
                saveDateData = Integer.parseInt(dateFormat.format(currentTime));
            }
//            while(saveDateData > 28){
//                if (saveMonthData == 2){
//                    if(saveYearData %4 ==0){//윤년이면
//                        saveDateData = saveDateData - 29;
//                        saveMonthData = saveMonthData + 1;
//                    }else{
//                        saveDateData = saveDateData - 28;
//                        saveMonthData = saveMonthData + 1;
//                    }
//                }else if (saveMonthData == 1 || saveMonthData == 3 || saveMonthData == 5 || saveMonthData == 7 || saveMonthData == 8 || saveMonthData == 10 || saveMonthData == 12) {
//                    saveDateData = saveDateData - 31;
//                    saveMonthData = saveMonthData + 1;
//                }else{
//                    saveDateData = saveDateData - 30;
//                    saveMonthData = saveMonthData + 1;
//                }
//            }
        }else if(data.indexOf("다음 주") > -1){
            w = data.indexOf("주");
            checkWeekWord = 0;

        }
        if(data.indexOf("일 뒤") > -1){
            d = data.indexOf("일 뒤");
            if(checkWeekWord == 0){
                saveDateData = Integer.parseInt(dateFormat.format(currentTime)) + Integer.parseInt(data.substring(w+1,d));
            }else if (checkWeekWord == 1){
                saveDateData = Integer.parseInt(dateFormat.format(currentTime)) + Integer.parseInt(data.substring(w+2,d));
            }else{
                saveDateData = Integer.parseInt(dateFormat.format(currentTime)) + Integer.parseInt(data.substring(0,d));
            }
        }else if(data.indexOf("요일") > -1){

        }else if(data.indexOf("일") > -1) {

        }else{//오늘 날짜 반환

        }



    }
}




//    @Overri환e
//    public void onClick(View view) {
//        if (isFabOpen) {
//            floating_main.setImageResource(R.drawable.ic_more);
//            floating_edit.startAnimation(floating_close);
//            floating_voice.startAnimation(floating_close);
//            floating_edit.setClickable(false);
//            floating_voice.setClickable(false);
//            isFabOpen = false;
//        } else {
//            floating_main.setImageResource(R.drawable.ic_close);
//            floating_edit.startAnimation(floating_open);
//            floating_voice.startAnimation(floating_open);
//            floating_edit.setClickable(true);
//            floating_voice.setClickable(true);
//            isFabOpen = true;
//        }
//    }
//}
