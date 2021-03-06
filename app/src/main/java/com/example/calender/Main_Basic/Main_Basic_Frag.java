package com.example.calender.Main_Basic;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

import static android.speech.tts.TextToSpeech.ERROR;

public class Main_Basic_Frag extends Fragment implements View.OnClickListener, TextToSpeech.OnInitListener {

    // TTS ??????
    private ImageButton main_basic_TTS_btn;

    boolean threadstart = false;

    Calender_Dao calender_dao;
    User_Dao user_dao;
    long dbinputTime = 0;
    String dbinputTitle = "";
    String dbinputDtitle = "";

    // ????????? ????????? ???????????? ??????
    ImageButton nolist_add;
    TextView nolist_add_text;

    // ????????? ??????
    int dateEndY, dateEndM, dateEndD;
    int ddayValue = 0;

    // ?????? ????????? ?????? ?????? ??????
    Calendar calendar;
    int currentYear, currentMonth, currentDay;

    // Millisecond ????????? ??????(24 ??????)
    private final int ONE_DAY = 24 * 60 * 60 * 1000;

    // TTS
    private TextToSpeech tts;
    private ImageButton btn_Tts;

    Context context;
    Main_Basic mainbasic;       // ??????????????? ???????????? ??????
    private Timer mTimer;
    //????????? ??????
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


    // ?????? ?????? ??????????????? ????????????

    private Handler mHandler = new Handler();

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {

            Date rightNow = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat(
                    "M??? d??? h??? m???");
            String dateString = formatter.format(rightNow);
            now.setText(dateString);

        }
    };

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS){
            int ttsResult = tts.setLanguage(Locale.KOREA); // TTS?????? ???????????? ??????

            if(ttsResult == TextToSpeech.LANG_NOT_SUPPORTED || ttsResult == TextToSpeech.LANG_MISSING_DATA){
                Log.e("TTS", "This Language is not supported");
            }else{

                speakOut();// onInit??? ??????????????? ???????????? ?????????
            }
        }else{
            Log.e("TTS", "Initialization Failed!");
        }
    }

    private void speakOut() {
        //CharSequence text = ????????????_?????????_???_???????????????.getText();
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
        String text = null;
        for(int i = 0; i < calender_like_data.size(); i++){
            text += calender_like_data.get(i).get_titles().toString();
            Log.v("tts", calender_like_data.get(i).get_titles());

            if(calender_like_data.size() - 1 == i)
                text += ",????????? ????????????,";
            else
                text += "?????????, ";
        }
        tts.setPitch((float)1.3); // ?????? ??? ?????? ??????
        tts.setSpeechRate((float)1.3); // ?????? ?????? ??????
//        tts.setPitch((float)1.5); // ?????? ??? ?????? ??????
//        tts.setSpeechRate((float)1.5); // ?????? ?????? ??????

        // ??? ?????? ????????????: ?????? ????????? ??? ?????????
        // ??? ?????? ????????????: 1. TextToSpeech.QUEUE_FLUSH - ???????????? ?????? ????????? ?????? ?????? TTS??? ?????? ??????
        //                 2. TextToSpeech.QUEUE_ADD - ???????????? ?????? ????????? ?????? ?????? ?????? TTS??? ?????? ??????
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }


    class MainTimerTask extends TimerTask {
        public void run() {
            mHandler.post(mUpdateTimeTask);
        }
    }

    @Override
    public void onDestroy() {
        if(tts!=null){ // ????????? TTS?????? ??????
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

    // DatePickerDialog?????????, ????????? ??????, ????????? ????????? ?????? ????????? ?????? ????????? ????????? ?????????
    private DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//            edit_endDateBtn.setText(year + "??? " + (monthOfYear + 1) + "??? " + dayOfMonth + "???");
            ddayValue = ddayResult_int(dateEndY, dateEndM, dateEndD);
            d_day.setText(getDday(year, monthOfYear, dayOfMonth));
        }
    };

    // ????????? ????????? year, mMonthOfYear : ????????? ????????? MonthOfYear, mDayOfMonth : ????????? ????????? DayOfMonth
    private String getDday(int mYear, int mMonthOfYear, int mDayOfMonth) {
        // D-day ??????
        List<Calender_DB> mainactDB = calender_dao.loadMainData(1);
        long result = 0;


        final Calendar ddayCalendar = Calendar.getInstance();
        ddayCalendar.set(mYear, mMonthOfYear, mDayOfMonth);


        // D-day ??? ????????? ?????? millisecond ?????? ???????????? d-day ?????? today ??? ?????? ?????????.
        final long dday = ddayCalendar.getTimeInMillis() / ONE_DAY;
        final long today = Calendar.getInstance().getTimeInMillis() / ONE_DAY;

        calender_dao.MainActDayupdate(1, dday);
        // ?????? ????????? ??????
        Log.v("MainDays", "dday : " + dday + "\n" + "today : " + today);
        result = dday - today;

        // db??? ????????? ?????? ???????????? ??????
        calender_dao.MainActDayupdate(1, dday);



        // ?????? ??? d-day ??? ?????? ??????
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

    // ????????? ??? ??????
    public int onCalculatorDate(int dateEndY, int dateEndM, int dateEndD) {
        try {
            Calendar today = Calendar.getInstance(); //?????? ?????? ??????
            Calendar dday = Calendar.getInstance();

            //?????????, ????????? ????????? ??????
            Calendar calendar = Calendar.getInstance();
            int cyear = calendar.get(Calendar.YEAR);
            int cmonth = (calendar.get(Calendar.MONTH) + 1);
            int cday = calendar.get(Calendar.DAY_OF_MONTH);

            today.set(cyear, cmonth, cday);
            dday.set(dateEndY, dateEndM, dateEndD);// D-day??? ????????? ???????????????.

            long day = dday.getTimeInMillis() / 86400000;
            // ?????? ?????? ?????? ?????? ????????? ??????
            //( 1?????? ???(86400000 = 24?????? * 60??? * 60??? * 1000(1??????) ) )

            long tday = today.getTimeInMillis() / 86400000;
            long count = tday - day; // ?????? ???????????? dday ????????? ????????? ?????????.
            return (int) count; // ????????? ?????? + ?????????????????????.
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    // ????????? ??? ????????? ????????? ??????
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
        // TTS ??????
        main_basic_TTS_btn = view.findViewById(R.id.tts_button);


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

        // ?????? DB??????
        List<Calender_DB> Maindata = calender_dao.getAllData();


        //?????? ????????? ???????????? ??????
        nolist_add = view.findViewById(R.id.main_basic_nolist_add);
        nolist_add_text = view.findViewById(R.id.main_basic_nolist_add_text);

        //?????????, ????????? ????????? ??????
        calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = (calendar.get(Calendar.MONTH));
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        //????????? ?????? (ex: date picker)
        Locale.setDefault(Locale.KOREAN);

        // ????????? ???????????????
        d_day = (TextView) view.findViewById(R.id.main_basic_dday);
        d_day_text = (TextView) view.findViewById(R.id.main_basic_dday_text);

        Log.v("mainflag", "Maindata : " + Maindata.get(0).get_mainActDTitle());
//        d_day_text.setText("default");
        if(!Maindata.get(0).get_mainActDTitle().isEmpty())
            d_day_text.setText(Maindata.get(0)._mainActDTitle);

        d_day.setOnClickListener(this);
        d_day_text.setOnClickListener(this);

        // ????????? ???????????????
        edit_title = (ImageButton) view.findViewById(R.id.edit_button);
        edit_title.setOnClickListener(this);
        maintitle_txt = (TextView) view.findViewById(R.id.main_basic_title);

        if(Maindata.get(0).get_mainActTitle() == null){
            maintitle_txt.setText("Title??? ??????????????????"); // ?????? ??????
        }else
            maintitle_txt.setText(Maindata.get(0).get_mainActTitle());


        // ?????? ??????
        now = view.findViewById(R.id.main_basic_now);
//        now.setText(getTime());
        Main_Basic_Frag.MainTimerTask timerTask = new Main_Basic_Frag.MainTimerTask();
        mTimer = new Timer();
        mTimer.schedule(timerTask, 500, 1000);

        fade_in = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),
                R.anim.fade_in);

        fade_out = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),
                R.anim.fade_out);

        //????????? ??? ?????? ?????? ??????
        add_schedule = view.findViewById(R.id.add_schedule);
        add_schedule_txt = view.findViewById(R.id.add_schedule_txt);
        add_schedule_dot = view.findViewById(R.id.add_schedule_dot);

        //????????? ??????
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

        //?????? ?????????
        list_itemAdapter.removeAllItem();

        // -------------------------------------------------------- DB ????????? ?????????
        //?????? ????????? ??????

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

//        Toast.makeText(getActivity().getApplication(), YearData + "-" + monthData + "-" + dayData, Toast.LENGTH_SHORT).show();

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

        // ????????? ?????? ?????????
        /*
        list_itemAdapter.setOnitemClickListener(new List_ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                // ?????? ???????????? ????????? ????????? ??????
                Toast.makeText(getActivity().getApplication(), "test : " + pos, Toast.LENGTH_SHORT).show();
            }
        });
        */

        list_itemAdapter.setOnitemLongClickListener(new List_ItemAdapter.OnItemLongClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
//                Toast.makeText(getActivity().getApplicationContext(), "LongClick : " + pos, Toast.LENGTH_SHORT).show();

                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("");        // ??????????????? ?????????
                dialog.setMessage("????????? ?????? ????????? ?????????????????????????");
                dialog.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        calender_dao.deleteCalendar(calender_like_data.get(pos).getNum());

                        Toast.makeText(getActivity().getApplicationContext(), "calender_like_data.get(pos).getNum() : " + calender_like_data.get(pos).getNum(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity().getApplicationContext(), "????????????", Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity().getApplicationContext(), "????????????", Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.show();

            }
        });
        



//        for (int i = 0; i < listDB; i++) {
//            List_Item list_item = new List_Item();
//            list_item.setTime("14:00" + "-" + i); // ??????
//            list_item.setTitle("????????????" + "-" + i); // ?????? ??????
//            list_item.setText("????????? ?????? ?????????" + "-" + i); // ?????? ??????
//            //????????? ??????
//            list_itemAdapter.addItem(list_item);
//        }
        //??????
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
                Log.v("main", "TTS ??????");

                speakOut();
            }
        });


        //????????? ??? ?????? ?????? ?????? ????????????
        recyclerView.startLayoutAnimation();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                lastVisibleItemPositions = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                itemTotalCounts = recyclerView.getAdapter().getItemCount() - 1;
                if (lastVisibleItemPositions == itemTotalCounts) { // ????????? ????????? ????????????
                    add_schedule.setVisibility(View.VISIBLE); //????????? ????????? ??????.
                    add_schedule_dot.setVisibility(View.VISIBLE); //????????? ????????? ??????.
                    add_schedule_txt.setVisibility(View.VISIBLE); //????????? ????????? ??????.
//                    add_schedule.startAnimation(fade_in);         //????????? ????????? ???????????????
//                    add_schedule_txt.startAnimation(fade_in);         //????????? ????????? ???????????????
//                    add_schedule_dot.startAnimation(fade_in);         //????????? ????????? ???????????????

                    add_schedule.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent n = new Intent(getActivity(), AddSchedule.class);
                            startActivity(n);
                        }
                    });

                } else if (lastVisibleItemPositions != itemTotalCounts) {
                    add_schedule.setVisibility(View.GONE); //????????? ???????????? ??????.
                    add_schedule_dot.setVisibility(View.GONE); //????????? ???????????? ??????.
                    add_schedule_txt.setVisibility(View.GONE); //????????? ???????????? ??????.
//                    add_schedule.startAnimation(fade_out);
//                    add_schedule_dot.startAnimation(fade_out);
//                    add_schedule_txt.startAnimation(fade_out);

                }
            }
        });
        return view;
    }

    //??????????????? ?????????
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floating_main:
                toggleFab();
                break;
            case R.id.floating_edit:
                toggleFab();
//                Toast.makeText(this, "?????? ?????? ?????? ??????", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity(), "?????? ?????? ?????? ??????", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(), AddSchedule.class);

                startActivity(i);
                break;
            case R.id.floating_voice:
                toggleFab();
//                Toast.makeText(this, "?????? ?????? ?????? ??????", Toast.LENGTH_SHORT).show();
                Custom_STT custom_stt = new Custom_STT(getActivity());
                int inputday = ((UidCode) getActivity().getApplication()).getStatic_day();
                custom_stt.show();
//                Toast.makeText(getActivity(),"?????? ?????? ?????? ??????",Toast.LENGTH_SHORT).show();
                break;
        }

        // -------------------------------------------------------- DB ????????? ?????????
        // ????????? ???, ????????? ?????? ????????? ?????? DB??? ???????????? ???
        // String???????????? ????????? ??????

        // ????????? ?????? ?????? ?????????
        if (v.equals(edit_title)) {
            final EditText edit_title = new EditText(this.getActivity());
            AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogTheme));
            dialog.setTitle("????????? ??????????????????"); // ????????? ??????
            dialog.setView(edit_title); // ?????? ???????????? ?????? ????????? ??????. ????????? ????????? ?????? DB??? ???????????????
            edit_title.setText(maintitle_txt.getText()); // ??? ????????? ?????? ????????? ?????? ??????????????? ?????? ????????????(??????????????? ?????? ??????)

            // ?????? ??????
            dialog.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String getTitle = edit_title.getText().toString();
                    maintitle_txt.setText(getTitle);
                    calender_dao.MainActTitleupdate(1, getTitle);
                }
            });

            // ?????? ??????
            dialog.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            dialog.show();
        }

        // -------------------------------------------------------- DB ????????? ?????????
        // ---------------------------- ?????????
        // D-day ?????? ?????????


        // ??????
        if (v.equals(d_day_text)) {
//            final EditText edit_dday = new EditText(this.getActivity());
            final EditText edit_dday_text = new EditText(this.getActivity());
            AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogTheme));
            dialog.setTitle("D-day??? ??????????????????");
            dialog.setView(edit_dday_text);
            dialog.setView(edit_dday_text);
//            edit_dday.setText("??????"); // D-day ??????
            edit_dday_text.setText(d_day_text.getText()); // D-day ??????

            // ?????? ??????
            dialog.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
//                    String getDday = edit_dday.getText().toString();
                    String getText = edit_dday_text.getText().toString();
                    d_day_text.setText(getText);
                    // ????????????????????? ????????? ?????? ????????????
                    calender_dao.MainActDtitleupdate(1, getText);
                }
            });

            // ?????? ??????
            dialog.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            dialog.show();
        }

    }


    //??????????????? ??????
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
        // db??? ?????? ?????????????????? ?????????
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

        Log.d("HSH", "sysYear ??? : " + sysYear);

        //???????????? ?????? ?????? ????????? ?????? ??????
        int y = 0;
        int m = 0;
        int w = 0;
        int d = 0;
        int h = 0;
        int mi = 0;
        int from = 0;
        int until = 0;
        int add = 0;

        // -1 = ??????, 0 = 1??????, 1 = 2??????
        int checkYearWord = -1;
        int checkMonthWord = -1;
        int checkWeekWord = -1;
        int checkDateWord = -1;
        int checkHourWord = -1;
        int checkMinuteWord = -1;
        int checkUntilWord = -1;

        //DB ?????? ?????????
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

        if (data.indexOf("??? ???") > -1) {
            checkYearWord = 1;
            y = data.indexOf("??? ???");
            saveYearData = sysYear + Integer.parseInt(data.substring(0, y));
            Log.d("HSH", "??? ??? ?????? y??? =" + y);
            Log.d("HSH", "??? ??? DB????????? =" + saveYearData);

        }else if(data.indexOf("???") > -1){
            checkYearWord = 0;
            y = data.indexOf("???");
            saveYearData = Integer.parseInt(data.substring(0, y));
            Log.d("HSH", "???, ?????? y??? =" + y);
            Log.d("HSH", "???, DB????????? =" + saveYearData);
        }

        if (data.indexOf("??? ???") > -1){
            checkMonthWord = 1;
            m = data.indexOf("??? ???");
            if(checkYearWord == 1){
                saveMonthData = sysMonth + Integer.parseInt(data.substring(y+3, m));
            }else if(checkYearWord == 0){
                saveMonthData = sysMonth + Integer.parseInt(data.substring(y+2, m));
            }else{
                saveMonthData = sysMonth + Integer.parseInt(data.substring(y, m));
            }
        }else if(data.indexOf("???") > -1){
            checkMonthWord = 0;
            m = data.indexOf("???");

            if(checkYearWord == 1){
                saveMonthData = Integer.parseInt(data.substring(y+3, m));
            }else if(checkYearWord == 0){
                saveMonthData = Integer.parseInt(data.substring(y+2, m));
            }else{
                saveMonthData = Integer.parseInt(data.substring(y, m));
            }
            Log.d("HSH", "???, ?????? m??? =" + m);
            Log.d("HSH", "???, DB????????? =" + saveMonthData);
        }

        if(data.indexOf("??? ???") > -1){
            checkDateWord = 1;
            d = data.indexOf("??? ???");

            if(checkMonthWord == 1){
                saveDateData = sysDate + Integer.parseInt(data.substring(m+3, d));
            }else if(checkMonthWord == 0){
                saveDateData = sysDate +Integer.parseInt(data.substring(m+2, d));
            }else{
                saveDateData = sysDate + Integer.parseInt(data.substring(m, d));
            }
            Log.d("HSH", "??? ???, ?????? d??? =" + d);
            Log.d("HSH", "??? ???, DB????????? =" + saveDateData);
        }else if(data.indexOf("??????") > -1){

        }else if(data.indexOf("???") > -1){
            checkDateWord = 0;
            d = data.indexOf("???");

            if(checkMonthWord == 1){
                saveDateData = Integer.parseInt(data.substring(m+3, d));
            }else if(checkMonthWord == 0){
                saveDateData = Integer.parseInt(data.substring(m+2, d));
            }else{
                saveDateData = Integer.parseInt(data.substring(m, d));
            }
            Log.d("HSH", "???, ?????? d??? =" + d);
            Log.d("HSH", "???, DB????????? =" + saveDateData);
        }

        if(data.indexOf("?????? ???") > -1){
            h = data.indexOf("?????? ???");
            saveHourData = sysHour + Integer.parseInt(data.substring(d, h));
            Log.d("HSH", "?????? ???, ?????? h??? =" + h);
            Log.d("HSH", "?????? ???, DB????????? =" + saveHourData);
        }else if(data.indexOf("??????") > -1){
            h = data.indexOf("??????");
            checkHourWord = 1;
            saveHourData = sysHour + Integer.parseInt(data.substring(d, h));
            Log.d("HSH", "??????, ?????? h??? =" + h);
            Log.d("HSH", "??????, DB????????? =" + saveHourData);
        }else if(data.indexOf("???") > -1){
            checkHourWord = 0;
            h = data.indexOf("???");

            if(checkDateWord == 1){
                saveHourData = Integer.parseInt(data.substring(d+3, h));
            }else if(checkDateWord == 0){
                saveHourData = Integer.parseInt(data.substring(d+2, h));
            }else{
                saveHourData = Integer.parseInt(data.substring(d, h));
            }
            Log.d("HSH", "???, ?????? h??? =" + h);
            Log.d("HSH", "???, DB????????? =" + saveHourData);
        }

        if(data.indexOf("??? ???") > -1){
            mi = data.indexOf("??? ???");
            checkMinuteWord = 1;

            if(checkHourWord == 1){
                saveMinuteData = sysMinute + Integer.parseInt(data.substring(h+3, mi));
            }else if(checkHourWord == 0){
                saveMinuteData = sysMinute + Integer.parseInt(data.substring(h+2, mi));
            }else{
                saveMinuteData = sysMinute + Integer.parseInt(data.substring(h, mi));
            }

            while(saveMinuteData >= 60){
                saveMinuteData = saveMinuteData - 60;
                saveHourData = saveHourData + 1;
            }
            Log.d("HSH", "??? ???, saveMinuteData ??? =" + saveMinuteData);


            Log.d("HSH", "??? ???, ?????? mi??? =" + mi);
            Log.d("HSH", "??? ???, DB????????? =" + saveHourData +": "+ saveMinuteData);
        }else if(data.indexOf("???") > -1){
            mi = data.indexOf("???");
            checkMinuteWord = 0;

            if(checkHourWord == 1){
                saveMinuteData = Integer.parseInt(data.substring(h+3, mi));
            }else if(checkHourWord == 0){
                saveMinuteData = Integer.parseInt(data.substring(h+2, mi));
            }else{
                saveMinuteData = Integer.parseInt(data.substring(h, mi));
            }

            Log.d("HSH", "???, ?????? mi??? =" + mi);
            Log.d("HSH", "???, DB????????? =" + saveMinuteData);
        }

        if(data.indexOf("??????") > -1){
            from = data.indexOf("??????");

            if (data.indexOf("??? ???") > -1) {
                checkYearWord = 1;
                y = data.indexOf("??? ???");
                saveEndYearData = sysYear + Integer.parseInt(data.substring(from + 3, y));

            }else if(data.indexOf("???") > -1){
                checkYearWord = 0;
                y = data.indexOf("???");
                saveEndYearData = Integer.parseInt(data.substring(from + 3, y));

            }

            if (data.indexOf("??? ???") > -1){
                checkMonthWord = 1;
                m = data.indexOf("??? ???");
                if(checkYearWord == 1){
                    saveEndMonthData = sysMonth + Integer.parseInt(data.substring(y+3, m));
                }else if(checkYearWord == 0){
                    saveEndMonthData = sysMonth + Integer.parseInt(data.substring(y+2, m));
                }else{
                    saveEndMonthData = sysMonth + Integer.parseInt(data.substring(from + 3, m));
                }
            }else if(data.indexOf("???") > -1){
                checkMonthWord = 0;
                m = data.indexOf("???");

                if(checkYearWord == 1){
                    saveEndMonthData = Integer.parseInt(data.substring(y+3, m));
                }else if(checkYearWord == 0){
                    saveEndMonthData = Integer.parseInt(data.substring(y+2, m));
                }else{
                    saveEndMonthData = Integer.parseInt(data.substring(from + 3, m));
                }
            }

            if(data.indexOf("??? ???") > -1){
                checkDateWord = 1;
                d = data.indexOf("??? ???");

                if(checkMonthWord == 1){
                    saveEndDateData = sysDate + Integer.parseInt(data.substring(m+3, d));
                }else if(checkMonthWord == 0){
                    saveEndDateData = sysDate +Integer.parseInt(data.substring(m+2, d));
                }else{
                    saveEndDateData = sysDate + Integer.parseInt(data.substring(from+3, d));
                }

            }else if(data.indexOf("??????") > -1){

            }else if(data.indexOf("???") > -1){
                checkDateWord = 0;
                d = data.indexOf("???");

                if(checkMonthWord == 1){
                    saveEndDateData = Integer.parseInt(data.substring(m+3, d));
                }else if(checkMonthWord == 0){
                    saveEndDateData = Integer.parseInt(data.substring(m+2, d));
                }else{
                    saveEndDateData = Integer.parseInt(data.substring(from+3, d));
                }
            }

            if(data.indexOf("?????? ???") > -1){
                h = data.indexOf("?????? ???");
                saveEndHourData = sysHour + Integer.parseInt(data.substring(from+3, h));
            }else if(data.indexOf("??????") > -1){
                h = data.indexOf("??????");
                checkHourWord = 1;
                saveEndHourData = sysHour + Integer.parseInt(data.substring(from+3, h));
            }else if(data.indexOf("???") > -1){
                checkHourWord = 0;
                h = data.indexOf("???");

                if(checkDateWord == 1){
                    saveEndHourData = Integer.parseInt(data.substring(d+3, h));
                }else if(checkDateWord == 0){
                    saveEndHourData = Integer.parseInt(data.substring(d+2, h));
                }else{
                    saveEndHourData = Integer.parseInt(data.substring(from+3, h));
                }

            }

            if(data.indexOf("??? ???") > -1){
                mi = data.indexOf("??? ???");
                checkMinuteWord = 1;

                if(checkHourWord == 1){
                    saveEndMinuteData = sysMinute + Integer.parseInt(data.substring(h+3, mi));
                }else if(checkHourWord == 0){
                    saveEndMinuteData = sysMinute + Integer.parseInt(data.substring(h+2, mi));
                }else{
                    saveEndMinuteData = sysMinute + Integer.parseInt(data.substring(from+3, mi));
                }

                while(saveEndMinuteData >= 60){
                    saveEndMinuteData = saveEndMinuteData - 60;
                    saveEndHourData = saveEndHourData + 1;
                }

            }else if(data.indexOf("???") > -1){
                mi = data.indexOf("???");
                checkMinuteWord = 0;

                if(checkHourWord == 1){
                    saveEndMinuteData = Integer.parseInt(data.substring(h+3, mi));
                }else if(checkHourWord == 0){
                    saveEndMinuteData = Integer.parseInt(data.substring(h+2, mi));
                }else{
                    saveEndMinuteData = Integer.parseInt(data.substring(from+3, mi));
                }

            }

        }
        else if(data.indexOf("??????") == -1){
            saveEndYearData = saveYearData;
            saveEndMonthData = saveMonthData;
            saveEndDateData = saveDateData;
            saveEndHourData = saveHourData;
            saveEndMinuteData = saveMinuteData;
        }

        if(data.indexOf("??????") > -1) {
            checkUntilWord = 1;
            until = data.indexOf("??????");
        }else if(data.indexOf("???") > -1){
            checkUntilWord = 0;
            until = data.indexOf("???");
        }

        if(data.indexOf("?????? ????????? ???") > -1){
            add = data.indexOf("????????? ???");
            if(checkUntilWord == 1){
                saveTitleData = data.substring(until+3, add);
            }else if(checkUntilWord == 0){
                saveTitleData = data.substring(until+2, add);

            }
        }else if(data.indexOf("????????? ???") > -1){
            add = data.indexOf("????????? ???");
            if(checkUntilWord == 1){
                saveTitleData = data.substring(until+3, add);
            }else if(checkUntilWord == 0){
                saveTitleData = data.substring(until+2, add);

            }
        }


        Log.d("HSH", "?????? DB ????????? = " + saveYearData +"??? "+ saveMonthData +"??? "+ saveDateData+"??? "
                + saveHourData+"??? "+ saveMinuteData + "??? " + "?????? : " + saveTitleData);

        Log.d("HSH", "?????? DB End ????????? = " + saveEndYearData +"??? "+ saveEndMonthData +"??? "+ saveEndDateData+"??? "
                + saveEndHourData+"??? "+ saveEndMinuteData + "??? ");

        String saveStartTime = Integer.toString(saveHourData) + Integer.toString(saveMinuteData);
        String saveEndTime = Integer.toString(saveEndHourData) + Integer.toString(saveEndMinuteData);

        Log.v("stt", "saveStartTime" + saveStartTime + "\n" + "saveEndTime" + saveEndTime);

        int saveStartTimeint = Integer.parseInt(saveStartTime);
        int saveEndTimeint = Integer.parseInt(saveEndTime);


        Calender_DB inputCalData = new Calender_DB();
        // ?????? ?????????
        inputCalData.setStart_years(00);
        inputCalData.setStart_month(00);
        inputCalData.setStart_day(00);
        inputCalData.setStart_time(00);

        // ?????? ????????????
        inputCalData.setEnd_years(00);
        inputCalData.setEnd_month(00);
        inputCalData.setEnd_day(00);
        inputCalData.setEnd_time(00);

        // ?????? ?????? ??????
        inputCalData.set_titles("saveTitleData");
        inputCalData.set_subtitle("");

        // ????????? ????????? DB??? ??????
        calender_dao.insertAll(inputCalData);

//        Log.v("stt", );

    }

}




//    @Override
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
