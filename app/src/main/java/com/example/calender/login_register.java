package com.example.calender;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class login_register extends FragmentActivity {

    EditText user_id,
            user_pwd, user_pwdre,
            user_email, user_lastemail,
            user_name, user_phonenumber,
            user_age,
            user_address, user_detailaddress,
            user_zipcode;

    TextView userIDCheck_text;

    Button userIDCheck_btn, register_btn;

    Boolean IdCheck = false,
            PwCheck = false;

    View.OnClickListener btnbranch;

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
        user_lastemail = (EditText) findViewById(R.id.editText_lastemail);
        user_name = (EditText) findViewById(R.id.editText_name);
        user_phonenumber = (EditText) findViewById(R.id.editText_phonenumber);
        user_age = (EditText) findViewById(R.id.editText_age);
        user_address = (EditText) findViewById(R.id.editText_address);
        user_detailaddress = (EditText) findViewById(R.id.editText_detailaddress);
        user_zipcode = (EditText) findViewById(R.id.editText_Zipcode);

        userIDCheck_text = (TextView) findViewById(R.id.Text_IDCheck);

        btnbranch = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.userIDCheck_Btn:
                        userIdCheck();
                        break;

                    case R.id.Register_Btn:
                        Log.v("error", "회원가입");
                        userRegister();
                        break;

                }
            }
        };

        userIDCheck_btn.setOnClickListener(btnbranch);
        register_btn.setOnClickListener(btnbranch);

//         btn9 = (Button) findViewById(R.id.button9);
//        btn7.setOnClickListener(new View.OnClickListener() {
    }

    // 유저의 아이디를 중복확인 하는 부분
    public void userIdCheck(){
        String userId = user_id.getText().toString();
        String result = "";

        try{
            login_register.IDCheck idcheck = new login_register.IDCheck();

            // 아이디를 삽입
            result = idcheck.execute(userId).get();
            System.out.println(result);


        }catch (Exception e){

        }finally {
            // 반환값을 확인하고 아이디 검증여부를 설정
            if(result.equals("true")){
                userIDCheck_text.setText("사용 가능한 아이디입니다");
                IdCheck = true;
                System.out.println("ID체크 : " + IdCheck);
            }else{
                userIDCheck_text.setText("사용 불가능한 아이디입니다");
                IdCheck = false;
                System.out.println("ID체크 : " + IdCheck);
            }

        }

    }


    //  유저 정보를 담기위한 객체
    class userInfo{
        String userId;
        String userpw;
        String useremail;
        String userlastemail;
        String username;
        String userphonenumber;
        String userage;
        String useraddress;
        String userdetailaddress;
        String userzipcode;

        @NonNull
        @Override
        public String toString() {
            return "userId = " + userId
                    + " / userpw = " + userpw
                    + " / useremail = " + useremail
                    + " / userlastemail = " + userlastemail
                    + " / userlastemail = " + userlastemail
                    + " / username = " + username
                    + " / userphonenumber = " + userphonenumber
                    + " / userage = " + userage
                    + " / useraddress = " + useraddress
                    + " / userdetailaddress = " + userdetailaddress
                    + " / userzipcode = " + userzipcode;
        }
    }
//    유저의 회원가입을 실행하는 부분
    public void userRegister() {
        Boolean registerC = false;

        try{

            // 만약 Id 중복검사가 false 라면 검사를 해달라는 말을 출력
            if(!IdCheck)
                Toast.makeText(this, "아이디 중복검사를 해주세요", Toast.LENGTH_SHORT).show();

            // 입력한 정보를 newUser 객체에 삽입
            userInfo newUser = new userInfo();
            newUser.userId = user_id.getText().toString();
            newUser.userpw = user_pwd.getText().toString();
            newUser.useremail = user_email.getText().toString();
            newUser.userlastemail = user_lastemail.getText().toString();
            newUser.username = user_name.getText().toString();
            newUser.username = user_phonenumber.getText().toString();
            newUser.userage = user_age.getText().toString();
            newUser.useraddress = user_address.getText().toString();
            newUser.userdetailaddress = user_detailaddress.getText().toString();
            newUser.userzipcode = user_zipcode.getText().toString();

            // 회원가입을 누를때마다 비밀번호의 동일성 여부를 확인하고
            // 입력한 비밀번호가 일치하지 않으면 false를 반환해서 가입이 안됨
            if(newUser.userpw.equals(user_pwdre.getText().toString()))
                PwCheck = true;
            else
                PwCheck = false;

            // 입력한 데이터가 비어있는지 여부를 확인
            if(IdCheck == false || PwCheck == false)
                Toast.makeText(this, "아이디 중복검사 또는 비밀번호를 재확인해주세요", Toast.LENGTH_SHORT).show();
            else{
                Log.v("error", newUser.toString());
                if(newUser.useremail.isEmpty())
                    Log.v("error", "이메일 비어있음");
                else if (newUser.userlastemail.isEmpty())
                    Log.v("error", "이메일 뒷부분 비어있음");
                else if (newUser.userage.isEmpty())
                    Log.v("error", "나이 비어있음");
                else if (newUser.userpw.length() < 5)
                    Log.v("error", "비밀번호 길이가 안맞음");
                else{
                    registerC = true;
                    if(newUser.useraddress.isEmpty())
                        newUser.useraddress = "nothing";

                    if(newUser.userdetailaddress.isEmpty())
                        newUser.userdetailaddress = "nothing";

                    if(newUser.userzipcode.isEmpty())
                        newUser.userzipcode = "nothing";

                }

                if(registerC){
                    // 아이디 삽입을 위한 객체 생성
                    login_register.registerTask register_task = new login_register.registerTask();

                    // 아이디를 삽입
                    String result = register_task.execute(newUser.userId,
                            newUser.userpw,
                            newUser.useremail,
                            newUser.userlastemail,
                            newUser.username,
                            newUser.userphonenumber,
                            newUser.userage,
                            newUser.useraddress,
                            newUser.userdetailaddress,
                            newUser.userzipcode).get();     // 서버가 전송한 값을 String 값으로
                    Toast.makeText(this, "회원가입 완료", Toast.LENGTH_SHORT).show();
                    finish();
                } else
                    Toast.makeText(this, "필수 입력란이 비어있거나 비밀번호를 6자 이상으로 해주세요.", Toast.LENGTH_SHORT).show();
                
            }



        }catch (Exception e){
            
        }
    }

    class registerTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://210.103.48.199:3306/android/andregister");

//                URL url = new URL("http://118.235.12.28:80/andrigister");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "id=" + strings[0]
                        + "&pw=" + strings[1]
                        + "&email=" + strings[2]
                        + "&lastemail=" + strings[3]
                        + "&name=" + strings[4]
                        + "&phonenumber=" + strings[5]
                        + "&age=" + strings[6]
                        + "&address=" + strings[7]
                        + "&detailaddress=" + strings[8]
                        + "&zipcode=" + strings[9]; // GET방식으로 작성해 POST로 보냄 ex) "id=admin&pwd=1234";
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

    class IDCheck extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://210.103.48.199:3306/android/IDCheck");

//                URL url = new URL("http://118.235.12.28:80/andrigister");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "id=" + strings[0]; // GET방식으로 작성해 POST로 보냄 ex) "id=admin&pwd=1234";
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
