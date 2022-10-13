package com.example.calender.Calendar_Easy;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.calender.DataBase.Calender_DB;
import com.example.calender.DataBase.Calender_DBSet;
import com.example.calender.DataBase.Calender_Dao;
import com.example.calender.Main_Basic.List_Item;
import com.example.calender.Main_Basic.List_ItemAdapter;
import com.example.calender.Main_Easy.List_ItemAdapter_Easy;
import com.example.calender.Main_Easy.Main_Easy;
import com.example.calender.R;
import com.example.calender.StaticUidCode.UidCode;
import com.example.calender.addschedule.AddSchedule;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class Calendar_Easy extends AppCompatActivity {

    int startYears = 0, startMonths = 0, startDays = 0, endYears = 0, endMonths = 0, endDays = 0,
            startMinute = 0, endHour = 0, endMinute = 0, startHour = 0, startDate = 0;
    int startLoaclHour = 0, startLoaclMinute = 0, endLoaclHour = 0, endLoaclMinute = 0;

    // 일정 없는 날 일정추가버튼 표시
    private ImageButton nolist_add;
    private TextView nolist_add_text;

    public Calender_Dao calender_dao;       // 데이터 베이스 구축을 위한 객체

    // 에러 표시를 위한 태그
    private static final String TAG = "ErrorTag";
    // 시간 포멧을 위한 양식
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE, d, MMM,, yyyy");

    private MaterialCalendarView calendarView;  // calenderView 객체 생상

    TextView now;
    RecyclerView recyclerView;
    List_ItemAdapter_Easy list_itemAdapter_easy;
    Button addcal_btn;

    public static Calendar_Easy newInstance() {
        Calendar_Easy Calender_Easy = new Calendar_Easy();
        return Calender_Easy;
    }

    private void refresh() {
        overridePendingTransition(0, 0);//인텐트 효과 없애기
        Intent intent = getIntent(); //인텐트
        startActivity(intent); //액티비티 열기
        overridePendingTransition(0, 0);//인텐트 효과 없애기
    }

    @Nullable
    @Override
    protected void onCreate(@org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_easy);

        // 일정 없는 날 일정추가버튼 표시
        nolist_add = (ImageButton) findViewById(R.id.calendar_easy_nolist_add);
        nolist_add_text = (TextView) findViewById(R.id.calendar_easy_nolist_add_text);

        // 날짜를 입력받기 위한 배열
        // 배열로 밖에 데이터를 못받기 때문에 배열로 선언
        final int[] _year = {0};
        final int[] _month = {0};
        final int[] _day = {0};
        final int[] appData = {0};  // DB 내에 있는 데이터의 사이즈

//        addcal_btn = (Button) findViewById(R.id.easy_Addcal_btn);

        //<editor-fold desc="기본 뷰 세팅 코드">
        calendarView = (MaterialCalendarView) findViewById(R.id.easy_calendarView);
        recyclerView = findViewById(R.id.easy_recycler_view);
        list_itemAdapter_easy = new List_ItemAdapter_Easy();
        recyclerView.setAdapter(list_itemAdapter_easy);

        list_itemAdapter_easy.removeAllItem();
        //</editor-fold desc="기본 뷰 세팅 코드">

        //<editor-fold desc="DB 기본 세팅 코드">
        Calender_DBSet dbController = Room.databaseBuilder(Calendar_Easy.this.getApplicationContext(), Calender_DBSet.class, "CalenderDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        calender_dao = dbController.calender_dao();

        List<Calender_DB> calender_dbs = calender_dao.getAllData();
        //</editor-fold>
/*
        //<editor-fold desc="달력 꾸미기">
        // 주말엔 색 다르게 변경
        calendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator()
        );

        // 월 한글로
        calendarView.setTitleFormatter(
                new MonthArrayTitleFormatter(
                        getResources().getTextArray(R.array.custom_weekdays)
                )
        );

        //</editor-fold desc="달력 꾸미기">
*/

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


        //<editor-fold desc="캘린더에 일자가 눌렷을떄">
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Calendar_Easy calender_basic_frag = new Calendar_Easy();

                String month_t = "",
                        week_t = "",
                        title = "",
                        subtitle = "",
                        outputData = "";

                int time = 0000;      // 시간은 앞 두개 분은 뒤 두개
                // 예를 들어 12시 39분 일때 1239

                //<editor-fold desc="누른 일자를 구하는 함수">
                _day[0] = date.getDay();
                _month[0] = date.getMonth() + 1;
                _year[0] = date.getYear();
                String week = new SimpleDateFormat("EE")
                        .format(date.getCalendar().getTime());

                month_t = _year[0] + "년 " + _month[0] + "월 " + _day[0] + "일";
                week_t = week;

                ((UidCode) Calendar_Easy.this.getApplication()).setStatic_year(_year[0]);
                ((UidCode) Calendar_Easy.this.getApplication()).setStatic_month(_month[0]);
                ((UidCode) Calendar_Easy.this.getApplication()).setStatic_day(_day[0]);
                ((UidCode) Calendar_Easy.this.getApplication()).setWeek(week);
                //</editor-fold desc="누른 일자를 구하는 함수">

                // 데이터베이스 데이터 호출
                List<Calender_DB> calender_like_data = calender_dao.loadAllDataByYears(
                        _year[0],
                        _month[0],
                        _day[0]
                );
                appData[0] = calender_like_data.size();

                Toast.makeText(Calendar_Easy.this.getApplication(), _year[0] + "-" + _month[0] + "-" + _day[0], Toast.LENGTH_SHORT).show();

//                Log.v("HSH", Integer.toString(((UidCode) Calendar_Easy.this).getApplication()).getStatic_day()));

                list_itemAdapter_easy.removeAllItem();

                // 일정 리스트 눌러서 뜨는 다이얼로그
                list_itemAdapter_easy.setOnItemClickListener(new List_ItemAdapter_Easy.OnItemClickListener() {
                    @Override
                    public void onItemClicked(View v, int pos) {

                        AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(Calendar_Easy.this, R.style.AlertDialogTheme));
                        LayoutInflater inflater= getLayoutInflater();
                        View view = inflater.inflate(R.layout.schedule_basic, null);
                        dialog.setView(view);

                        final EditText schedule_title = (EditText) view.findViewById(R.id.schedule_basic_title_ed);
                        final TextView schedule_start_time = (TextView) view.findViewById(R.id.schedule_basic_start_time_ed);
                        final TextView schedule_end_time = (TextView) view.findViewById(R.id.schedule_basic_end_time_ed);
                        final EditText schedule_text = (EditText) view.findViewById(R.id.schedule_basic_text_ed);

                        String startTime = String.format("%04d", calender_like_data.get(pos).getStart_time());
                        String valueStartTime = startTime.substring(0,2) + " : " + startTime.substring(2, startTime.length());
                        String EndTime = String.format("%04d", calender_like_data.get(pos).getEnd_time());
                        String valueEndTime = EndTime.substring(0,2) + " : " + EndTime.substring(2, EndTime.length());

                        schedule_title.setText(calender_like_data.get(pos).get_titles());
                        schedule_start_time.setText(valueStartTime);
                        schedule_end_time.setText(valueEndTime);
                        schedule_text.setText(calender_like_data.get(pos).get_subtitle());

                        final int startHour=Integer.parseInt(startTime.substring(0,2)), startMinute=Integer.parseInt(startTime.substring(2, startTime.length()));
                        final int endHour=Integer.parseInt(EndTime.substring(0,2)), Calendar_EasyCalendar_EasyendMinute=Integer.parseInt(EndTime.substring(2, startTime.length()));

                        schedule_start_time.setOnClickListener(new View.OnClickListener() { // 일정 시작 시간 타임피커
                            @Override
                            public void onClick(View view) {
                                TimePickerDialog timePickerDialog = new TimePickerDialog
                                        (Calendar_Easy.this, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                String startHour = String.format("%02d",hourOfDay);
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
                                        (Calendar_Easy.this, android.R.style.Theme_Holo_Light_Dialog,new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                String endHour = String.format("%02d",hourOfDay);
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

//                        mArrayList.get (pos).setName (schedule_title);
//                        mArrayList.get (pos).setNumber (schedule_text);
//                        mAdapter.notifyItemChanged (position);
//                        dialog.dismiss();

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
//                                refresh();
                                list_itemAdapter_easy.notifyDataSetChanged();
                                recyclerView.setAdapter(list_itemAdapter_easy);
                            }
                        });

                        // 삭제 버튼
                        dialog.setNegativeButton("삭제",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                calender_dao.deleteCalendar(calender_like_data.get(pos).getNum());
//                                refresh();
                                list_itemAdapter_easy.notifyDataSetChanged();
                                recyclerView.setAdapter(list_itemAdapter_easy);
                            }
                        });
                        dialog.show();

                    }
                });


                if (calender_like_data.isEmpty()) { // 캘린더 눌러서 일정이 없을때
                    Log.v("일정이 없는 날입니다", Integer.toString(((UidCode) Calendar_Easy.this.getApplication()).getStatic_day()));
                    // 일정 없는 날 일정추가버튼 표시
                    nolist_add.setVisibility(View.VISIBLE);
                    nolist_add_text.setVisibility(View.VISIBLE);
                    nolist_add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            Intent n = new Intent(Calendar_Easy.this, AddSchedule.class);
//                            startActivity(n);


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
//                String dayData = dayFormat.format(currentTime);
//                LocalTime now = LocalTime.now();

                            startYears = ((UidCode) getApplication()).getStatic_year();
                            startMonths = ((UidCode) getApplication()).getStatic_month();
                            startDays = ((UidCode) getApplication()).getStatic_day();
                            endYears = ((UidCode) getApplication()).getStatic_year();
                            endMonths = ((UidCode) getApplication()).getStatic_month();
                            endDays = ((UidCode) getApplication()).getStatic_day();
//                startLoaclHour = now.getHour();
//                startLoaclMinute = now.getMinute();
//                endLoaclHour = now.getHour();
//                endLoaclMinute = now.getMinute();

//                List<Calender_DB> calender_like_data = calender_dao.loadAllDataByYears(
//                        Integer.parseInt(YearData),
//                        Integer.parseInt(monthData),
//                        Integer.parseInt(dayData)
//                );

                            AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(Calendar_Easy.this, R.style.AlertDialogTheme));
                            LayoutInflater inflater = getLayoutInflater();
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

                            if (startYears < 2000 || endYears < 2000) {
                                startYears = Integer.parseInt(yearFormat.format(today));
                                endYears = Integer.parseInt(yearFormat.format(today));
                            }
                            if (startMonths < 1 || endMonths < 1) {
                                startMonths = Integer.parseInt(monthFormat.format(today));
                                endMonths = Integer.parseInt(monthFormat.format(today));
                            }
                            if (startDays < 1 || endDays < 1) {
                                startDays = Integer.parseInt(dateFormat.format(today));
                                endDays = Integer.parseInt(dateFormat.format(today));
                            }

//                String startTime = String.format("%04d", calender_like_data.get(pos).getStart_time());
//                String valueStartTime = startTime.substring(0,2) + " : " + startTime.substring(2, startTime.length());
//                String EndTime = String.format("%04d", calender_like_data.get(pos).getEnd_time());
//                String valueEndTime = EndTime.substring(0,2) + " : " + EndTime.substring(2, EndTime.length());

//                schedule_title.setText(calender_like_data.get(pos).get_titles());
                            schedule_start_day.setText("시작날짜");
                            schedule_end_day.setText("종료날짜");
                            schedule_start_time.setText("시작시간");
                            schedule_end_time.setText("종료시간");
//                schedule_text.setText(calender_like_data.get(pos).get_subtitle());

//                final int startScheduleHour=Integer.parseInt(startTime.substring(0,2)), startScheduleMinute=Integer.parseInt(startTime.substring(2, startTime.length()));
//                final int endScheduleHour=Integer.parseInt(EndTime.substring(0,2)), endScheduleMinute=Integer.parseInt(EndTime.substring(2, startTime.length()));


                            GregorianCalendar gregorianCalendar = new GregorianCalendar();

                            // 일정시간 하루종일 체크
                            allDayCheck.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (allDayCheck.isChecked()) {
                                        schedule_start_time.setVisibility(View.GONE);
                                        schedule_end_time.setVisibility(View.GONE);
                                        dot.setVisibility(View.GONE);
                                    } else {
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
                                    DatePickerDialog datePickerDialog = new DatePickerDialog(Calendar_Easy.this, new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                            startYears = year;
                                            startMonths = month + 1;
                                            startDays = dayOfMonth;
                                            schedule_start_day.setText(year + "년 " + (month + 1) + "월 " + dayOfMonth + "일 ");
                                        }
                                    }, startYears, startMonths - 1, startDays);

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
                                    DatePickerDialog datePickerDialog = new DatePickerDialog(Calendar_Easy.this, new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                            startYears = year;
                                            startMonths = month + 1;
                                            startDays = dayOfMonth;
                                            schedule_end_day.setText(year + "년 " + (month + 1) + "월 " + dayOfMonth + "일 ");
                                        }
                                    }, startYears, startMonths - 1, startDays);

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
                                            (Calendar_Easy.this, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hour, int minute) {
                                                    String startScheduleHour = String.format("%02d", hour);
                                                    String startScheduleMinute = String.format("%02d", minute);
                                                    schedule_start_time.setText(startScheduleHour + " : " + startScheduleMinute);
                                                }
                                            }, startHour, startMinute, true);

                                    // 타임피커 설정(확인) 버튼
                                    timePickerDialog.setButton(TimePickerDialog.BUTTON_POSITIVE, "설정", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    // 타임피커 취소 버튼
                                    timePickerDialog.setButton(TimePickerDialog.BUTTON_NEGATIVE, "취소", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

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
                                            (Calendar_Easy.this, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hour, int minute) {
                                                    String endScheduleHour = String.format("%02d", hour);
                                                    String endScheduleMinute = String.format("%02d", minute);
                                                    schedule_end_time.setText(endScheduleHour + " : " + endScheduleMinute);
                                                }
                                            }, endHour, endMinute, true);

                                    // 타임피커 설정(확인) 버튼
                                    timePickerDialog.setButton(TimePickerDialog.BUTTON_POSITIVE, "설정", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    // 타임피커 취소 버튼
                                    timePickerDialog.setButton(TimePickerDialog.BUTTON_NEGATIVE, "취소", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 타임피커 다이얼로그 뒷배경 여백 투명하게
                                    timePickerDialog.show();
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
                                    int saveStartTime = (startHour * 100) + (startMinute);
                                    int saveEndTime = (endHour * 100) + (endMinute);
                                    String title = schedule_title.getText().toString();
                                    String subtitle = schedule_text.getText().toString();
                                    boolean scheduleLoof = allDayCheck.isChecked();
                                    if (allDayCheck.isChecked()) {
                                        saveStartTime = 0;
                                        saveEndTime = 2359;
                                    }


                                    // 그대로 데이터베이스에 연동하면됨
                                    // for 시작날~끝난날까지 DB삽입
                                    Log.d("MyTag", String.valueOf(saveStartYears) + "년" + String.valueOf(saveStartMonths) + "월" + String.valueOf(saveStartDays) + "일");
                                    Log.d("MyTag", String.valueOf(saveEndYears) + "년" + String.valueOf(saveEndMonths) + "월" + String.valueOf(saveEndDays) + "일");
                                    Log.d("MyTag", saveStartTime + " 부터 " + saveEndTime + " 까지");
                                    Log.d("MyTag", title + " / " + subtitle);

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

                                    // 입력한 일정을 DB에 추가
                                    calender_dao.insertAll(inputCalData);

//                                    refresh();
                                    list_itemAdapter_easy.notifyDataSetChanged();
                                    recyclerView.setAdapter(list_itemAdapter_easy);

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
                    });
                } else {
                    // 일정 있으면 추가버튼 없애기
//                    nolist_add.setVisibility(View.GONE);
//                    nolist_add_text.setVisibility(View.GONE);
                    for (int i = 0; i < calender_like_data.size(); i++) {
                        List_Item calList = new List_Item();
                        calList.setTime(Integer.toString(calender_like_data.get(i).getStart_time()));
                        calList.setTitle(calender_like_data.get(i).get_titles());
                        calList.setText(calender_like_data.get(i).get_subtitle());

                        list_itemAdapter_easy.addItem(calList);
                    }
                    list_itemAdapter_easy.notifyDataSetChanged();
                    recyclerView.startLayoutAnimation();
                }
            }
        });
        //</editor-fold desc="캘린더에 일자가 눌렷을떄">

        list_itemAdapter_easy.notifyDataSetChanged();
        recyclerView.startLayoutAnimation();

//        return view;
    }
}
