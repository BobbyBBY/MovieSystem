package com.example.demo.mapper;

import com.example.demo.pojo.Harddisk;

public interface HarddiskMapper {
    int insert(Harddisk record);

    int insertSelective(Harddisk record);
}