package com.example.calender.Main_Easy;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calender.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Single_Adapter extends RecyclerView.Adapter<Single_Adapter.Single_Adapter_ViewHolder> {

    private ArrayList<String> arrayList;
    private Context context;
    private ArrayList<Main_Easy_Calendar_Day> days;
    private int checkedPosition = Integer.parseInt(getToday())-1;

    // -1 : 아무런 선택도 없는 상태. 0 : 첫번째 아이템 자리 

    //===== 일정 리스트 클릭 이벤트 구현을 위해 추가된 코드 ==========================
    // OnItemClickListener 인터페이스 선언
    public interface OnItemClickListener {
        void onItemClicked(View v, int pos);
    }

    // OnItemClickListener 참조 변수 선언
    private static List_ItemAdapter_Easy.OnItemClickListener mListener = null;

    // OnItemClickListener 전달 메소드
    public void setOnItemClickListener (List_ItemAdapter_Easy.OnItemClickListener listener) {
        this.mListener = listener;
    }
    //======================================================================

    private String getToday() { //오늘 날짜 가져오기
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("d");
        String getToday = dateFormat.format(date);

        return getToday;
    }

    public Single_Adapter(Context context, ArrayList<Main_Easy_Calendar_Day> days){
        this.context = context;
        this.days = days;
    }

    public void SetMain_Easy_Calendar_Day(ArrayList<Main_Easy_Calendar_Day> days){
        this.days = new ArrayList<>();
        this.days = days;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Single_Adapter_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_easy_calendar_day, parent,false);
        return new Single_Adapter_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Single_Adapter_ViewHolder holder, int position) {
        holder.bind(days.get(position));
    }

    @Override
    public int getItemCount() {
        return days.size();
    }


    class Single_Adapter_ViewHolder extends RecyclerView.ViewHolder{

        private TextView day;
        private ImageView img;

        public Single_Adapter_ViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.easy_calendar_day);
            img = itemView.findViewById(R.id.imageView);




        }

        void bind(final Main_Easy_Calendar_Day main_easy_calendar_day){
            if (checkedPosition == -1)
            {
//                day.setTextColor(Color.RED);
                img.setVisibility(View.VISIBLE);
            } else
            {
                if (checkedPosition == getAdapterPosition()) // 앱 실행하면 오늘 날짜에 선택된 표시
                {
                    img.setVisibility(View.VISIBLE);
                    day.setTextColor(Color.WHITE);

                } else {
                    img.setVisibility(View.GONE);
                    day.setTextColor(ContextCompat.getColor(context, R.color.test1));
                    //달력 색
                }
            }
            day.setText(main_easy_calendar_day.getDay());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { // 날짜 눌렀을때
                    day.setTextColor(Color.WHITE); // 날짜 흰색
                    img.setVisibility(View.VISIBLE); // 배경이미지(검정 동그라미) 출력
                    if(checkedPosition != getAdapterPosition())
                    {
                        notifyItemChanged(checkedPosition);
                        checkedPosition = getAdapterPosition();
                    }

                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(mListener != null){
                            mListener.onItemClicked(v, pos);
                        }
                    }

                    // 클릭한 일자 출력
                    // 일자 데이터베이스 데이터를 찾는데 사용됨
                    int days = getAdapterPosition() + 1;
                    if (days != RecyclerView.NO_POSITION) {
//                        Log.v("주간달력", "현재 Day : " + days);
                    }
                }
            });
        }
    }




}
