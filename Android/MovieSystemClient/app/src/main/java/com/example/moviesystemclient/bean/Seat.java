package com.example.moviesystemclient.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Seat {
    private Integer seatId;

    private Integer screeningroomId;

    private String seatLocation;

    private String seatName;

    private Integer seatStatus;

    public Seat(){

    }

    public Seat(int seatId, int screeningroomId){
        this.screeningroomId = screeningroomId;
        this.seatId = seatId;
    }

    public Seat(int screeningroomId, String seatLocation, String seatName, int seatStatus){
        this.screeningroomId = screeningroomId;
        this.seatLocation  = seatLocation;
        this.seatName = seatName;
        this.seatStatus = seatStatus;
    }

    public Integer getSeatId() {
        return seatId;
    }

    public void setSeatId(Integer seatId) {
        this.seatId = seatId;
    }

    public Integer getScreeningroomId() {
        return screeningroomId;
    }

    public void setScreeningroomId(Integer screeningroomId) {
        this.screeningroomId = screeningroomId;
    }

    public String getSeatLocation() {
        return seatLocation;
    }

    public void setSeatLocation(String seatLocation) {
        this.seatLocation = seatLocation == null ? null : seatLocation.trim();
    }

    public String getSeatName() {
        return seatName;
    }

    public void setSeatName(String seatName) {
        this.seatName = seatName == null ? null : seatName.trim();
    }

    public Integer getSeatStatus() {
        return seatStatus;
    }

    public void setSeatStatus(Integer seatStatus) {
        this.seatStatus = seatStatus;
    }
}