package com.example.calender;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Setting_dark extends Fragment {
    public static final String TAG="MainActivity";
    Button mod_btn,mod_btn1;
    String themeColor;


    public static Setting_dark newInstance() {
        Setting_dark setting_dark = new Setting_dark();
        return setting_dark;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.darkmode, container, false);

        themeColor=ThemeUtil.modLoad(getActivity().getApplicationContext());
        ThemeUtil.applyTheme(themeColor);
        //버튼 찾아주기
        mod_btn=(Button) view.findViewById(R.id.light_btn);
        mod_btn1=(Button) view.findViewById(R.id.dark_btn);
        //라이트모드 버튼을 눌렀을대
        mod_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themeColor =ThemeUtil.LIGHT_MODE;
                ThemeUtil.applyTheme(themeColor);
                ThemeUtil.modSave(getActivity().getApplicationContext(),themeColor);
            }
        });
        //다크모드 버튼을 눌렀을때
        mod_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themeColor = ThemeUtil.DARK_MODE;
                ThemeUtil.applyTheme(themeColor);
                ThemeUtil.modSave(getActivity().getApplicationContext(), themeColor);
            }
        });
        return view;
    }

    //    @Nullable
//    @Override
//    public void onCreateview(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        View view = inflater.inflate(R.layout.setting_main, container, false);
//
//        themeColor=ThemeUtil.modLoad(getActivity().getApplicationContext());
//        ThemeUtil.applyTheme(themeColor);
//        //버튼 찾아주기
//        mod_btn=getActivity().findViewById(R.id.light_btn);
//        mod_btn1=getActivity().findViewById(R.id.dark_btn);
//        //라이트모드 버튼을 눌렀을대
//        mod_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                themeColor =ThemeUtil.LIGHT_MODE;
//                ThemeUtil.applyTheme(themeColor);
//                ThemeUtil.modSave(getActivity().getApplicationContext(),themeColor);
//            }
//        });
//        //다크모드 버튼을 눌렀을때
//        mod_btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                themeColor =ThemeUtil.DARK_MODE;
//                ThemeUtil.applyTheme(themeColor);
//                ThemeUtil.modSave(getActivity().getApplicationContext(),themeColor);
//            }
//        });
//    }
}