package com.example.calender.StaticUidCode;

import android.app.Application;

// 앱 전역변수
public class UidCode extends Application {
    private int UserCode;

    private int static_year;
    private int static_month;
    private int static_day;
    private int time;

    public int getStatic_year() {
        return static_year;
    }

    public void setStatic_year(int static_year) {
        this.static_year = static_year;
    }

    public int getStatic_month() {
        return static_month;
    }

    public void setStatic_month(int static_month) {
        this.static_month = static_month;
    }

    public int getStatic_day() {
        return static_day;
    }

    public void setStatic_day(int static_day) {
        this.static_day = static_day;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
    public int getUserCode() {
        return UserCode;
    }

    public void setUserCode(int userCode) {
        UserCode = userCode;
    }
}
