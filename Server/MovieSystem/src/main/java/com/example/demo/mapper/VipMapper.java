package com.example.demo.mapper;

import com.example.demo.pojo.Vip;

public interface VipMapper {
    int insert(Vip record);

    int insertSelective(Vip record);
}