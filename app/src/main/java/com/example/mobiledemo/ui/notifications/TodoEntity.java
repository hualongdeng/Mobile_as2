package com.example.mobiledemo.ui.notifications;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Calendar;

public class TodoEntity implements Serializable {
    public int id;
    public LocalDateTime start_time;
    public LocalDateTime end_time;
    public String title;
    public String email;
    public String place;
    public int remind;
    public int repeat;

    public TodoEntity(int id, LocalDateTime start_time, LocalDateTime end_time, String title, String email, String place, int remind, int repeat) {
        this.id = id;
        this.start_time = start_time;
        this.end_time = end_time;
        this.title = title;
        this.email = email;
        this.place = place;
        this.remind = remind;
        this.repeat = repeat;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int getId() {
        return id;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getStart_time() {
        String hour1;
        String min1;
        if (start_time.getHour() < 10) {
            hour1 = "0" + start_time.getHour();
        } else {
            hour1 = String.valueOf(start_time.getHour());
        }
        if (start_time.getMinute() < 10) {
            min1 = "0" + start_time.getMinute();
        } else {
            min1 = String.valueOf(start_time.getMinute());
        }
        return hour1 + ":" + min1 + ":00" ;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getEnd_time() {
        String hour1;
        String min1;
        if (end_time.getHour() < 10) {
            hour1 = "0" + end_time.getHour();
        } else {
            hour1 = String.valueOf(end_time.getHour());
        }
        if (end_time.getMinute() < 10) {
            min1 = "0" + end_time.getMinute();
        } else {
            min1 = String.valueOf(end_time.getMinute());
        }
        return hour1 + ":" + min1 + ":00" ;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getStart_whole_time() {
        return start_time.getYear() + "-" + start_time.getMonthValue() + "-" + start_time.getDayOfMonth() + "  " + start_time.getHour() + " : : " + start_time.getMinute();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getEnd_whole_time() {
        return end_time.getYear() + "-" + end_time.getMonthValue() + "-" + end_time.getDayOfMonth() + "  " + end_time.getHour() + " : : " + end_time.getMinute();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int getStartYear() {
        return start_time.getYear();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int getStartMonth() {
        return start_time.getMonthValue();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int getStartDay() {
        return start_time.getDayOfMonth();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int getStartHour() {
        return start_time.getHour();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int getStartMin() {
        return start_time.getMinute();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int getEndYear() {
        return end_time.getYear();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int getEndMonth() {
        return end_time.getMonthValue();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int getEndDay() {
        return end_time.getDayOfMonth();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int getEndHour() {
        return end_time.getHour();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int getEndMin() {
        return end_time.getMinute();
    }

    public String getTitle() {
        return title;
    }

    public String getEmail() {
        return email;
    }

    public String getPlace() {
        return place;
    }

    public int getRemind() {
        return remind;
    }

    public int getRepeat() {
        return repeat;
    }
}

