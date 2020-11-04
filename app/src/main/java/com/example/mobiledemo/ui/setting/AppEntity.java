package com.example.mobiledemo.ui.setting;

import java.io.Serializable;
import java.time.LocalDateTime;

public class AppEntity implements Serializable {
    private String app_name;
    private int blocked;

    public AppEntity(String name, int blocked) {
        this.app_name = name;
        this.blocked = blocked;
    }

    public String getAppName() {
        return app_name;
    }
    public int getAppBlocked() { return  blocked; }
    public void setBlocked (int blocked) { this.blocked = blocked; }
}