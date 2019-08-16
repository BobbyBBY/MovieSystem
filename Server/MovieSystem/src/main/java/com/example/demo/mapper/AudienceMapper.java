package com.example.demo.mapper;

import com.example.demo.pojo.Audience;

public interface AudienceMapper {
    int insert(Audience record);

    int insertSelective(Audience record);
}