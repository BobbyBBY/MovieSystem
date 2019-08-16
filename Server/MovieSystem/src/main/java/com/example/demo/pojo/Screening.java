package com.example.demo.pojo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Screening {
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
    private Integer screeningId;

    private String movieId;

    private Integer screeningroomId;

    private Double screeningPrice;

    private Date screeningStarttime;

    private Integer screeningSpecialeffect;

    public Integer getScreeningId() {
        return screeningId;
    }

    public void setScreeningId(Integer screeningId) {
        this.screeningId = screeningId;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId == null ? null : movieId.trim();
    }

    public Integer getScreeningroomId() {
        return screeningroomId;
    }

    public void setScreeningroomId(Integer screeningroomId) {
        this.screeningroomId = screeningroomId;
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
}