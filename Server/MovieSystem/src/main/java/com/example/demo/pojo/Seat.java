package com.example.demo.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Seat {
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
    private Integer seatId;

    private Integer screeningroomId;

    private String seatLocation;

    private String seatName;

    private Integer seatStatus;

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