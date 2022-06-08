package com.example.calender.StaticUidCode;

import android.app.Application;

// 앱 전역변수
public class UidCode extends Application {
    // 유저 코드에 대한 전역변수
    private int UserCode;

    // 달력에서 선택한 날짜에 대한 전역변수
    private int static_year;
    private int static_month;
    private int static_day;
    private int time;
    private String week;

    // 시작 날짜
    private int start_years;
    private int start_months;
    private int start_days;
    private int start_times;

    // 마지막 날짜
    private int end_years;
    private int end_months;
    private int end_days;
    private int end_times;

    public int getStart_years() {
        return start_years;
    }

    public void setStart_years(int start_years) {
        this.start_years = start_years;
    }

    public int getStart_months() {
        return start_months;
    }

    public void setStart_months(int start_months) {
        this.start_months = start_months;
    }

    public int getStart_days() {
        return start_days;
    }

    public void setStart_days(int start_days) {
        this.start_days = start_days;
    }

    public int getStart_times() {
        return start_times;
    }

    public void setStart_times(int start_times) {
        this.start_times = start_times;
    }

    public int getEnd_years() {
        return end_years;
    }

    public void setEnd_years(int end_years) {
        this.end_years = end_years;
    }

    public int getEnd_months() {
        return end_months;
    }

    public void setEnd_months(int end_months) {
        this.end_months = end_months;
    }

    public int getEnd_days() {
        return end_days;
    }

    public void setEnd_days(int end_days) {
        this.end_days = end_days;
    }

    public int getEnd_times() {
        return end_times;
    }

    public void setEnd_times(int end_times) {
        this.end_times = end_times;
    }

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

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

}
