package com.example.demo.mapper;

import com.example.demo.pojo.OrderView;

public interface OrderViewMapper {
    int insert(OrderView record);

    int insertSelective(OrderView record);
}