package com.example.calender;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.calender.Main_Basic.Main_Basic_Frag;
import com.example.calender.Permission.Permission;
import com.example.calender.setting.Setting_main;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import io.reactivex.rxjava3.annotations.NonNull;

public class nav1 extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Main_Basic_Frag main_basic_frag;
    private Calender_Basic_Frag calender_basic_frag;
    private Setting_main setting_main;

    private Permission permission;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation);

        permissionCheck();

        bottomNavigationView = findViewById(R.id.navi_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navi_calender:
                        setFrag(0);
                        break;
                    case R.id.navi_main:
                        setFrag(1);
                        break;
                    case R.id.navi_setting:
                        setFrag(2);
                        break;
                }
                return true;
            }
        });

        calender_basic_frag = new Calender_Basic_Frag();
        main_basic_frag = new Main_Basic_Frag();
        setting_main = new Setting_main();
        setFrag(2); // ??? ????????? ??? ??????????????? ?????? ??????

    }

    // ??????????????? ??????
    private void setFrag(int n)
    {
        fm = getSupportFragmentManager();
        ft= fm.beginTransaction();
        switch (n)
        {
            case 0:
                ft.replace(R.id.navi_fragment_main,calender_basic_frag);
                ft.commit();
                break;

            case 1:
                ft.replace(R.id.navi_fragment_main,main_basic_frag);
                ft.commit();
                break;

            case 2:
                ft.replace(R.id.navi_fragment_main,setting_main);
                ft.commit();
                break;

        }
    }

    void permissionCheck() {
        // PermissionSupport.java ????????? ?????? ??????
        permission = new Permission(this, this);

        // ?????? ?????? ??? ????????? false??? ????????????
        if (!permission.checkPermission()){
            //?????? ??????
            permission.requestPermission();
        }
    }
    // Request Permission??? ?????? ?????? ??? ?????????
    @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull String[] permissions, @androidx.annotation.NonNull int[] grantResults) {
        //???????????? ????????? false??? ??????????????? (???????????? ?????? ?????? ??????)
        if (!permission.permissionResult(requestCode, permissions, grantResults)) {
            // ?????? permission ??????
            permission.requestPermission();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}

