package com.example.calender.DataBase;

import androidx.annotation.NonNull;
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
    @ColumnInfo(name = "start_years")
    public int start_years;

    @ColumnInfo(name = "start_months")
    public int start_month;

    @ColumnInfo(name = "start_days")
    public int start_day;

    @ColumnInfo(name = "start_times")
    public int start_time;

    @ColumnInfo(name = "end_years")
    public int end_years;

    @ColumnInfo(name = "end_months")
    public int end_month;

    @ColumnInfo(name = "end_days")
    public int end_day;

    @ColumnInfo(name = "end_times")
    public int end_time;

    @ColumnInfo(name = "Titles")
    public String _titles;

    @ColumnInfo(name = "Subtitles")
    public String _subtitle;

    @ColumnInfo(name = "MainActTitle")
    public String _mainActTitle;

    @ColumnInfo(name = "MainActDay")
    public long _mainActTime;

    @ColumnInfo(name = "MainActDTitle")
    public String _mainActDTitle;

    @ColumnInfo(name = "CalanderCategory")
    public int _calanderCategory;

    @ColumnInfo(name = "FinishdQuest")
    public boolean _finishedQuest;

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

    public int getStart_years() {
        return start_years;
    }

    public void setStart_years(int start_years) {
        this.start_years = start_years;
    }

    public int getStart_month() {
        return start_month;
    }

    public void setStart_month(int start_month) {
        this.start_month = start_month;
    }

    public int getStart_day() {
        return start_day;
    }

    public void setStart_day(int start_day) {
        this.start_day = start_day;
    }

    public int getStart_time() {
        return start_time;
    }

    public void setStart_time(int start_time) {
        this.start_time = start_time;
    }

    public int getEnd_years() {
        return end_years;
    }

    public void setEnd_years(int end_years) {
        this.end_years = end_years;
    }

    public int getEnd_month() {
        return end_month;
    }

    public void setEnd_month(int end_month) {
        this.end_month = end_month;
    }

    public int getEnd_day() {
        return end_day;
    }

    public void setEnd_day(int end_day) {
        this.end_day = end_day;
    }

    public int getEnd_time() {
        return end_time;
    }

    public void setEnd_time(int end_time) {
        this.end_time = end_time;
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

    public String get_mainActTitle() {
        return _mainActTitle;
    }

    public void set_mainActTitle(String _mainActTitle) {
        this._mainActTitle = _mainActTitle;
    }

    public long get_mainActTime() {
        return _mainActTime;
    }

    public void set_mainActTime(long _mainActTime) {
        this._mainActTime = _mainActTime;
    }

    public String get_mainActDTitle() {
        return _mainActDTitle;
    }

    public void set_mainActDTitle(String _mainActDTitle) {
        this._mainActDTitle = _mainActDTitle;
    }

    public int get_calanderCategory() {
        return _calanderCategory;
    }

    public void set_calanderCategory(int _calanderCategory) {
        this._calanderCategory = _calanderCategory;
    }

    public boolean get_finishedQuest() {
        return _finishedQuest;
    }

    public void set_finishedQuest(boolean _finishedQuest) {
        this._finishedQuest = _finishedQuest;
    }



    //</editor-fold desc="DB Getter & Setter">
}
