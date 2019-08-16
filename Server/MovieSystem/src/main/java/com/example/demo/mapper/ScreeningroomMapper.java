package com.example.demo.mapper;

import com.example.demo.pojo.Screeningroom;

public interface ScreeningroomMapper {
    int insert(Screeningroom record);

    int insertSelective(Screeningroom record);
}