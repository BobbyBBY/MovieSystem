package com.example.moviesystemclient.bean;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketView {
    private String ticketId;

    private Integer ticketStatus;

    private String seatName;

    private String screeningroomName;

    private Double screeningPrice;

    private Date screeningStarttime;

    private Integer screeningSpecialeffect;

    private String movieName;

    private Integer movieDuration;

    private String orderId;

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId == null ? null : ticketId.trim();
    }

    public Integer getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(Integer ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getSeatName() {
        return seatName;
    }

    public void setSeatName(String seatName) {
        this.seatName = seatName == null ? null : seatName.trim();
    }

    public String getScreeningroomName() {
        return screeningroomName;
    }

    public void setScreeningroomName(String screeningroomName) {
        this.screeningroomName = screeningroomName == null ? null : screeningroomName.trim();
    }

    public Double getScreeningPrice() {
        return screeningPrice;
    }

    public void setScreeningPrice(Double screeningPrice) {
        this.screeningPrice = screeningPrice;
    }

    public Date getScreeningStarttime() {
        return screeningStarttime;
    }

    public void setScreeningStarttime(Date screeningStarttime) {
        this.screeningStarttime = screeningStarttime;
    }

    public Integer getScreeningSpecialeffect() {
        return screeningSpecialeffect;
    }

    public void setScreeningSpecialeffect(Integer screeningSpecialeffect) {
        this.screeningSpecialeffect = screeningSpecialeffect;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName == null ? null : movieName.trim();
    }

    public Integer getMovieDuration() {
        return movieDuration;
    }

    public void setMovieDuration(Integer movieDuration) {
        this.movieDuration = movieDuration;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }
}