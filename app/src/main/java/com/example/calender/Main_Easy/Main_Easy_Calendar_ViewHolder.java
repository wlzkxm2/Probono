package com.example.calender.Main_Easy;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.calender.R;

public class Main_Easy_Calendar_ViewHolder extends RecyclerView.ViewHolder{

    public TextView easy_calendar_day;
    public Button button;

    Main_Easy_Calendar_ViewHolder(Context context, View itemView) {
        super(itemView);

        easy_calendar_day = itemView.findViewById(R.id.easy_calendar_day);
    }

}
