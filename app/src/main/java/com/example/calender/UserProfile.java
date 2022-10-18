package com.example.calender;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import com.example.calender.DataBase.Calender_DBSet;
import com.example.calender.DataBase.Calender_Dao;
import com.example.calender.DataBase.UserDB;
import com.example.calender.DataBase.User_DBset;
import com.example.calender.DataBase.User_Dao;
import com.example.calender.FileUploadCode.FileUploadUtils;

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
import java.util.List;

import ir.androidexception.roomdatabasebackupandrestore.Backup;
import ir.androidexception.roomdatabasebackupandrestore.Restore;

public class UserProfile extends AppCompatActivity implements View.OnClickListener{

    Calender_Dao calender_dao;
    User_Dao user_dao;

    TextView userid, useremail, username,
            userage, userphonenum, useraddress;

    TextView syncUpload, syncDownload;

    File tempSelectFile;

    Button userLogout;

    private Calender_DBSet database;

    String userId = "";
    String resultFileName = null;

    // 파일을 받는 함수들
//    "http://210.103.48.199:3306/api/download"    // 웹서버 파일 경로
    private File outputFile; //파일명까지 포함한 경로
    private File path;//디렉토리경로
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userprofile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = Room.databaseBuilder(getApplicationContext(),
                        Calender_DBSet.class, "CalenderDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();


        User_DBset userdbController = Room.databaseBuilder(getApplicationContext(), User_DBset.class, "UserInfoDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        user_dao = userdbController.user_dao();

        // 다운로드 프로그래스바바
        progressBar = new ProgressDialog(UserProfile.this);
        progressBar.setMessage("다운로드중");
        progressBar.setIndeterminate(true);
        progressBar.setCancelable(true);

        userid = (TextView) findViewById(R.id.UserID_text);
        useremail = (TextView) findViewById(R.id.contextUserEmail_text);
        username = (TextView) findViewById(R.id.UserName_text);
        userage = (TextView) findViewById(R.id.UserAge_text);
        userphonenum = (TextView) findViewById(R.id.UserPhonNumber_text);
        useraddress = (TextView) findViewById(R.id.Useraddress_text);

        syncUpload = (TextView) findViewById(R.id.uploadFile);
        syncDownload = (TextView) findViewById(R.id.downloadFile);

        userLogout = (Button) findViewById(R.id.btn_userlogout);

        List<UserDB> callUserInfo = user_dao.getAllData();

        userId = callUserInfo.get(0).getId().toString();

        userid.setText(callUserInfo.get(0).getId().toString());
        useremail.setText(callUserInfo.get(0).getEmail().toString());
        userage.setText(Integer.toString(callUserInfo.get(0).getAge()));
        useraddress.setText(callUserInfo.get(0).getAddress().toString() + "\n" +
                callUserInfo.get(0).getAddressDetail().toString() + "\n" +
                callUserInfo.get(0).getZipcode().toString());

        syncUpload.setOnClickListener(this);
        syncDownload.setOnClickListener(this);
        userLogout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.uploadFile:

                List<UserDB> callUserInfo = user_dao.getAllData();
                Log.v("profile", "프로필 동기화");
                try {
                    SendIdTasks sid = new SendIdTasks();
                    resultFileName = sid.execute(userId).get();
                    Log.v("profile", "받은 파일 이름 : " + resultFileName);
                    // 그럼 먼저 DB를 백업
                    BackUpDB(this);
                    // 그후 백업한 DB를 업로드


                }catch (Exception e){

                }


                break;
            case R.id.downloadFile:

                List<UserDB> callUserInfos = user_dao.getAllData();
                Log.v("profile", "프로필 동기화");

                try {
                    SendIdTasks sid = new SendIdTasks();
                    resultFileName = sid.execute(userId).get();
                    Log.v("profile", "받은 파일 이름 : " + resultFileName);

                    // 서버의 파일을 다운로드
                    DownloadServerFiles();


                }catch (Exception e){

                }
                break;

            case R.id.btn_userlogout:
                user_dao.deleteAll(1);
                finish();
                break;
        }
    }

    public void BackUpDB(Context context) {
        new Backup.Init()
                .database(database)
                .path(getFilesDir().getPath())
                .fileName(userId + "_DB.txt")
                .secretKey("probono")
                .onWorkFinishListener((success, message) -> Toast.makeText(UserProfile.this, message, Toast.LENGTH_SHORT).show())
                .execute();

        UploadServerFiles();
    }

    public void RestoreDB() {
        new Restore.Init()
                .database(database)
                .backupFilePath(getFilesDir() + "/DownloadDB/" + "sDB.txt")
                .secretKey("probono")
                .onWorkFinishListener((success, message) -> {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                })
                .execute();

    }

    public void UploadServerFiles() {
        try {
            UploadTasks task = new UploadTasks();
            String result = task.execute(userId).get();
            tempSelectFile = new File(getFilesDir() + File.separator + (userId + "_DB.txt"));
            FileUploadUtils.send2Server(tempSelectFile);
            String findresults = result;

        } catch (Exception e) {

        }
    }

    public void DownloadServerFiles(){
        // 폴더가 없으면 만드는 함수
        String folderName = "/DownloadDB";
        File dir = new File(getFilesDir() + folderName);
        if(!dir.exists()){
            dir.mkdirs();
        }

        try{
            SendIdTasks sid = new SendIdTasks();
            resultFileName = sid.execute(userId).get();
            Log.v("error", "받은 파일 이름값 : " + resultFileName);
        }catch (Exception e){

        }

        // 웹브라우저에 링크를 입력하면 다운되는 함수
        final String fileURL = "http://13.125.150.245:8081/CloudDownload";
//        final String fileURL = "http://210.103.48.199:3306/api/download";
        path = new File(getFilesDir() + folderName);
        outputFile = new File(path, "sDB.txt"); // 파일명을 포함한 경로

        if(outputFile.exists()){        // 이미 파일이 다운로드 되어있을때
            AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
            builder.setTitle("데이터 다운로드");
            builder.setMessage("이미 데이터가 존재합니다 다시 받을까요?");
            builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(UserProfile.this, "다운로드를 취소 했습니다", Toast.LENGTH_SHORT).show();
                }
            });
            String finalResultFileName = resultFileName;
            builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    outputFile.delete();        // 파일 삭제
                    final DownloadFilesTask downloadTask = new DownloadFilesTask(UserProfile.this);
                    downloadTask.execute(fileURL + "/" + finalResultFileName);

                    progressBar.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            downloadTask.cancel(true);
                        }
                    });
                }
            });
            builder.show();
        }else{  // 새로 받는 경우
            final DownloadFilesTask downloadTask = new DownloadFilesTask(UserProfile.this);
            downloadTask.execute(fileURL + "/" + resultFileName);

            progressBar.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    downloadTask.cancel(true);
                }
            });

        }


    }

    class DownloadFilesTask extends AsyncTask<String, String, Long> {
        private Context context;
        private PowerManager.WakeLock mWakeLock;
        String folderName = "/DownloadDB";

        public DownloadFilesTask(Context context){
            this.context = context;

            Log.v("error", "DownloadFilesTask");
        }

        // 파일 다운로드 전에 프로그레스바 출력
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
            mWakeLock.acquire();

            progressBar.show();
        }

        // 파일 다운로드 진행
        @Override
        protected Long doInBackground(String... string_url) {
            int count;
            long FileSize = -1;
            InputStream input = null;
            OutputStream output = null;
            URLConnection connection = null;
            Log.v("error", "doIn");

            try{

                URL url = new URL(string_url[0]);
                connection = url.openConnection();
                connection.connect();

                FileSize = connection.getContentLength();
                input = new BufferedInputStream(url.openStream(), 8192);
                path = new File(getFilesDir() + folderName);
                outputFile = new File(path, "sDB.txt");

                output = new FileOutputStream(outputFile);  //  파일 저장

                byte data[] = new byte[1024];
                long downloadedSize = 0;
                while((count = input.read(data)) != -1){
                    // 사용자가 뒤로가기 누르면 취소
                    if (isCancelled()){
                        input.close();
                        return Long.valueOf(-1);
                    }

                    downloadedSize += count;

                    if(FileSize > 0){
                        float per = ((float) downloadedSize/FileSize) * 100;
                        String str = "Downloaded" + downloadedSize + "KB/" + FileSize + "KB (" + (int)per + "%)";
                        publishProgress("" + (int) ((downloadedSize * 100) / FileSize), str);
                    }
                    // 파일에 데이터를 기록
                    output.write(data, 0, count);
                }

                //Flush output
                output.flush();

                // close Streams
                output.close();
                input.close();
            }catch (Exception e){
                Log.v("error", e.getMessage());

            }finally {
                try{
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();

                }catch (IOException ignored){

                }
                mWakeLock.release();
            }

            return FileSize;
        }

        // 파일 다운로드중 프로그래스바 업데이트
        @Override
        protected void onProgressUpdate(String... progress) {
            super.onProgressUpdate(progress);

            progressBar.setIndeterminate(false);
            progressBar.setMax(100);
            progressBar.setProgress(Integer.parseInt(progress[0]));
            progressBar.setMessage(progress[1]);
        }

        // 파일 다운로드 완료
        @Override
        protected void onPostExecute(Long size) {
            super.onPostExecute(size);

            progressBar.dismiss();

            if(size > -1){
                Toast.makeText(context, "다운로드 완료되었습니다. 파일 크기=" + size.toString(), Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(context, "다운로드 크기 = " + size.toString(), Toast.LENGTH_SHORT).show();
            }

            RestoreDB();
        }
    }


    class UploadTasks extends AsyncTask<String, Void, String> {

        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {

                String str;
//                URL url = new URL("http://210.103.48.199:3306/upload");
                URL url = new URL("http://13.125.150.245:8081/testService");
//                URL url = new URL("http://118.235.12.28:80/android");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");                              //데이터를 POST 방식으로 전송합니다.
                conn.setDoOutput(true);

                // 서버에 보낼 값 포함해 요청함.
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

    class SendIdTasks extends AsyncTask<String, Void, String> {

        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {

                String str;
//                URL url = new URL("http://210.103.48.199:3306/api/datacheak");
                URL url = new URL("http://13.125.150.245:8081/api/datacheak");
//                URL url = new URL("http://118.235.12.28:80/android");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");                              //데이터를 POST 방식으로 전송합니다.
                conn.setDoOutput(true);

                // 서버에 보낼 값 포함해 요청함.
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
