package com.example.calender.Main_Easy;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calender.R;

import java.util.ArrayList;

public class Single_Adapter extends RecyclerView.Adapter<Single_Adapter.Single_Adapter_ViewHolder> {

    private Context context;
    private ArrayList<Main_Easy_Calendar_Day> days;
    private int checkedPosition = 0; // -1 : 아무런 선택도 없는 상태. 0 : 첫번째 아이템 자리

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

        public Single_Adapter_ViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.easy_calendar_day);
        }

        void bind(final Main_Easy_Calendar_Day main_easy_calendar_day){
            if (checkedPosition == -1)
            {
                day.setTextColor(Color.BLUE);
            } else
            {
                if (checkedPosition == getAdapterPosition())
                {
                    day.setTextColor(Color.RED);
                } else
                    day.setTextColor(Color.GREEN);
            }
            day.setText(main_easy_calendar_day.getDay());
            day.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    day.setVisibility(View.VISIBLE);
                    if(checkedPosition != getAdapterPosition())
                    {
                        notifyItemChanged(checkedPosition);
                        checkedPosition = getAdapterPosition();
                    }
                }
            });
        }
    }

    public Main_Easy_Calendar_Day getSelected(){
        if (checkedPosition != -1)
        {
            return days.get(checkedPosition);
        }
        return null;
    }


}
