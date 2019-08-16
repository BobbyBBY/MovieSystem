package com.example.moviesystemclient.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Screeningroom {
    private Integer screeningroomId;

    private String screeningroomName;

    private Integer screeningroomStatus;

    public Integer getScreeningroomId() {
        return screeningroomId;
    }

    public void setScreeningroomId(Integer screeningroomId) {
        this.screeningroomId = screeningroomId;
    }

    public String getScreeningroomName() {
        return screeningroomName;
    }

    public void setScreeningroomName(String screeningroomName) {
        this.screeningroomName = screeningroomName == null ? null : screeningroomName.trim();
    }

    public Integer getScreeningroomStatus() {
        return screeningroomStatus;
    }

    public void setScreeningroomStatus(Integer screeningroomStatus) {
        this.screeningroomStatus = screeningroomStatus;
    }
}