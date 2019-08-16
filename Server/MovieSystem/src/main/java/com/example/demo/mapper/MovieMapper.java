package com.example.demo.mapper;

import com.example.demo.pojo.Movie;

public interface MovieMapper {
    int insert(Movie record);

    int insertSelective(Movie record);
}