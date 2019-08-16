package com.example.demo.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Screeningroom {
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
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