package com.example.calender;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Navigation extends AppCompatActivity {

    private FragmentManager fm;
    private FragmentTransaction ft;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation);

        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() { //NavigationItemSelecte
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                BottomNavigate(menuItem.getItemId());


                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.navi_main); // 네비게이션 첫 화면
    }
    private void BottomNavigate(int id) {  //BottomNavigation 페이지 변경
        String tag = String.valueOf(id);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment currentFragment = fragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }

        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            if (id == R.id.navi_calender) {
                fragment = new Calender_Basic_Frag();

            } else if (id == R.id.navi_main){
                fragment = new Main_Basic_Frag();

            }
            else {
                fragment = new Setting_main();
            }

            fragmentTransaction.add(R.id.content_layout, fragment, tag);
        } else {
            fragmentTransaction.show(fragment);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragment);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNow();




//        bottomNavigationView = findViewById(R.id.navi_main); // 네비게이션 아이콘을 누를시 해당 화면으로 이동
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.navi_calender:
//                        setFrag(0);
//                        break;
//                    case R.id.navi_main:
//                        setFrag(1);
//                        break;
//                    case R.id.navi_setting:
//                        setFrag(2);
//                        break;
//
//
//
//                }
//                return true;
//            }
//        });
//        Calender_Basic_Frag = new Calender_Basic_Frag();
//        Main_Basic_Frag = new Main_Basic_Frag();
//        setFrag(1); // 첫 프래그먼트 화면을 무엇으로 지정해줄 것인지 선택
//
//    }
//
//    //프래그먼트 교체가 일어나는 실행문
//    private void setFrag(int n){
//        fm = getSupportFragmentManager();
//        ft = fm.beginTransaction();
//        switch (n) {
//            case 0:
//                ft.replace(R.id.store_Frame, Calender_Basic_Frag);
//                ft.commit();
//                break;
//            case 1:
//                ft.replace(R.id.store_Frame, garden_MyGarden);
//                ft.commit();
//                break;
//            case 2:
//                ft.replace(R.id.store_Frame, mainActivity);
//                ft.commit();
//                break;
//
//
//        }
//    }


}
}
