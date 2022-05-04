package com.example.calender;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Main_basic extends FragmentActivity {

    TextView now;
    RecyclerView recyclerView;
    ItemAdapter itemAdapter;

    private String getTime() { //현재 시간 가져오기
        long now = System.currentTimeMillis(); // 현재 시간을 now 변수에 넣음
        Date date = new Date(now); // 현재 시간을 date 형식으로 변환
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM월 dd일 hh시 mm분");
        String getTime = dateFormat.format(date);
        return getTime;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_basic);

        now = (TextView) findViewById(R.id.main_basic_now);
        now.setText(getTime());

        recyclerView = findViewById(R.id.recycler_view);

        itemAdapter = new ItemAdapter();
        recyclerView.setAdapter(itemAdapter);

        //화면 클리어
        itemAdapter.removeAllItem();

        //샘플 데이터 생성
        for(int i = 0; i < 50; i++){

            Item item = new Item();
            item.setTitle("title"+i);
            item.setDescription("description" + i);

            //데이터 등록
            itemAdapter.addItem(item);
        }

        //적용
        itemAdapter.notifyDataSetChanged();

        //애니메이션 실행
        recyclerView.startLayoutAnimation();
    }

}
