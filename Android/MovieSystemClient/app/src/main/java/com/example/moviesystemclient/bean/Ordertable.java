package com.example.moviesystemclient.bean;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ordertable {
    private String orderId;

    private String audiencePhone;

    private Date orderGeneratedtime;

    private Double orderTotalprice;

    private Integer orderStatus;

    private Integer orderTicketcount;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getAudiencePhone() {
        return audiencePhone;
    }

    public void setAudiencePhone(String audiencePhone) {
        this.audiencePhone = audiencePhone == null ? null : audiencePhone.trim();
    }

    public Date getOrderGeneratedtime() {
        return orderGeneratedtime;
    }

    public void setOrderGeneratedtime(Date orderGeneratedtime) {
        this.orderGeneratedtime = orderGeneratedtime;
    }

    public Double getOrderTotalprice() {
        return orderTotalprice;
    }

    public void setOrderTotalprice(Double orderTotalprice) {
        this.orderTotalprice = orderTotalprice;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getOrderTicketcount() {
        return orderTicketcount;
    }

    public void setOrderTicketcount(Integer orderTicketcount) {
        this.orderTicketcount = orderTicketcount;
    }
}