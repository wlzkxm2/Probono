package com.example.calender.DataBase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

// 엔티티
@Entity
public class Calender_DB {

    @PrimaryKey(autoGenerate = true) // pk
    public int num;

    @ColumnInfo(name = "UserIDCode")
    public int uid;

    @ColumnInfo(name = "FirstInputData")
    public boolean _firstData;

    // ColumnInfo 를 통해서 DB내부 엔티티 이름을 따로 설정
    @ColumnInfo(name = "years")
    public int _years;

    @ColumnInfo(name = "months")
    public int _month;

    @ColumnInfo(name = "days")
    public int _day;

    @ColumnInfo(name = "times")
    public int _time;

    @ColumnInfo(name = "Titles")
    public String _titles;

    @ColumnInfo(name = "Subtitles")
    public String _subtitle;


//<editor-fold desc="DB Getter & Setter">


    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public boolean is_firstData() {
        return _firstData;
    }

    public void set_firstData(boolean _firstData) {
        this._firstData = _firstData;
    }

    public int get_month() {
        return _month;
    }

    public void set_month(int _month) {
        this._month = _month;
    }

    public int get_day() {
        return _day;
    }

    public void set_day(int _day) {
        this._day = _day;
    }

    public int get_years() {
        return _years;
    }

    public void set_years(int _years) {
        this._years = _years;
    }

    public int get_time() {
        return _time;
    }

    public void set_time(int _time) {
        this._time = _time;
    }

    public String get_titles() {
        return _titles;
    }

    public void set_titles(String _titles) {
        this._titles = _titles;
    }

    public String get_subtitle() {
        return _subtitle;
    }

    public void set_subtitle(String _subtitle) {
        this._subtitle = _subtitle;
    }

    //</editor-fold desc="DB Getter & Setter">

}
