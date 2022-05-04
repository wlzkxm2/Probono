package com.example.calender.DataBase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

// 엔티티
@Entity
public class Calender_DB {
    @PrimaryKey // pk
    @NotNull
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
    
    @ColumnInfo(name = "datas")
    public String _data;


//<editor-fold desc="DB Getter & Setter">

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

    public String get_data() {
        return _data;
    }

    public void set_data(String _data) {
        this._data = _data;
    }

    public int get_time() {
        return _time;
    }

    public void set_time(int _time) {
        this._time = _time;
    }

    //</editor-fold desc="DB Getter & Setter">

}
