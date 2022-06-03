package com.example.calender;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class login_register extends FragmentActivity implements View.OnClickListener{

    EditText user_id,
            user_pwd, user_pwdre,
            user_email,
            user_age,
            user_address, user_detailaddress,
            user_zipcode;

    TextView userIDCheck_text;

    Button userIDCheck_btn, register_btn;

    Boolean IdCheck = true,
            PWCheck = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register);
//        알림 설정 레이아웃을 표시해준다

        userIDCheck_btn = (Button) findViewById(R.id.userIDCheck_Btn);
        register_btn = (Button) findViewById(R.id.Register_Btn);

        user_id = (EditText) findViewById(R.id.editText_ID);
        user_pwd = (EditText) findViewById(R.id.editText_userpw);
        user_pwdre = (EditText) findViewById(R.id.editText_usrpw_re);
        user_email = (EditText) findViewById(R.id.editText_email);
        user_age = (EditText) findViewById(R.id.editText_age);
        user_address = (EditText) findViewById(R.id.editText_address);
        user_detailaddress = (EditText) findViewById(R.id.editText_detailaddress);
        user_zipcode = (EditText) findViewById(R.id.editText_Zipcode);

        userIDCheck_btn.setOnClickListener(this);
        register_btn.setOnClickListener(this);

//         btn9 = (Button) findViewById(R.id.button9);
//        btn7.setOnClickListener(new View.OnClickListener() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.userIDCheck_Btn:
                userIdCheck();
                break;
                
            case R.id.Register_Btn:
                userRegister();
                break;
        }
    }
    
    
    // 유저의 아이디를 중복확인 하는 부분
    public void userIdCheck(){
        
    }


    //  유저 정보를 담기위한 객체
    class userInfo{
        String userId;
        String userpw;
        String useremail;
        String userage;
        String useraddress;
        String userdetailaddress;
        String userzipcode;
    }
//    유저의 회원가입을 실행하는 부분
    public void userRegister() {


        try{

            userInfo newUser = new userInfo();
            newUser.userId = user_id.getText().toString();
            newUser.userpw = user_pwd.getText().toString();
            newUser.useremail = user_email.getText().toString();
            newUser.userage = user_age.getText().toString();
            newUser.useraddress = user_address.getText().toString();
            newUser.userdetailaddress = user_detailaddress.getText().toString();
            newUser.userzipcode = user_zipcode.getText().toString();

            login_register.registerTask register_task = new login_register.registerTask();

            String result = register_task.execute(newUser.userId,
                    newUser.userpw,
                    newUser.useremail,
                    newUser.userage,
                    newUser.useraddress,
                    newUser.userdetailaddress,
                    newUser.userzipcode).get();     // 서버가 전송한 값을 String 값으로
//            // 아이디 체크가 false 라면 아이디 중복검사가 필요함
//            if(!IdCheck)
//                Toast.makeText(this, "아이디 중복검사를 해주세요", Toast.LENGTH_SHORT).show();
//
//                // 패스워드가 두개 전부 제대로 입력이 됫다면 true 아니면 false
//            if(user_pwd.equals(user_pwdre))
//                PWCheck = true;
//            else
//                PWCheck = false;
//
//            if(IdCheck){
//                if(PWCheck){
//
//                    // 유저 객체 생성후 데이터 삽입
//                    userInfo newUser = new userInfo();
//                    newUser.userId = user_id.getText().toString();
//                    newUser.userpw = user_pwd.getText().toString();
//                    newUser.useremail = user_email.getText().toString();
//                    newUser.userage = user_age.getText().toString();
//                    newUser.useraddress = user_address.getText().toString();
//                    newUser.userdetailaddress = user_detailaddress.getText().toString();
//                    newUser.userzipcode = user_zipcode.getText().toString();
//
//                    login_register.registerTask register_task = new login_register.registerTask();
//
//                    String result = register_task.execute(newUser.userId,
//                            newUser.userpw,
//                            newUser.useremail,
//                            newUser.userage,
//                            newUser.useraddress,
//                            newUser.userdetailaddress,
//                            newUser.userzipcode).get();     // 서버가 전송한 값을 String 값으로
//
//                }
//
//            }


        }catch (Exception e){
            
        }
    }

    class registerTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://210.103.48.199:3306/andrigister");

//                URL url = new URL("http://118.235.12.28:80/andrigister");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "id=" + strings[0]
                        + "&pw=" + strings[1]
                        + "&email" + strings[2]
                        + "&age=" + strings[3]
                        + "&address=" + strings[4]
                        + "&detailaddress=" + strings[5]
                        + "&zipcode=" + strings[6]; // GET방식으로 작성해 POST로 보냄 ex) "id=admin&pwd=1234";
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
