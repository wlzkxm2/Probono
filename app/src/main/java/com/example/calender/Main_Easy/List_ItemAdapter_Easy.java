package com.example.calender.Main_Easy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.calender.Main_Basic.List_Item;
import com.example.calender.R;

import java.util.ArrayList;

public class List_ItemAdapter_Easy extends RecyclerView.Adapter<List_ItemAdapter_Easy.ViewHolder>{

    ArrayList<List_Item> listItems = new ArrayList<List_Item>();

    //===== 일정 리스트 클릭 이벤트 구현을 위해 추가된 코드 ==========================
    // OnItemClickListener 인터페이스 선언
    public interface OnItemClickListener {
        void onItemClicked(View v, int pos);
    }

    // OnItemClickListener 참조 변수 선언
    private static OnItemClickListener mListener = null;

    // OnItemClickListener 전달 메소드
    public void setOnItemClickListener (OnItemClickListener listener) {
        this.mListener = listener;
    }
    //======================================================================

    int lastPosition = -1;

    Context context;

    static String TAG = "Adapter";

    public interface OnItemLongClickListener{
        void onItemClick(View v, int pos);
    }

    private static OnItemLongClickListener mListener_long = null;

    public void setOnitemLongClickListener(OnItemLongClickListener listener){
        this.mListener_long = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.item_layout_easy, viewGroup, false);

        context = viewGroup.getContext();

        //===== [Click 이벤트 구현을 위해 추가된 코드] =====================

//        List_ItemAdapter_Easy.ViewHolder viewHolder = new List_ItemAdapter_Easy.ViewHolder(itemView);
//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String data = "";
//                int position = viewHolder.getAdapterPosition();
//                if (position != RecyclerView.NO_POSITION) {
////                    data = viewHolder.getTextView().getText().toString();
//                }
//                itemClickListener.onItemClicked(v, pos);
//            }
//        });
        //==================================================================

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        if(viewHolder.getAdapterPosition() > lastPosition){

            //애니메이션 적용
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_row);
            ((ViewHolder) viewHolder).itemView.startAnimation(animation);

            List_Item listItem = listItems.get(position);
            viewHolder.setItem(listItem);
        }

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public void addItem(List_Item listItem){
        listItems.add(listItem);
    }

    public void removeAllItem(){
        listItems.clear();
    }

    public void removeItem(int pos){
        listItems.remove(pos);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView time;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.schedule_time);
            title = itemView.findViewById(R.id.schedule_title);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(mListener_long != null){
                            mListener_long.onItemClick(v, pos);
                        }
                    }
                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(mListener != null){
                            mListener.onItemClicked(v, pos);
                        }
                    }
                }
            });

        }

        public void setItem(List_Item item){

            time.setText(item.getTime());
            title.setText(item.getTitle());
        }
    }
}