package com.example.calender;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calender.Item;
import com.example.calender.R;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{

    ArrayList<Item> items = new ArrayList<Item>();

    int lastPosition = -1;

    Context context;

    static String TAG = "Adapter";

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.item_layout, viewGroup, false);

        context = viewGroup.getContext();

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        if(viewHolder.getAdapterPosition() > lastPosition){

            //애니메이션 적용
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_row);
            ((ViewHolder) viewHolder).itemView.startAnimation(animation);

            Item item = items.get(position);
            viewHolder.setItem(item);
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Item item){
        items.add(item);
    }

    public void removeAllItem(){
        items.clear();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView title_view;
        TextView description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title_view = itemView.findViewById(R.id.title_text);
            description = itemView.findViewById(R.id.desc_text);
        }

        public void setItem(Item item){

            title_view.setText(item.getTitle());
            description.setText(item.getDescription());
        }
    }
}