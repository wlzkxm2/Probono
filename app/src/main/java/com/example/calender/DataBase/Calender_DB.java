package com.example.calender.DataBase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// 엔티티
@Entity
public class Calender_DB {
    @PrimaryKey // pk 
    public int uid;

    @ColumnInfo(name = "FirstInputData")
    public boolean _firstData;
    
    // ColumnInfo 를 통해서 DB내부 엔티티 이름을 따로 설정
    @ColumnInfo(name = "months")
    public int _month;
    
    @ColumnInfo(name = "days")
    public int _day;

    @ColumnInfo(name = "years")
    public int _years;
    
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

    //</editor-fold desc="DB Getter & Setter">

}
