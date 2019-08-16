package com.example.demo.mapper;

import com.example.demo.pojo.Screening;

public interface ScreeningMapper {
    int insert(Screening record);

    int insertSelective(Screening record);
}