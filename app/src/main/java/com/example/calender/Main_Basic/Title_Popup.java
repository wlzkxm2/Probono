package com.example.calender.Main_Basic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.calender.R;

public class Title_Popup extends Activity {

    EditText edit_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.title_popup);

        //UI 객체생성
        edit_title = (EditText)findViewById(R.id.edit_title);

        //데이터 가져오기
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        edit_title.setText(data);
    }

    //확인 버튼 클릭
    public void Confirm(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", edit_title.getText().toString());
        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
    }

    //취소 버튼 클릭
    public void Cancel(View v){
        //액티비티(팝업) 닫기
        finish();
    }
}