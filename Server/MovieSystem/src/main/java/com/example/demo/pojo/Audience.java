package com.example.demo.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Audience {
	@Id
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