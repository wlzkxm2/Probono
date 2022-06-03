package com.example.calender.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.calender.R;

public class Setting_account extends AppCompatActivity {
    Button back;
    View.OnClickListener cl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_account);
        //알림 설정 레이아웃을 표시

        back = (Button) findViewById(R.id.accountback);


        cl = new View.OnClickListener() {  //뒤로가기 버튼 누르면 화면 종료되도록 설정
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.accountback:
                        finish();
                        break;
                }
            }
        };
        back.setOnClickListener(cl);

    }
//    void showDialog() {
//        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(Setting_account.this)//팝업창 생성
//
//                .setIcon(R.drawable.exam)
//                .setTitle("로그아웃")//쿠폰의 타이틀
//                .setMessage("로그아웃 하시겠습니까???")//쿠폰의 메세지
//                .setPositiveButton("완료", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(Setting_account.this,"로그아웃 됐습니다", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(Setting_account.this,login.class);
//                        startActivity(intent);
//                    }//긍정 버튼 눌를때 나오는 텍스트
//                })
//                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(Setting_account.this, "취소되었습니다", Toast.LENGTH_SHORT).show();
//                    }//부정 버튼 눌를때 나오는 텍스트
//                });
//        AlertDialog msgDlg = msgBuilder.create();
//        msgDlg.show();
//    }
}