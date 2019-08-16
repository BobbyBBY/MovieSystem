package com.example.moviesystemmanager.bean;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Movie {
    private String movieId;

    private String harddiskId;

    private String movieName;

    private String movieDoubanid;

    private Date movieOnlinetime;

    private Date movieOfflinetime;

    private Integer movieDuration;

    private Integer movieStatus;

    private String movieCover;

    private String movieIntroduction;

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId == null ? null : movieId.trim();
    }

    public String getHarddiskId() {
        return harddiskId;
    }

    public void setHarddiskId(String harddiskId) {
        this.harddiskId = harddiskId == null ? null : harddiskId.trim();
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName == null ? null : movieName.trim();
    }

    public String getMovieDoubanid() {
        return movieDoubanid;
    }

    public void setMovieDoubanid(String movieDoubanid) {
        this.movieDoubanid = movieDoubanid == null ? null : movieDoubanid.trim();
    }

    public Date getMovieOnlinetime() {
        return movieOnlinetime;
    }

    public void setMovieOnlinetime(Date movieOnlinetime) {
        this.movieOnlinetime = movieOnlinetime;
    }

    public Date getMovieOfflinetime() {
        return movieOfflinetime;
    }

    public void setMovieOfflinetime(Date movieOfflinetime) {
        this.movieOfflinetime = movieOfflinetime;
    }

    public Integer getMovieDuration() {
        return movieDuration;
    }

    public void setMovieDuration(Integer movieDuration) {
        this.movieDuration = movieDuration;
    }

    public Integer getMovieStatus() {
        return movieStatus;
    }

    public void setMovieStatus(Integer movieStatus) {
        this.movieStatus = movieStatus;
    }

    public String getMovieCover() {
        return movieCover;
    }

    public void setMovieCover(String movieCover) {
        this.movieCover = movieCover == null ? null : movieCover.trim();
    }

    public String getMovieIntroduction() {
        return movieIntroduction;
    }

    public void setMovieIntroduction(String movieIntroduction) {
        this.movieIntroduction = movieIntroduction == null ? null : movieIntroduction.trim();
    }
}