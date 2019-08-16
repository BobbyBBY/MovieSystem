package com.example.demo.mapper;

import com.example.demo.pojo.ScreeningView;

public interface ScreeningViewMapper {
    int insert(ScreeningView record);

    int insertSelective(ScreeningView record);
}