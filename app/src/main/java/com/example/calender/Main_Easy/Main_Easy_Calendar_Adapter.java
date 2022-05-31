package com.example.calender.Main_Easy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calender.R;

import java.util.ArrayList;

public class Main_Easy_Calendar_Adapter extends RecyclerView.Adapter<Main_Easy_Calendar_ViewHolder>{

    private ArrayList<String> arrayList;

    public Main_Easy_Calendar_Adapter() {
        arrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public Main_Easy_Calendar_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.main_easy_calendar_day, parent, false);

        Main_Easy_Calendar_ViewHolder main_easy_calendar_viewHolder = new Main_Easy_Calendar_ViewHolder(context, view);

        return main_easy_calendar_viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Main_Easy_Calendar_ViewHolder holder, int position) {
        String text = arrayList.get(position);
        holder.easy_calendar_day.setText(text);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    // 데이터를 입력
    public void setArrayData(String strData) {
        arrayList.add(strData);
    }

    // 데이터를 삭제
    public void removeArrayData() {
//        arrayList.remove(position);
//        notifyItemRemoved(position);
//        notifyDataSetChanged();
        arrayList.removeAll(arrayList);
        notifyDataSetChanged();

    }


}
