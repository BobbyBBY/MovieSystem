package com.example.moviesystemclient.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ticket {
    private String ticketId;

    private Integer seatId;

    private Integer screeningId;

    private String orderId;

    private Integer ticketStatus;

    public Ticket(Integer seatId, Integer screeningId, String orderId, Integer ticketStatus) {
        this.seatId = seatId;
        this.screeningId = screeningId;
        this.orderId = orderId;
        this.ticketStatus = ticketStatus;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId == null ? null : ticketId.trim();
    }

    public Integer getSeatId() {
        return seatId;
    }

    public void setSeatId(Integer seatId) {
        this.seatId = seatId;
    }

    public Integer getScreeningId() {
        return screeningId;
    }

    public void setScreeningId(Integer screeningId) {
        this.screeningId = screeningId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public Integer getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(Integer ticketStatus) {
        this.ticketStatus = ticketStatus;
    }
}