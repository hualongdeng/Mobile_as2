package com.example.mobiledemo.data;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

public class RecordTime implements Comparable<RecordTime>{

    private int id;

    private String email;

    private String start_time;

    private String time_length;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTime_length() {
        return time_length;
    }

    public void setTime_length(String time_length) {
        this.time_length = time_length;
    }

    @Override
    public String toString() {
        return "RecordTime{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", start_time='" + start_time + '\'' +
                ", time_length='" + time_length + '\'' +
                '}';
    }

    @Override
    public int compareTo(RecordTime recordTime) {
        if(Long.parseLong(this.getStart_time())>Long.parseLong(recordTime.getStart_time())) {
            return 1;
        } else if (Long.parseLong(this.getStart_time())==Long.parseLong(recordTime.getStart_time())) {
            return 0;
        } else {
            return -1;
        }
    }
}
