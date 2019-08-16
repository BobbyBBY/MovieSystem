package com.example.demo.mapper;

import com.example.demo.pojo.Ordertable;

public interface OrderTableMapper {
    int insert(Ordertable record);

    int insertSelective(Ordertable record);
}