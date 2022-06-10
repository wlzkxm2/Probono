package com.example.calender;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.calender.DataBase.Calender_DB;
import com.example.calender.DataBase.Calender_DBSet;
import com.example.calender.DataBase.Calender_Dao;
import com.example.calender.DataBase.UserDB;
import com.example.calender.DataBase.User_DBset;
import com.example.calender.DataBase.User_Dao;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import ir.androidexception.roomdatabasebackupandrestore.Backup;
import ir.androidexception.roomdatabasebackupandrestore.Restore;

public class login extends AppCompatActivity {
    Calender_Dao calender_dao;
    User_Dao user_dao;

    Button loginbtn,registerbtn;
    EditText userID, userPassword;
    ImageButton logback;
    View.OnClickListener cl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        logback = (ImageButton) findViewById(R.id.login_back);


        cl = new View.OnClickListener() {  //뒤로가기 버튼 누르면 화면 종료되도록 설정
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.login_back:
                        finish();
                        break;
                }
            }
        };
        logback.setOnClickListener(cl);
        Calender_DBSet dbController = Room.databaseBuilder(getApplicationContext(), Calender_DBSet.class, "CalenderDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        User_DBset userdbController = Room.databaseBuilder(getApplicationContext(), User_DBset.class, "UserInfoDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        calender_dao = dbController.calender_dao();
        user_dao = userdbController.user_dao();

//        List<Calender_DB> calender_dbs = calender_dao.getAllData();
//        List<UserDB> userdb = user_dao.getAllData();        // 유저 데이터베이스


        //알림 설정 레이아웃을 표시해준다
        loginbtn = (Button) findViewById(R.id.login_btn);
        registerbtn = (Button) findViewById(R.id.Register_Btn);
        userID =(EditText)findViewById(R.id.UserID_Edit);
        userPassword =(EditText)findViewById(R.id.UserPW_Edit);
        loginbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), Navigation.class);
//                startActivity(intent);
                try{
                    login.LoginTask loginTask = new login.LoginTask();

                    String userId = userID.getText().toString();
                    String userPw = userPassword.getText().toString();

                    String result = loginTask.execute(userId, userPw).get();
                    
                    // 반환된값이 참이라면
                    if(result.isEmpty() == false){
                        Log.v("login", result);
                        loginSet(result);
//                        finish();
                        Toast.makeText(login.this, "로그인", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(login.this, "아이디또는 비밀번호가 맞지 않습니다", Toast.LENGTH_SHORT).show();

                }catch (Exception e){

                }
            }
        });//버튼을 누르면 메인화면으로 가는 명령어
        registerbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getApplicationContext(), login_register.class);
                startActivity(intent2);
            }
        });//버튼을 누르면 메인화면으로 가는 명령어
    }

    public void loginSet(String userData){
        Log.v("login", "Loginset Data :  "+ userData);
        int FO = 0, LO = 0;     // 값을 초기화
        String findresults = null;

        try{


            ArrayList<String> userDatas = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                FO = userData.indexOf("=", LO+1);
                LO = userData.indexOf(",", LO+1);

                if(i == 6){
                    LO = userData.indexOf(")");
                    findresults = userData.substring(FO+1, LO);
                }
                else
                    findresults = userData.substring(FO+1, LO);

                userDatas.add(findresults);
                Log.v("login", "finalliydata : " + userDatas.toString());
                Log.v("login", "loginFindData : " + findresults);
            }
            System.out.println("test");
            UserDB userAdd = new UserDB (userDatas.get(0).toString(),   // ID
                    userDatas.get(1).toString(),                        // PW
                    userDatas.get(2).toString(),                        // address city
                    userDatas.get(3).toString(),                        // address detail
                    userDatas.get(4).toString(),                        // zipcode
                    userDatas.get(5).toString(),                        // email
                    Integer.parseInt(userDatas.get(6).toString())      // age
            );

            user_dao.update(1, userDatas.get(0).toString(),   // ID
                    userDatas.get(1).toString(),                        // PW
                    userDatas.get(2).toString(),                        // address city
                    userDatas.get(3).toString(),                        // address detail
                    userDatas.get(4).toString(),                        // zipcode
                    userDatas.get(5).toString(),                        // email
                    Integer.parseInt(userDatas.get(6).toString())
            );     // age
//        String findresults = userData.substring(FO+1, LO);
            finish();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    class LoginTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://210.103.48.199:3306/android/login");

//                URL url = new URL("http://118.235.12.28:80/andrigister");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "id=" + strings[0]
                        + "&pw=" + strings[1]; // GET방식으로 작성해 POST로 보냄 ex) "id=admin&pwd=1234";
                osw.write(sendMsg);                           // OutputStreamWriter에 담아 전송
                osw.flush();

                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();

                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();
                } else {
                    Log.i("통신 결과", conn.getResponseMessage() + "에러");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }
            // 서버에서 보낸 값을 리턴합니다.
            return receiveMsg;
        }
    }

}