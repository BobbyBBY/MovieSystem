package com.example.demo.mapper;

import com.example.demo.pojo.Seat;

public interface SeatMapper {
    int insert(Seat record);

    int insertSelective(Seat record);
}