package com.example.mobiledemo.data;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

@Table("recordtime")
public class recordTime {

    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;

    @Column("starttime")
    private long startTime;

    @Column("timelength")
    private long timeLength;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(long timeLength) {
        this.timeLength = timeLength;
    }

    @Override
    public String toString() {
        return "recordTime{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", timeLength=" + timeLength +
                '}';
    }
}
