package com.example.calender.StaticUidCode;

import android.app.Application;

// 앱 전역변수
public class UidCode extends Application {
    private int UserCode;

    public int getUserCode() {
        return UserCode;
    }

    public void setUserCode(int userCode) {
        UserCode = userCode;
    }
}
