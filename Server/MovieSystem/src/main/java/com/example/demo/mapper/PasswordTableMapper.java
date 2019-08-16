package com.example.demo.mapper;

import com.example.demo.pojo.Passwordtable;

public interface PasswordTableMapper {
    int insert(Passwordtable record);

    int insertSelective(Passwordtable record);
}