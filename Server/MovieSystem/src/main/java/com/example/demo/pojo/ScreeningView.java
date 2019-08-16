package com.example.demo.pojo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class ScreeningView {
    private String movieName;

    private Integer movieDuration;

    private String movieId;

    @Id
    private Integer screeningId;

    private Double screeningPrice;

    private Date screeningStarttime;

    private Integer screeningSpecialeffect;

    private String screeningroomName;

    
    private Integer screeningroomId;

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

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId == null ? null : movieId.trim();
    }

    public Integer getScreeningId() {
        return screeningId;
    }

    public void setScreeningId(Integer screeningId) {
        this.screeningId = screeningId;
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

    public String getScreeningroomName() {
        return screeningroomName;
    }

    public void setScreeningroomName(String screeningroomName) {
        this.screeningroomName = screeningroomName == null ? null : screeningroomName.trim();
    }

    public Integer getScreeningroomId() {
        return screeningroomId;
    }

    public void setScreeningroomId(Integer screeningroomId) {
        this.screeningroomId = screeningroomId;
    }
}