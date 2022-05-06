package com.example.calender;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Calender_Basic_Frag extends Fragment {

    public static Calender_Basic_Frag newInstance() {
        Calender_Basic_Frag Calender_Basic = new Calender_Basic_Frag();
        return Calender_Basic;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calender_basic, container, false);
        return view;
    }

}
