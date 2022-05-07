package com.example.calender;


import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MotionEventCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Main_Basic extends FragmentActivity{

    Main_Basic_Frag mainbasic_frag; // 프래그먼트 호출을 위한 객체 생성
    Calender_Basic_Frag calenderbasic_frag;

    Button btntest1, btntest2;      // 프래그먼트 전환을 위한 버튼

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintestlayout);        // 레이아웃 재세팅후 이부분 수정바랍니다
        Log.v("Frag", "Main_Basic 실행");

        mainbasic_frag = new Main_Basic_Frag(); // 객체 할당
        calenderbasic_frag = new Calender_Basic_Frag();

        btntest1 = (Button) findViewById(R.id.test_btn01);
        btntest2 = (Button) findViewById(R.id.test_btn02);

        btntest1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_main, mainbasic_frag)
                        .commit();
                // maintestlayout 에 있는 fragment_main 이라는 프래그먼트뷰에
                // 이름으로 그 이름 프래그먼트에 해당 프래그먼트 호출;
            }
        });

        btntest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_main, calenderbasic_frag)
                        .commit();
            }
        });

    }
}

/*
public class Main_Basic extends FragmentActivity implements View.OnClickListener{
    //플로팅 버튼
    private Context mContext;
    private FloatingActionButton floating_main, floating_edit, floating_voice;
    private Animation floating_open, floating_close;
    private boolean isFabOpen = false;

    ImageView add_schedule_dot;
    ImageButton add_schedule;
    TextView now, add_schedule_txt;
    RecyclerView recyclerView, mRecyclerView;
    List_ItemAdapter list_itemAdapter;
    int listDB=10;

    int lastVisibleItemPositions;
    int itemTotalCounts;

    Animation fade_in,fade_out;

    private String getTime() { //현재 시간 가져오기
        long now = System.currentTimeMillis(); // 현재 시간을 now 변수에 넣음
        Date date = new Date(now); // 현재 시간을 date 형식으로 변환
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM월 dd일 hh시 mm분");
        String getTime = dateFormat.format(date);
        return getTime;
    }

//    @Override 테스트용
//    public boolean onTouchEvent(MotionEvent event) {
//        int action= MotionEventCompat.getActionMasked(event);
//        int i=0;
//        switch (action){
//            case (MotionEvent.ACTION_UP) :
//                Log.d("Basic","Up " + i);
//                i++;
//                return true;
//
//            case (MotionEvent.ACTION_MOVE):
//                Log.d("Basic","Move " + i);
//                i++;
//                return true;
//
//            default:
//                return super.onTouchEvent(event);
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_basic);

        fade_in = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);

        fade_out = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_out);

        //리스트 밑 일정 등록 버튼
        add_schedule = (ImageButton) findViewById(R.id.add_schedule);
        add_schedule_txt = (TextView) findViewById(R.id.add_schedule_txt);
        add_schedule_dot = (ImageView) findViewById(R.id.add_schedule_dot);

        //플로팅 버튼
        mContext = getApplicationContext();

        floating_open = AnimationUtils.loadAnimation(mContext, R.anim.floating_open);
        floating_close = AnimationUtils.loadAnimation(mContext, R.anim.floating_close);

        floating_main = (FloatingActionButton) findViewById(R.id.floating_main);
        floating_edit = (FloatingActionButton) findViewById(R.id.floating_edit);
        floating_voice = (FloatingActionButton) findViewById(R.id.floating_voice);

        floating_main.setOnClickListener(this);
        floating_edit.setOnClickListener(this);
        floating_voice.setOnClickListener(this);

        //현재 시간
        now = (TextView) findViewById(R.id.main_basic_now);
        now.setText(getTime());


        recyclerView = findViewById(R.id.recycler_view);

        list_itemAdapter = new List_ItemAdapter();
        recyclerView.setAdapter(list_itemAdapter);

        //화면 클리어
        list_itemAdapter.removeAllItem();

        //샘플 데이터 생성
        for (int i = 0; i < listDB; i++) {
            List_Item list_item = new List_Item();
            list_item.setTime("14:00" + "-" + i);
            list_item.setTitle("과제하기" + "-" + i);
            list_item.setText("그치만 하기 싫은걸" + "-" + i);
            //데이터 등록
            list_itemAdapter.addItem(list_item);
        }

        //적용
        list_itemAdapter.notifyDataSetChanged();

        //리스트 밑 일정 추가 버튼 나타나기
        recyclerView.startLayoutAnimation();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                lastVisibleItemPositions = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                itemTotalCounts = recyclerView.getAdapter().getItemCount()-1;
                if (lastVisibleItemPositions == itemTotalCounts) {
//                    add_schedule.setVisibility(View.VISIBLE); //화면에 보이게 한다.
//                    add_schedule_dot.setVisibility(View.VISIBLE); //화면에 보이게 한다.
//                    add_schedule_txt.setVisibility(View.VISIBLE); //화면에 보이게 한다.
                    add_schedule.startAnimation(fade_in);
                    add_schedule_txt.startAnimation(fade_in);
                    add_schedule_dot.startAnimation(fade_in);
                } else if (lastVisibleItemPositions != itemTotalCounts){
                    add_schedule.setVisibility(View.INVISIBLE); //화면에 안보이게 한다.
                    add_schedule_dot.setVisibility(View.INVISIBLE); //화면에 안보이게 한다.
                    add_schedule_txt.setVisibility(View.INVISIBLE); //화면에 안보이게 한다.
//                    add_schedule.startAnimation(fade_out);
//                    add_schedule_dot.startAnimation(fade_out);
//                    add_schedule_txt.startAnimation(fade_out);

                }
            }
        });
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
                Toast.makeText(this, "일정 상세 등록 팝업", Toast.LENGTH_SHORT).show();
                break;
            case R.id.floating_voice:
                toggleFab();
                Toast.makeText(this, "일정 음성 등록 팝업", Toast.LENGTH_SHORT).show();
                break;
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
    }



*/