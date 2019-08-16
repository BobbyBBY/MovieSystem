package com.example.moviesystemmanager.bean;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Audience {
    private String audiencePhone;

    private Integer audienceVipstatus;

    private Integer vipId;

    public String getAudiencePhone() {
        return audiencePhone;
    }

    public void setAudiencePhone(String audiencePhone) {
        this.audiencePhone = audiencePhone == null ? null : audiencePhone.trim();
    }

    public Integer getAudienceVipstatus() {
        return audienceVipstatus;
    }

    public void setAudienceVipstatus(Integer audienceVipstatus) {
        this.audienceVipstatus = audienceVipstatus;
    }

    public Integer getVipId() {
        return vipId;
    }

    public void setVipId(Integer vipId) {
        this.vipId = vipId;
    }
}